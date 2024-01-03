package ie.nok.geographic

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}

private val genCoordinates: Gen[Coordinates] = for {
  latitude  <- arbitrary[BigDecimal]
  longitude <- arbitrary[BigDecimal]
  coordinates = Coordinates(
    latitude = latitude,
    longitude = longitude
  )
} yield coordinates

given Arbitrary[Coordinates] = Arbitrary(genCoordinates)
