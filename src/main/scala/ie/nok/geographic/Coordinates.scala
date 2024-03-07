package ie.nok.geographic

import zio.json.{DeriveJsonCodec, JsonCodec}

case class Coordinates(latitude: BigDecimal, longitude: BigDecimal)

object Coordinates {
  given JsonCodec[Coordinates] = DeriveJsonCodec.gen[Coordinates]

  val zero: Coordinates = Coordinates(0, 0)
}
