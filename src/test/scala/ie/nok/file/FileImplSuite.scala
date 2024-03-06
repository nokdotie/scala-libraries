package ie.nok.file

import _root_.zio.json.JsonCodec

class FileImplSuite extends munit.FunSuite {

  case class Line(foo: String, bar: Int, baz: Boolean)
  given JsonCodec[Line] = JsonCodec.derived[Line]

  val writer = FileWriterImpl.jsonLines[Line]
  val reader = FileReaderImpl.jsonLines[Line]

  test("FileImplSuite write and read file") {
    val written = List(
      Line("foo", 1, true),
      Line("bar", 2, false),
      Line("baz", 3, true)
    )

    val file = writer
      .write(written.toIterator)
      .getOrElse { fail("Failed to write to file") }

    val read = reader
      .read(file)
      .getOrElse { fail("Failed to read from file") }
      .toList

    assertEquals(written, read)
  }
}
