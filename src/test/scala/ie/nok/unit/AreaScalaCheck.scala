package ie.nok.unit

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}

private val genArea: Gen[Area] = for {
  value <- arbitrary[BigDecimal]
  unit  <- Gen.oneOf(AreaUnit.values)
  area = Area(value = value, unit = unit)
} yield area

given arbArea: Arbitrary[Area] = Arbitrary(genArea)
