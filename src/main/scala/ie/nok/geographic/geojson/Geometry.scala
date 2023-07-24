package ie.nok.geographic.geojson

import zio.json.{JsonEncoder, DeriveJsonEncoder}

case class Geometry(`type`: String = "Point", coordinates: List[BigDecimal])
object Geometry {
    given JsonEncoder[Geometry] = DeriveJsonEncoder.gen[Geometry]
}
