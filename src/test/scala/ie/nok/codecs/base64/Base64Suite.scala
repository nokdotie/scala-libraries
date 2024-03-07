package ie.nok.codecs.base64

import scala.util.chaining.scalaUtilChainingOps

class Base64Suite extends munit.FunSuite {

  test("Base64 encode and decode") {
    val written = "foo"

    val read = Base64
      .encode(written)
      .pipe { Base64.decode }
      .getOrElse { fail("Failed to decode") }

    assertEquals(written, read)
  }
}
