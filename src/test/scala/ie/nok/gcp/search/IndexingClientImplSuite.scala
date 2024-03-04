package ie.nok.gcp.search

import ie.nok.zio.ZIOOps
import scala.util.chaining.scalaUtilChainingOps

class IndexingClientImplSuite extends munit.FunSuite {
  val instance = IndexingClientImpl.instance.get

  test("GoogleSearchApiImpl notifies Google's Indexing service of an update") {
    instance
      .update("https://nok.ie/example")
      .getOrElse { fail("Failed to update Google Index") }
  }

  test("GoogleSearchApiImpl notifies Google's Indexing service of a delete") {
    instance
      .delete("https://nok.ie/example")
      .getOrElse { fail("Failed to delete from Google Index") }
  }

}
