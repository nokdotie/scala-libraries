package ie.nok.geographic.geojson

import zio.json.EncoderOps

class GeoJsonSuite extends munit.FunSuite {
  test("GeoJSON encodes to expected JSON") {
    val expected = """{"type":"FeatureCollection","features":[{"type":"Feature","geometry":{"type":"Point","coordinates":[125.6,10.1]},"properties":{"name":"Dinagat Islands"}}]}"""
    val obtained = FeatureCollection(
      features = List(
        Feature(
          geometry = Geometry(
            coordinates = List(
              BigDecimal("125.6"),
              BigDecimal("10.1")
            )
          ),
          properties = Map(
            "name" -> "Dinagat Islands"
          )
        )
      )
    ).toJson

    assertEquals(obtained, expected)
  }
}
