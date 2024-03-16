package ie.nok.seo.indexnow

import zio.{ZIO, ZLayer}
import zio.json.{JsonEncoder, EncoderOps}
import zio.http.{Body, Client}
import zio.http.model.{Method, Headers}

trait ZIndexNowService {
  def submit(urls: Iterable[String]): ZIO[Any, Throwable, Unit]
}

object ZIndexNowService {
  def submit(urls: Iterable[String]): ZIO[ZIndexNowService, Throwable, Unit] =
    ZIO.serviceWithZIO(_.submit(urls))
}

class ZIndexNowServiceImpl(client: Client) extends ZIndexNowService {
  private case class Payload(
      host: String = "https://nok.ie",
      key: String = IndexNow.key,
      urlList: Iterable[String]
  )
  private given JsonEncoder[Payload] = JsonEncoder.derived[Payload]

  def submit(urls: Iterable[String]): ZIO[Any, Throwable, Unit] = {
    val payload = Payload(urlList = urls)

    Client
      .request(
        "https://api.indexnow.org/indexnow",
        Method.POST,
        Headers("content-type", "application/json"),
        Body.fromString(payload.toJson)
      )
      .flatMap {
        case response if response.status.isSuccess => ZIO.unit
        case response => response.body.asString.flatMap { body => ZIO.fail(Throwable(s"Failed to submit URLs: ${response.status}, ${body}")) }
      }
      .provide(ZLayer.succeed(client))
  }
}

object ZIndexNowServiceImpl {

  val layer: ZLayer[Any, Throwable, ZIndexNowService] =
    Client.default.andTo { ZLayer.fromFunction { ZIndexNowServiceImpl(_) } }

}
