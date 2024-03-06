package ie.nok.codec.json

import scala.util.Try
import scala.util.chaining.scalaUtilChainingOps
import zio.json.{DecoderOps, JsonDecoder}

object Json {

  def decode[A: JsonDecoder](str: String): Try[A] =
    str
      .fromJson[A]
      .left
      .map { error => Throwable(s"Failed to decode: $error, $str") }
      .toTry

}
