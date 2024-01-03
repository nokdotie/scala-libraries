package ie.nok.geographic

import ch.hsr.geohash.GeoHash as UnderlyingGeoHash

object GeoHash {
  def fromCoordinates(coordinates: Coordinates): String =
    UnderlyingGeoHash.geoHashStringWithCharacterPrecision(
      coordinates.latitude.toDouble,
      coordinates.longitude.toDouble,
      12
    )
}
