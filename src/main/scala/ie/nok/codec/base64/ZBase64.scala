package ie.nok.codec.base64

import zio.ZIO

object ZBase64 {

  def decode(str: String): ZIO[Any, Throwable, String] =
    ZIO.fromTry(Base64.decode(str))

}
