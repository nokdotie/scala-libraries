package ie.nok.base64

import java.util.Base64
import zio.ZIO

object Base64Decoder {
  private val decoder = Base64.getUrlDecoder

  def decode(str: String): ZIO[Any, Throwable, String] =
    ZIO
      .attempt { decoder.decode(str) }
      .map { new String(_) }
}
