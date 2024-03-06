package ie.nok.file.zio

import ie.nok.zio.ZIOOps
import zio.json.JsonCodec
import zio.stream.ZStream
import scala.util.chaining.scalaUtilChainingOps

class ZFileImplSuite extends munit.FunSuite {

  case class Line(foo: String, bar: Int, baz: Boolean)
  given JsonCodec[Line] = JsonCodec.derived[Line]

  val writer = ZFileWriterImpl.jsonLines[Line]
  val reader = ZFileReaderImpl.jsonLines[Line]

  test("FileImplSuite write and read file") {
    val written = List(
      Line("foo", 1, true),
      Line("bar", 2, false),
      Line("baz", 3, true)
    )

    val stream = ZStream.fromIterable(written)

    writer
      .write(stream)
      .flatMap { file => reader.read(file).runCollect }
      .map { _.toList }
      .map { read => assertEquals(written, read) }
      .pipe { ZIOOps.unsafeRun }
  }
}
