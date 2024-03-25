package ie.nok.stores.pagination

case class Page[A](
    items: List[A],
    pageInfo: PageInfo
)
