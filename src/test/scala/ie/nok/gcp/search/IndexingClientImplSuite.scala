package ie.nok.gcp.search

class IndexingClientImplSuite extends munit.FunSuite {
  val instance = IndexingClientImpl.default.get

  test("GoogleSearchApiImpl notifies Google's Indexing service of an update") {
    instance
      .update("https://nok.ie/example")
      .getOrElse { fail("Failed to update Google Index") }
  }

  test("GoogleSearchApiImpl notifies Google's Indexing service of a delete") {
    instance
      .delete("https://nok.ie/example")
      .getOrElse { fail("Failed to delete Google Index") }
  }

}
