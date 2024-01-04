package ie.nok.geographic

import ie.nok.geographic.geojson.Geometry
import zio.json.{DeriveJsonCodec, JsonCodec}

case class Coordinates(latitude: BigDecimal, longitude: BigDecimal)

object Coordinates {
  given JsonCodec[Coordinates] = DeriveJsonCodec.gen[Coordinates]

  def toGeoJsonGeometry(coordinates: Coordinates): Geometry =
    Geometry(coordinates = List(coordinates.longitude, coordinates.latitude))

  val zero: Coordinates = Coordinates(0, 0)
}
