package ie.nok.file.zio

import ie.nok.codec.json.zio.ZJsonDecoder
import java.io.File
import zio.ZIO
import zio.json.{readJsonLinesAs, JsonDecoder}
import zio.stream.{ZStream, ZPipeline}

trait ZFileReader[A] {
  def read(file: File): ZStream[Any, Throwable, A]
}

class ZFileReaderImpl[A](
    decode: String => ZIO[Any, Throwable, A]
) extends ZFileReader[A] {
  override def read(file: File): ZStream[Any, Throwable, A] =
    ZStream
      .fromFile(file)
      .via(ZPipeline.utf8Decode)
      .via(ZPipeline.splitLines)
      .mapZIO { decode }
}

object ZFileReaderImpl {

  def jsonLines[A: JsonDecoder]: ZFileReader[A] =
    ZFileReaderImpl(ZJsonDecoder.decode[A])

}
