package ie.nok.codec.hash

import scala.util.hashing.Hashing

object HashEncoder {
  private val hasher: Hashing[String] = Hashing.Default()

  def encode(str: String): String = hasher.hash(str).toHexString
}
