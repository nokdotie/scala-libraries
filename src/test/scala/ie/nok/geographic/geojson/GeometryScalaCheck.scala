package ie.nok.geographic.geojson

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}

private val genPoint: Gen[Geometry] = {
  val point: Gen[Geometry] = for {
    latitude  <- arbitrary[BigDecimal]
    longitude <- arbitrary[BigDecimal]
    coordinates = List(latitude, longitude)
    point       = Geometry(coordinates = coordinates)
  } yield point

  point
}

given Arbitrary[Geometry] = Arbitrary(genPoint)
