package ie.nok.file.zio

import ie.nok.codec.json.zio.ZJsonDecoder
import java.io.File
import zio.ZIO
import zio.json.{EncoderOps, JsonCodec}
import zio.nio.file.Files
import zio.stream.{ZStream, ZSink, ZPipeline}

trait ZFileService[A] {
  def write(lines: ZStream[Any, Throwable, A]): ZIO[Any, Throwable, File]
  def read(file: File): ZStream[Any, Throwable, A]
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
  def jsonLines[A: JsonCodec]: ZFileService[A] =
    ZFileServiceImpl(_.toJson, ZJsonDecoder.decode[A])
}
