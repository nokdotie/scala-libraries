package ie.nok.util

import java.util.Base64
import scala.util.chaining.scalaUtilChainingOps
import zio.ZIO
import zio.json._

object Base64Json {
  private val encoder = Base64.getUrlEncoder.withoutPadding()
  private val decoder = Base64.getUrlDecoder
  private val charset = "UTF-8"

  def encode[A: JsonEncoder](value: A): String =
    value.toJson.getBytes(charset).pipe(encoder.encodeToString)

  def decode[A: JsonDecoder](str: String): ZIO[Any, Throwable, A] =
    ZIO.attempt { decoder.decode(str) }
      .map { new String(_, charset) }
      .flatMap { json =>
          json.fromJson[A]
              .left
              .map { err => Throwable(s"$err: $json") }
              .pipe(ZIO.fromEither)
      }
}
