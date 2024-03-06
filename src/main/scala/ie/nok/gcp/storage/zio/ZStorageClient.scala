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

trait ZStorageClient {
  def write(bucketName: String, blobName: String, file: File): ZIO[Any, Throwable, Unit]
  def read(bucketName: String, blobName: String): ZIO[Any, Throwable, File]
  def listBlobNames(bucketName: String, blobGlobPattern: String): ZIO[Any, Throwable, Iterable[String]]
}

object ZStorageClient {

  def write(bucketName: String, blobName: String, file: File): ZIO[ZStorageClient, Throwable, Unit] =
    ZIO.serviceWithZIO[ZStorageClient](_.write(bucketName, blobName, file))
  def read(bucketName: String, blobName: String): ZIO[ZStorageClient, Throwable, File] = ZIO.serviceWithZIO[ZStorageClient](_.read(bucketName, blobName))
  def listBlobNames(bucketName: String, blobGlobPattern: String): ZIO[ZStorageClient, Throwable, Iterable[String]] =
    ZIO.serviceWithZIO[ZStorageClient](_.listBlobNames(bucketName, blobGlobPattern))

}

class ZStorageClientImpl(storageClient: StorageClient) extends ZStorageClient {

  def write(bucketName: String, blobName: String, file: File): ZIO[Any, Throwable, Unit] = ZIO.fromTry {
    storageClient.write(bucketName, blobName, file)
  }

  def read(bucketName: String, blobName: String): ZIO[Any, Throwable, File] = ZIO.fromTry {
    storageClient.read(bucketName, blobName)
  }

  def listBlobNames(bucketName: String, blobGlobPattern: String): ZIO[Any, Throwable, Iterable[String]] = ZIO.fromTry {
    storageClient.listBlobNames(bucketName, blobGlobPattern)
  }

}

object ZStorageClientImpl {

  val layer: ZLayer[Any, Throwable, ZStorageClient] =
    ZIO
      .fromTry { StorageClientImpl.default }
      .map { ZStorageClientImpl(_) }
      .pipe { ZLayer.fromZIO(_) }

}
