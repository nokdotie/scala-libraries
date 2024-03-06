package ie.nok.codec.json

import scala.util.Try
import scala.util.chaining.scalaUtilChainingOps
import _root_.zio.json.{DecoderOps, JsonDecoder}

object JsonDecoder {

  def decode[A: JsonDecoder](str: String): Try[A] =
    str
      .fromJson[A]
      .left
      .map { error => Throwable("Failed to decode: $error, $str") }
      .toTry

}
