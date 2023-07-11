package ie.nok.unit

import zio.json.{JsonCodec, DeriveJsonCodec}

case class Coordinates(latitude: BigDecimal, longitude: BigDecimal)

object Coordinates {
  given JsonCodec[Coordinates] = DeriveJsonCodec.gen[Coordinates]
}
