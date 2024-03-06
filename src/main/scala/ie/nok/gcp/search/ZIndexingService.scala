package ie.nok.gcp.search

import scala.util.chaining.scalaUtilChainingOps
import zio.{ZIO, ZLayer}

trait ZIndexingService {

  def update(url: String): ZIO[Any, Throwable, Unit]
  def delete(url: String): ZIO[Any, Throwable, Unit]

}

object ZIndexingService {

  def update(url: String): ZIO[ZIndexingService, Throwable, Unit] = ZIO.serviceWithZIO[ZIndexingService](_.update(url))
  def delete(url: String): ZIO[ZIndexingService, Throwable, Unit] = ZIO.serviceWithZIO[ZIndexingService](_.delete(url))

}

class ZIndexingServiceImpl(indexingService: IndexingService) extends ZIndexingService {

  def update(url: String): ZIO[Any, Throwable, Unit] = ZIO.attempt { indexingService.update(url) }
  def delete(url: String): ZIO[Any, Throwable, Unit] = ZIO.attempt { indexingService.delete(url) }

}

object ZIndexingServiceImpl {

  val layer: ZLayer[Any, Throwable, ZIndexingService] =
    ZIO
      .fromTry { IndexingServiceImpl.default }
      .map { ZIndexingServiceImpl(_) }
      .pipe { ZLayer.fromZIO(_) }

}
