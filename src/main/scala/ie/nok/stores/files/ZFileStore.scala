package ie.nok.stores.files

import ie.nok.codecs.json.ZJson
import java.io.File
import zio.{Tag, ZIO, ZLayer}
import zio.json.{EncoderOps, JsonCodec}
import zio.nio.file.Files
import zio.stream.{ZStream, ZSink, ZPipeline}

trait ZFileStore[A] {
  def write[R](lines: ZStream[R, Throwable, A]): ZIO[R, Throwable, File]
  def read(file: File): ZStream[Any, Throwable, A]
}

object ZFileStore {
  def write[R, A: Tag](lines: ZStream[R, Throwable, A]): ZIO[R & ZFileStore[A], Throwable, File] =
    ZIO.serviceWithZIO[ZFileStore[A]](_.write(lines))

  def read[A: Tag](file: File): ZStream[ZFileStore[A], Throwable, A] =
    ZStream.serviceWithStream[ZFileStore[A]](_.read(file))
}

class ZFileStoreImpl[A](
    encode: A => String,
    decode: String => ZIO[Any, Throwable, A]
) extends ZFileStore[A] {
  override def write[R](lines: ZStream[R, Throwable, A]): ZIO[R, Throwable, File] = for {
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

object ZFileStoreImpl {

  def layer[A: Tag: JsonCodec]: ZLayer[Any, Throwable, ZFileStore[A]] =
    ZLayer.succeed { ZFileStoreImpl[A](_.toJson, ZJson.decode[A]) }

}
