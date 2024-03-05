package ie.nok.gcp.storage

import com.google.cloud.storage.{Storage, BlobInfo}
import com.google.cloud.storage.Storage.BlobListOption
import ie.nok.gcp.Credentials
import java.io.File
import scala.jdk.CollectionConverters._
import scala.util.chaining.scalaUtilChainingOps
import scala.util.Try
import com.google.cloud.storage.StorageOptions

trait StorageClient {
  def write(bucketName: String, blobName: String, file: File): Try[Unit]
  def read(bucketName: String, blobName: String): Try[File]
  def listBlobNames(bucketName: String, blobGlobPattern: String): Try[Iterable[String]]
}

class StorageClientImpl(storage: Storage) extends StorageClient {

  def write(bucketName: String, blobName: String, file: File): Try[Unit] = Try {
    val blobInfo = BlobInfo.newBuilder(bucketName, blobName).build()
    storage.createFrom(blobInfo, file.toPath())
  }

  def read(bucketName: String, blobName: String): Try[File] = Try {
    val file = File.createTempFile("temp", "file")

    storage
      .get(bucketName, blobName)
      .downloadTo(file.toPath())

    file
  }

  def listBlobNames(bucketName: String, blobGlobPattern: String): Try[Iterable[String]] = Try {
    storage
      .list(bucketName, BlobListOption.matchGlob(blobGlobPattern))
      .iterateAll()
      .asScala
      .map { _.getName() }
  }

}

object StorageClientImpl {

  lazy val default: Try[StorageClientImpl] =
    Credentials.default
      .flatMap { credentials =>
        Try {
          StorageOptions.getDefaultInstance
            .toBuilder()
            .setCredentials(credentials)
            .build()
            .getService
        }
      }
      .map { StorageClientImpl(_) }

}
