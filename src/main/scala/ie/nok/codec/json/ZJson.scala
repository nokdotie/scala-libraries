package ie.nok.codec.json

import zio.ZIO
import zio.json.JsonDecoder

object ZJson {

  def decode[A: JsonDecoder](str: String): ZIO[Any, Throwable, A] =
    ZIO.fromTry(Json.decode(str))

}
