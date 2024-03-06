package ie.nok.file

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import scala.util.Try
import scala.util.chaining.scalaUtilChainingOps
import _root_.zio.json.{EncoderOps, JsonEncoder}

trait FileWriter[A] {
  def write(lines: Iterator[A]): Try[File]
}

class FileWriterImpl[A](
    encode: A => String
) extends FileWriter[A] {
  override def write(lines: Iterator[A]): Try[File] = Try {
    val bytes = lines
      .map { encode }
      .mkString("\n")
      .getBytes(StandardCharsets.UTF_8)

    Files.createTempFile("file-store-impl", ".tmp").pipe { Files.write(_, bytes) }.toFile
  }
}

object FileWriterImpl {
  def jsonLines[A: JsonEncoder]: FileWriter[A] =
    FileWriterImpl(_.toJson)
}
