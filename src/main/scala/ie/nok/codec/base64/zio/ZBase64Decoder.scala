package ie.nok.codec.base64.zio

import ie.nok.codec.base64.Base64Decoder
import zio.ZIO

object ZBase64Decoder {

  def decode(str: String): ZIO[Any, Throwable, String] =
    ZIO.fromTry(Base64Decoder.decode(str))

}
