package ie.nok.geographic.geojson

import zio.json.{DeriveJsonEncoder, JsonEncoder}

case class Feature[A](
    `type`: String = "Feature",
    geometry: Geometry,
    properties: A
)

object Feature {
  given [A: JsonEncoder]: JsonEncoder[Feature[A]] =
    DeriveJsonEncoder.gen[Feature[A]]
}
