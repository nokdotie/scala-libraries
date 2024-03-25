package ie.nok.stores.pagination

case class Page[A](
    items: Iterable[A],
    hasPreviousPage: Boolean,
    hasNextPage: Boolean
)
