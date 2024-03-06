package ie.nok.file

import zio.json.JsonCodec

class FileServiceImplSuite extends munit.FunSuite {

  case class Line(foo: String, bar: Int, baz: Boolean)
  given JsonCodec[Line] = JsonCodec.derived[Line]

  val fileServiceImpl = FileServiceImpl.default[Line]

  test("FileServiceImpl write and read file") {
    val written = List(
      Line("foo", 1, true),
      Line("bar", 2, false),
      Line("baz", 3, true)
    )

    val file = fileServiceImpl
      .write(written.toIterator)
      .getOrElse { fail("Failed to write to file") }

    val read = fileServiceImpl
      .read(file)
      .getOrElse { fail("Failed to read from file") }
      .toList

    assertEquals(written, read)
  }
}
