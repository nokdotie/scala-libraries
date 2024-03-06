package ie.nok.file

import ie.nok.codec.json.JsonDecoder
import java.io.File
import scala.io.Source
import scala.util.{Try, Using}
import _root_.zio.json.{JsonDecoder as ZIOJsonDecoder}

trait FileReader[A] {
  def read(file: File): Try[Iterator[A]]
}

class FileReaderImpl[A](
    decode: String => Try[A]
) extends FileReader[A] {
  override def read(file: File): Try[Iterator[A]] =
    Try {
      Source
        .fromFile(file)
        .getLines()
        .map { decode(_).get }
    }
}

object FileReaderImpl {
  def jsonLines[A: ZIOJsonDecoder]: FileReader[A] =
    FileReaderImpl(JsonDecoder.decode[A])

}
