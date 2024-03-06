package ie.nok.file.zio

import java.io.File
import zio.ZIO
import zio.json.{EncoderOps, JsonEncoder}
import zio.nio.file.Files
import zio.stream.{ZStream, ZSink, ZPipeline}

trait ZFileWriter[A] {
  def write(lines: ZStream[Any, Throwable, A]): ZIO[Any, Throwable, File]
}

class ZFileWriterImpl[A](
    encode: A => String
) extends ZFileWriter[A] {
  override def write(lines: ZStream[Any, Throwable, A]): ZIO[Any, Throwable, File] = for {
    file <- Files.createTempFile(".tmp", Some("file-store-impl"), Nil).map { _.toFile }
    sink = ZSink.fromFile(file)
    _ <- lines
      .map { encode }
      .intersperse("\n")
      .via { ZPipeline.utf8Encode }
      .run(sink)
  } yield file
}

object ZFileWriterImpl {
  def jsonLines[A: JsonEncoder]: ZFileWriter[A] =
    ZFileWriterImpl(_.toJson)
}
