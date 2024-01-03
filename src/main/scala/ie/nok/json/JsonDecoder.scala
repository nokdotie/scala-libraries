package ie.nok.json

import zio.ZIO
import zio.json.{DecoderOps, JsonDecoder}

import scala.util.chaining.scalaUtilChainingOps

object JsonDecoder {

  def decode[A: JsonDecoder](str: String): ZIO[Any, Throwable, A] =
    str
      .fromJson[A]
      .left
      .map(Throwable(_))
      .pipe(ZIO.fromEither)

}
