package ie.nok.stores.files

import zio.json.JsonCodec

class FileStoreImplSuite extends munit.FunSuite {

  case class Line(foo: String, bar: Int, baz: Boolean)
  given JsonCodec[Line] = JsonCodec.derived[Line]

  val fileStoreImpl = FileStoreImpl.default[Line]

  test("FileStoreImpl write and read file") {
    val written = List(
      Line("foo", 1, true),
      Line("bar", 2, false),
      Line("baz", 3, true)
    )

    val file = fileStoreImpl
      .write(written.toIterator)
      .getOrElse { fail("Failed to write to file") }

    val read = fileStoreImpl
      .read(file)
      .getOrElse { fail("Failed to read from file") }
      .toList

    assertEquals(written, read)
  }
}
