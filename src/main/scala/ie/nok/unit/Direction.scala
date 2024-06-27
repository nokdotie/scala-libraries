package ie.nok.unit

import zio.json.{DeriveJsonCodec, JsonCodec}

enum Direction {
  case Ascending
  case Descending
}

object Direction {
  given JsonCodec[Direction] = DeriveJsonCodec.gen[Direction]
}
