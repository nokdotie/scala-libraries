package ie.nok.seo.indexnow

import scala.util.Random

object IndexNow {
  val key = s"indexnow-${Random.apply(1).alphanumeric.take(32).mkString}"
}
