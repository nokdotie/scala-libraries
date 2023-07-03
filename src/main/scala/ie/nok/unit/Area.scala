package ie.nok.unit

import zio.json.{JsonCodec, DeriveJsonCodec}

enum AreaUnit {
  case SquareMetres
  case SquareFeet
  case Acres
  case Hectares
}
object AreaUnit {
  given JsonCodec[AreaUnit] = DeriveJsonCodec.gen[AreaUnit]
}

case class Area(value: BigDecimal, unit: AreaUnit)
object Area {
  given JsonCodec[Area] = DeriveJsonCodec.gen[Area]

  def toSquareMetres(area: Area): Area =
    area.unit match {
      case AreaUnit.SquareMetres => area
      case AreaUnit.SquareFeet =>
        new Area(area.value * 0.092903, AreaUnit.SquareMetres)
      case AreaUnit.Acres =>
        new Area(area.value * 4046.86, AreaUnit.SquareMetres)
      case AreaUnit.Hectares =>
        new Area(area.value * 10000, AreaUnit.SquareMetres)
    }

  val empty = new Area(0, AreaUnit.SquareMetres)
}
