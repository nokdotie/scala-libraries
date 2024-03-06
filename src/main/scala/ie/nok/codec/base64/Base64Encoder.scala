package ie.nok.codec.base64

import java.util.Base64
import scala.util.chaining.scalaUtilChainingOps

object Base64Encoder {
  private val encoder = Base64.getUrlEncoder.withoutPadding()

  def encode(str: String): String =
    str.getBytes().pipe(encoder.encodeToString)

}
