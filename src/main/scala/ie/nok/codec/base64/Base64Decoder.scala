package ie.nok.codec.base64

import java.util.Base64
import scala.util.Try

object Base64Decoder {
  private val decoder = Base64.getUrlDecoder

  def decode(str: String): Try[String] =
    Try { decoder.decode(str) }
      .map { new String(_) }

}
