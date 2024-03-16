package ie.nok.seo.google.search

class IndexingServiceImplSuite extends munit.FunSuite {
  val instance = IndexingServiceImpl.default.get

  test("IndexingServiceImpl notifies Google's Indexing service of an update".ignore) {
    instance
      .update("https://nok.ie/example")
      .getOrElse { fail("Failed to update Google Index") }
  }

  test("IndexingServiceImpl notifies Google's Indexing service of a delete".ignore) {
    instance
      .delete("https://nok.ie/example")
      .getOrElse { fail("Failed to delete Google Index") }
  }

}
