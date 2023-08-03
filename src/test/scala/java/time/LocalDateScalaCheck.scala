package java.time

import org.scalacheck.{Arbitrary, Gen}

private val genLocalDate: Gen[LocalDate] =
  Gen
    .choose(
      min = LocalDate.MIN.toEpochDay,
      max = LocalDate.MAX.toEpochDay
    )
    .map(LocalDate.ofEpochDay)

given Arbitrary[LocalDate] = Arbitrary(genLocalDate)
