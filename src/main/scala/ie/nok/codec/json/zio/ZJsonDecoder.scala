package ie.nok.codec.json.zio

import ie.nok.codec.json.JsonDecoder
import zio.ZIO
import zio.json.{DecoderOps, JsonDecoder as ZioJsonDecoder}

object ZJsonDecoder {

  def decode[A: ZioJsonDecoder](str: String): ZIO[Any, Throwable, A] =
    ZIO.fromTry(JsonDecoder.decode(str))

}
