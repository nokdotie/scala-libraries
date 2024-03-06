package ie.nok.file

import ie.nok.codec.json.ZJson
import java.io.File
import zio.{Tag, ZIO}
import zio.json.{EncoderOps, JsonCodec}
import zio.nio.file.Files
import zio.stream.{ZStream, ZSink, ZPipeline}

trait ZFileService[A] {
  def write(lines: ZStream[Any, Throwable, A]): ZIO[Any, Throwable, File]
  def read(file: File): ZStream[Any, Throwable, A]
}

object ZFileService {
  def write[A: Tag](lines: ZStream[Any, Throwable, A]): ZIO[ZFileService[A], Throwable, File] =
    ZIO.serviceWithZIO[ZFileService[A]](_.write(lines))

  def read[A: Tag](file: File): ZStream[ZFileService[A], Throwable, A] =
    ZStream.serviceWithStream[ZFileService[A]](_.read(file))
}

class ZFileServiceImpl[A](
    encode: A => String,
    decode: String => ZIO[Any, Throwable, A]
) extends ZFileService[A] {
  override def write(lines: ZStream[Any, Throwable, A]): ZIO[Any, Throwable, File] = for {
    file <- Files.createTempFile(".tmp", Some("file-store-impl"), Nil).map { _.toFile }
    sink = ZSink.fromFile(file)
    _ <- lines
      .map { encode }
      .intersperse("\n")
      .via { ZPipeline.utf8Encode }
      .run(sink)
  } yield file

  override def read(file: File): ZStream[Any, Throwable, A] =
    ZStream
      .fromFile(file)
      .via(ZPipeline.utf8Decode)
      .via(ZPipeline.splitLines)
      .mapZIO { decode }
}

object ZFileServiceImpl {
  def default[A: JsonCodec]: ZFileService[A] =
    ZFileServiceImpl(_.toJson, ZJson.decode[A])
}
