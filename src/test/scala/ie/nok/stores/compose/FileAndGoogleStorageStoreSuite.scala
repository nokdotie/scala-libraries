package ie.nok.stores.compose

import ie.nok.stores.google.storage.StorageConvention
import zio.json.JsonCodec

class FileAndGoogleStorageStoreSuite extends munit.FunSuite {

  case class Line(foo: String, bar: Int, baz: Boolean)
  given JsonCodec[Line] = JsonCodec.derived[Line]

  val fileAndGoogleStorageStoreImpl = FileAndGoogleStorageStoreImpl.default[Line]

  test("FileAndGoogleStorageStoreImpl write and read file and google") {
    val written = List(
      Line("foo", 1, true),
      Line("bar", 2, false),
      Line("baz", 3, true)
    )

    val blobName = s"scala-libraries/test/${getClass.getSimpleName}.txt"

    fileAndGoogleStorageStoreImpl
      .write(StorageConvention.bucketName, blobName, written.toIterator)
      .getOrElse { fail("Failed to write to file and google storage") }

    val read = fileAndGoogleStorageStoreImpl
      .read(StorageConvention.bucketName, blobName)
      .getOrElse { fail("Failed to read from google storage and file") }
      .toList

    assertEquals(written, read)
  }
}
