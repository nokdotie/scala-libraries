package ie.nok.geographic

import zio.json.{DeriveJsonCodec, JsonCodec}

enum CoordinatesFilter {
  case Empty
  case And(head: CoordinatesFilter, tail: CoordinatesFilter*)
  case Or(head: CoordinatesFilter, tail: CoordinatesFilter*)

  case WithinRectangle(northEast: Coordinates, southWest: Coordinates)

  def filter(value: Coordinates): Boolean = this match {
    case Empty            => true
    case And(head, tail*) => (head +: tail).forall(_.filter(value))
    case Or(head, tail*)  => (head +: tail).exists(_.filter(value))

    case WithinRectangle(northEast, southWest) =>
      value.latitude <= northEast.latitude && value.latitude >= southWest.latitude &&
      value.longitude <= northEast.longitude && value.longitude >= southWest.longitude
  }
}

object CoordinatesFilter {
  given JsonCodec[CoordinatesFilter] = DeriveJsonCodec.gen[CoordinatesFilter]
}
