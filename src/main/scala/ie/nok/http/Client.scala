package ie.nok.http

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import scala.util.chaining.scalaUtilChainingOps
import zio.ZIO
import zio.http.{Body, Client => ZioClient}
import zio.json.{DecoderOps, JsonDecoder}
import zio.http.model.{Method, Headers}

object Client {

  def requestBody(
      url: String,
      method: Method = Method.GET,
      headers: Headers = Headers.empty,
      content: Body = Body.empty
  ): ZIO[ZioClient, Throwable, String] =
    ZioClient
      .request(url, method, headers, content)
      .flatMap { _.body.asString }

  def requestBodyAsJson[A: JsonDecoder](
      url: String,
      method: Method = Method.GET,
      headers: Headers = Headers.empty,
      content: Body = Body.empty
  ): ZIO[ZioClient, Throwable, A] =
    requestBody(url, method, headers, content)
      .flatMap {
        _.fromJson[A].left
          .map(Throwable(_))
          .pipe(ZIO.fromEither)
      }

  def requestBodyAsHtml(
      url: String,
      method: Method = Method.GET,
      headers: Headers = Headers.empty,
      content: Body = Body.empty
  ): ZIO[ZioClient, Throwable, Document] =
    requestBody(url, method, headers, content)
      .flatMap { html => ZIO.attempt { Jsoup.parse(html) } }
}
