package ie.nok.gcp.storage

import com.google.cloud.storage.{Storage, BlobInfo}
import com.google.cloud.storage.Storage.BlobListOption
import ie.nok.gcp.Credentials
import java.io.File
import scala.jdk.CollectionConverters._
import scala.util.chaining.scalaUtilChainingOps
import scala.util.Try
import com.google.cloud.storage.StorageOptions
import zio.{ZIO, ZLayer}

trait ZStorageService {
  def write(bucketName: String, blobName: String, file: File): ZIO[Any, Throwable, Unit]
  def read(bucketName: String, blobName: String): ZIO[Any, Throwable, File]
  def listBlobNames(bucketName: String, blobGlobPattern: String): ZIO[Any, Throwable, Iterable[String]]
}

object ZStorageService {

  def write(bucketName: String, blobName: String, file: File): ZIO[ZStorageService, Throwable, Unit] =
    ZIO.serviceWithZIO[ZStorageService](_.write(bucketName, blobName, file))
  def read(bucketName: String, blobName: String): ZIO[ZStorageService, Throwable, File] = ZIO.serviceWithZIO[ZStorageService](_.read(bucketName, blobName))
  def listBlobNames(bucketName: String, blobGlobPattern: String): ZIO[ZStorageService, Throwable, Iterable[String]] =
    ZIO.serviceWithZIO[ZStorageService](_.listBlobNames(bucketName, blobGlobPattern))

}

class ZStorageServiceImpl(storageService: StorageService) extends ZStorageService {

  def write(bucketName: String, blobName: String, file: File): ZIO[Any, Throwable, Unit] = ZIO.fromTry {
    storageService.write(bucketName, blobName, file)
  }

  def read(bucketName: String, blobName: String): ZIO[Any, Throwable, File] = ZIO.fromTry {
    storageService.read(bucketName, blobName)
  }

  def listBlobNames(bucketName: String, blobGlobPattern: String): ZIO[Any, Throwable, Iterable[String]] = ZIO.fromTry {
    storageService.listBlobNames(bucketName, blobGlobPattern)
  }

}

object ZStorageServiceImpl {

  val layer: ZLayer[Any, Throwable, ZStorageService] =
    ZIO
      .fromTry { StorageServiceImpl.default }
      .map { ZStorageServiceImpl(_) }
      .pipe { ZLayer.fromZIO(_) }

}
