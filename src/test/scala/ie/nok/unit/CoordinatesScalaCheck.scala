package ie.nok.unit

import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Arbitrary.arbitrary

val genCoordinates: Gen[Coordinates] = for {
  latitude <- arbitrary[BigDecimal]
  longitude <- arbitrary[BigDecimal]
  coordinates = Coordinates(
    latitude = latitude,
    longitude = longitude
  )
} yield coordinates

implicit val arbCoordinates: Arbitrary[Coordinates] =
  Arbitrary(genCoordinates)
