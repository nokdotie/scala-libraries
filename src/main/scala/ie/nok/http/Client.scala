package ie.nok.http

import ie.nok.codec.json.zio.ZJsonDecoder
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import zio.http.model.{Headers, Method}
import zio.http.{Body, Client as ZioClient}
import zio.json.JsonDecoder as ZIOJsonDecoder
import zio.nio.file.{Files, Path}
import zio.stream.ZSink
import zio.{Scope, ZIO}

import scala.util.chaining.scalaUtilChainingOps

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

  def requestBodyAsJson[A: ZIOJsonDecoder](
      url: String,
      method: Method = Method.GET,
      headers: Headers = Headers.empty,
      content: Body = Body.empty
  ): ZIO[ZioClient, Throwable, A] =
    requestBody(url, method, headers, content)
      .flatMap { ZJsonDecoder.decode[A] }

  def requestBodyAsHtml(
      url: String,
      method: Method = Method.GET,
      headers: Headers = Headers.empty,
      content: Body = Body.empty
  ): ZIO[ZioClient, Throwable, Document] =
    requestBody(url, method, headers, content)
      .flatMap { html => ZIO.attempt { Jsoup.parse(html) } }

  def requestBodyAsTempFile(
      url: String,
      method: Method = Method.GET,
      headers: Headers = Headers.empty,
      content: Body = Body.empty
  ): ZIO[ZioClient & Scope, Throwable, Path] = for {
    path     <- Files.createTempFileScoped()
    response <- ZioClient.request(url, method, headers, content)
    _        <- response.body.asStream.run(ZSink.fromFile(path.toFile))
  } yield path

}
