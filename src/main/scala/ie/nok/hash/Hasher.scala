package ie.nok.hash

import scala.util.hashing.Hashing

object Hasher {
  private val hasher: Hashing[String] = Hashing.Default()

  def hash(str: String): String = hasher.hash(str).toHexString
}
