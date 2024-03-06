package ie.nok.filter

import zio.json.{DeriveJsonCodec, JsonCodec}

enum StringFilter {
  case Empty
  case And(head: StringFilter, tail: StringFilter*)
  case Or(head: StringFilter, tail: StringFilter*)

  case Equals(filter: String)
  case EqualsCaseInsensitive(filter: String)
  case ContainsCaseInsensitive(filter: String)
  case StartsWithCaseInsensitive(filter: String)

  def filter(value: String): Boolean = this match {
    case Empty            => true
    case And(head, tail*) => (head +: tail).forall(_.filter(value))
    case Or(head, tail*)  => (head +: tail).exists(_.filter(value))

    case Equals(filter)                => value == filter
    case EqualsCaseInsensitive(filter) => value.toLowerCase() == filter.toLowerCase()
    case ContainsCaseInsensitive(filter) =>
      value.toLowerCase().contains(filter.toLowerCase())
    case StartsWithCaseInsensitive(filter) =>
      value.toLowerCase().startsWith(filter.toLowerCase())
  }
}

object StringFilter {
  given JsonCodec[StringFilter] = DeriveJsonCodec.gen[StringFilter]
}
