package ie.nok.geographic.geojson

import zio.json.{DeriveJsonEncoder, JsonEncoder}

case class Geometry(`type`: String = "Point", coordinates: List[BigDecimal])
object Geometry {
  given JsonEncoder[Geometry] = DeriveJsonEncoder.gen[Geometry]
}
