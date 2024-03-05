package ie.nok.gcp.search.zio

import ie.nok.gcp.search.{IndexingClient, IndexingClientImpl}
import scala.util.chaining.scalaUtilChainingOps
import zio.{ZIO, ZLayer}

trait IndexingClientZio {

  def update(url: String): ZIO[Any, Throwable, Unit]
  def delete(url: String): ZIO[Any, Throwable, Unit]

}

object IndexingClientZio {
  def update(url: String): ZIO[IndexingClientZio, Throwable, Unit] = ZIO.serviceWithZIO[IndexingClientZio](_.update(url))
  def delete(url: String): ZIO[IndexingClientZio, Throwable, Unit] = ZIO.serviceWithZIO[IndexingClientZio](_.delete(url))
}

class IndexingClientZioImpl(indexingClient: IndexingClient) extends IndexingClientZio {

  def update(url: String): ZIO[Any, Throwable, Unit] = ZIO.attempt { indexingClient.update(url) }
  def delete(url: String): ZIO[Any, Throwable, Unit] = ZIO.attempt { indexingClient.delete(url) }

}

object IndexingClientZioImpl {

  val layer: ZLayer[Any, Throwable, IndexingClientZio] =
    ZIO
      .fromTry { IndexingClientImpl.instance }
      .map { IndexingClientZioImpl(_) }
      .pipe { ZLayer.fromZIO(_) }

}
