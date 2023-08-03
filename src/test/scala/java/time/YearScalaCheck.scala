package java.time

import org.scalacheck.{Arbitrary, Gen}

private val genYear: Gen[Year] =
  Gen
    .choose(
      min = Year.MIN_VALUE,
      max = Year.MAX_VALUE
    )
    .map(Year.of)

given Arbitrary[Year] = Arbitrary(genYear)
