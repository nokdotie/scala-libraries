package ie.nok.env

import scala.util.chaining.scalaUtilChainingOps

enum Environment {
  case Production
  case Other
}

object Environment {
  def fromEnvironmentVariable(name: String): Environment =
    sys.env
      .get(name)
      .pipe {
        case Some("production") => Environment.Production
        case _                  => Environment.Other
      }

  def default: Environment = fromEnvironmentVariable("ENV")
}
