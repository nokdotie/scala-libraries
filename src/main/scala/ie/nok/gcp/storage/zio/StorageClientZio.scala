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

trait StorageClientZio {
  def write(bucketName: String, blobName: String, file: File): ZIO[Any, Throwable, Unit]
  def read(bucketName: String, blobName: String): ZIO[Any, Throwable, File]
  def listBlobNames(bucketName: String, blobGlobPattern: String): ZIO[Any, Throwable, Iterable[String]]
}

object StorageClientZio {

  def write(bucketName: String, blobName: String, file: File): ZIO[StorageClientZio, Throwable, Unit] =
    ZIO.serviceWithZIO[StorageClientZio](_.write(bucketName, blobName, file))
  def read(bucketName: String, blobName: String): ZIO[StorageClientZio, Throwable, File] = ZIO.serviceWithZIO[StorageClientZio](_.read(bucketName, blobName))
  def listBlobNames(bucketName: String, blobGlobPattern: String): ZIO[StorageClientZio, Throwable, Iterable[String]] =
    ZIO.serviceWithZIO[StorageClientZio](_.listBlobNames(bucketName, blobGlobPattern))

}

class StorageClientZioImpl(storageClient: StorageClient) extends StorageClientZio {

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

object StorageClientZioImpl {

  val layer: ZLayer[Any, Throwable, StorageClientZio] =
    ZIO
      .fromTry { StorageClientImpl.default }
      .map { StorageClientZioImpl(_) }
      .pipe { ZLayer.fromZIO(_) }

}
