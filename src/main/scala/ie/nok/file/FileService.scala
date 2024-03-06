package ie.nok.file

import ie.nok.codec.json.JsonDecoder
import java.io.File
import java.nio.file.Files
import scala.io.Source
import scala.util.{Try, Using}
import scala.util.chaining.scalaUtilChainingOps
import _root_.zio.json.{EncoderOps, JsonCodec}

trait FileService[A] {
  def write(lines: Iterator[A]): Try[File]
  def read(file: File): Try[Iterator[A]]
}

class FileServiceImpl[A](
    encode: A => String,
    decode: String => Try[A]
) extends FileService[A] {

  override def write(lines: Iterator[A]): Try[File] = Try {
    val bytes = lines
      .map { encode }
      .mkString("\n")
      .getBytes()

    Files.createTempFile("file-store-impl", ".tmp").pipe { Files.write(_, bytes) }.toFile
  }

  override def read(file: File): Try[Iterator[A]] =
    Try {
      Source
        .fromFile(file)
        .getLines()
        .map { decode(_).get }
    }
}

object FileServiceImpl {
  def jsonLines[A: JsonCodec]: FileService[A] =
    FileServiceImpl(_.toJson, JsonDecoder.decode[A])
}
