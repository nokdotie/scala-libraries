package ie.nok.geographic.geojson

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}

private def genFeature[A: Arbitrary]: Gen[Feature[A]] = for {
  geometry   <- arbitrary[Geometry]
  properties <- arbitrary[A]
  feature = Feature(geometry = geometry, properties = properties)
} yield feature

given [A: Arbitrary]: Arbitrary[Feature[A]] = Arbitrary(genFeature)
