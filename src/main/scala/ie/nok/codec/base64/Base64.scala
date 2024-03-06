package ie.nok.codec.base64

import java.util.{Base64 as JBase64}
import scala.util.Try
import scala.util.chaining.scalaUtilChainingOps

object Base64 {
  private val encoder = JBase64.getUrlEncoder.withoutPadding()
  private val decoder = JBase64.getUrlDecoder

  def encode(str: String): String =
    str.getBytes().pipe(encoder.encodeToString)

  def decode(str: String): Try[String] =
    Try { decoder.decode(str) }
      .map { new String(_) }

}
