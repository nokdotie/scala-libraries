package ie.nok.codecs.hash

class HashSuite extends munit.FunSuite {

  test("Hash should be referencial transparent") {
    assertEquals(
      Hash.encode("foo"),
      Hash.encode("foo")
    )
  }

}
