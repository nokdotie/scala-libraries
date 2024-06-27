package ie.nok.unit

import org.scalacheck.{Arbitrary, Gen}

private val genDirection: Gen[Direction] =
  Gen.oneOf(Direction.values)

given arbDirection: Arbitrary[Direction] = Arbitrary(genDirection)
