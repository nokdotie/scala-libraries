package ie.nok.unit

import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Arbitrary.arbitrary
import java.time.Instant

val genArea: Gen[Area] = for {
  value <- arbitrary[BigDecimal]
  unit <- Gen.oneOf(AreaUnit.values)
  area = Area(value = value, unit = unit)
} yield area

implicit val arbArea: Arbitrary[Area] =
  Arbitrary(genArea)
