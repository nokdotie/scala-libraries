package ie.nok.stores.filters

import zio.json.{DeriveJsonCodec, JsonCodec}

enum IntFilter {
  case Empty
  case And(head: IntFilter, tail: IntFilter*)
  case Or(head: IntFilter, tail: IntFilter*)

  case Equals(filter: Int)
  case GreaterThan(filter: Int)
  case LessThan(filter: Int)

  def filter(value: Int): Boolean = this match {
    case Empty            => true
    case And(head, tail*) => (head +: tail).forall(_.filter(value))
    case Or(head, tail*)  => (head +: tail).exists(_.filter(value))

    case Equals(filter)      => value == filter
    case GreaterThan(filter) => value > filter
    case LessThan(filter)    => value < filter
  }
}

object IntFilter {
  given JsonCodec[IntFilter] = DeriveJsonCodec.gen[IntFilter]

  def GreaterThanOrEqual(filter: Int): IntFilter =
    IntFilter.Or(
      IntFilter.GreaterThan(filter),
      IntFilter.Equals(filter)
    )
  def LessThanOrEqual(filter: Int): IntFilter = IntFilter
    .Or(IntFilter.LessThan(filter), IntFilter.Equals(filter))
}
