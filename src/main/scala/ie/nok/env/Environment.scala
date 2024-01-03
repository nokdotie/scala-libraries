package ie.nok.env

import zio.{System, ZIO}

enum Environment {
  case Production
  case Other
}

object Environment {
  val get: ZIO[Any, SecurityException, Environment] =
    System
      .env("ENV")
      .map {
        case Some("production") => Environment.Production
        case _                  => Environment.Other
      }
}
