package ie.nok.stores.compose

import ie.nok.stores.google.storage.StorageConvention
import ie.nok.zio.ZIOOps
import scala.util.chaining.scalaUtilChainingOps
import zio.stream.ZStream
import zio.json.JsonCodec

class ZFileAndGoogleStorageStoreSuite extends munit.FunSuite {

  case class Line(foo: String, bar: Int, baz: Boolean)
  given JsonCodec[Line] = JsonCodec.derived[Line]

  test("ZFileAndGoogleStorageStoreImpl write and read file and google") {
    val written = List(
      Line("foo", 1, true),
      Line("bar", 2, false),
      Line("baz", 3, true)
    )

    val stream = ZStream.fromIterable(written)

    val blobName = s"scala-libraries/test/${getClass.getSimpleName}.txt"

    ZFileAndGoogleStorageStore
      .write[Any, Line](StorageConvention.bucketName, blobName, stream)
      .flatMap { _ => ZFileAndGoogleStorageStore.read[Line](StorageConvention.bucketName, blobName).runCollect }
      .map { _.toList }
      .map { read => assert(written == read) }
      .provide(ZFileAndGoogleStorageStoreImpl.layer[Line])
      .pipe { ZIOOps.unsafeRun }
  }
}
