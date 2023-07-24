package ie.nok.geographic.geojson

import zio.json.{JsonEncoder, DeriveJsonEncoder}

case class FeatureCollection[A](
    `type`: String = "FeatureCollection",
    features: List[Feature[A]]
)

object FeatureCollection {
  given [A: JsonEncoder]: JsonEncoder[FeatureCollection[A]] =
    DeriveJsonEncoder.gen[FeatureCollection[A]]
}
