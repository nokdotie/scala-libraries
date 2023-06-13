package ie.nok.json

import scala.util.chaining.scalaUtilChainingOps
import zio.ZIO
import zio.json.{DecoderOps, JsonDecoder}

object JsonDecoder {

  def decode[A: JsonDecoder](str: String): ZIO[Any, Throwable, A] =
    str
      .fromJson[A]
      .left
      .map(Throwable(_))
      .pipe(ZIO.fromEither)

}
