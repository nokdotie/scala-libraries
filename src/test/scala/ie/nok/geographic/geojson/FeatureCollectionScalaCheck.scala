package ie.nok.geographic.geojson

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}

private def genFeatureCollection[A: Arbitrary]: Gen[FeatureCollection[A]] =
  for {
    features <- arbitrary[List[Feature[A]]]
    featureCollection = FeatureCollection(features = features)
  } yield featureCollection

given [A: Arbitrary]: Arbitrary[FeatureCollection[A]] = Arbitrary(
  genFeatureCollection
)
