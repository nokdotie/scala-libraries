package ie.nok.gcp.search.zio

import ie.nok.gcp.search.{IndexingClient, IndexingClientImpl}
import scala.util.chaining.scalaUtilChainingOps
import zio.{ZIO, ZLayer}

trait ZIndexingClient {

  def update(url: String): ZIO[Any, Throwable, Unit]
  def delete(url: String): ZIO[Any, Throwable, Unit]

}

object ZIndexingClient {

  def update(url: String): ZIO[ZIndexingClient, Throwable, Unit] = ZIO.serviceWithZIO[ZIndexingClient](_.update(url))
  def delete(url: String): ZIO[ZIndexingClient, Throwable, Unit] = ZIO.serviceWithZIO[ZIndexingClient](_.delete(url))

}

class ZIndexingClientImpl(indexingClient: IndexingClient) extends ZIndexingClient {

  def update(url: String): ZIO[Any, Throwable, Unit] = ZIO.attempt { indexingClient.update(url) }
  def delete(url: String): ZIO[Any, Throwable, Unit] = ZIO.attempt { indexingClient.delete(url) }

}

object ZIndexingClientImpl {

  val layer: ZLayer[Any, Throwable, ZIndexingClient] =
    ZIO
      .fromTry { IndexingClientImpl.default }
      .map { ZIndexingClientImpl(_) }
      .pipe { ZLayer.fromZIO(_) }

}
