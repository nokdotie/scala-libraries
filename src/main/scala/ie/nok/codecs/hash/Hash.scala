package ie.nok.codecs.hash

import scala.util.hashing.Hashing

object Hash {
  private val hasher: Hashing[String] = Hashing.Default()

  def encode(str: String): String = hasher.hash(str).toHexString
}
