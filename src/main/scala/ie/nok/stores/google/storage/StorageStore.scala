package ie.nok.stores.google.storage

import com.google.cloud.storage.{Storage, BlobInfo}
import com.google.cloud.storage.Storage.BlobListOption
import ie.nok.google.Credentials
import java.io.File
import scala.jdk.CollectionConverters._
import scala.util.chaining.scalaUtilChainingOps
import scala.util.Try
import com.google.cloud.storage.StorageOptions

trait StorageStore {
  def write(bucketName: String, blobName: String, file: File): Try[Unit]
  def read(bucketName: String, blobName: String): Try[File]
  def listBlobNames(bucketName: String, blobGlobPattern: String): Try[Iterable[String]]
}

class StorageStoreImpl(storage: Storage) extends StorageStore {

  def write(bucketName: String, blobName: String, file: File): Try[Unit] = Try {
    val blobInfo = BlobInfo.newBuilder(bucketName, blobName).build()
    storage.createFrom(blobInfo, file.toPath())
  }

  def read(bucketName: String, blobName: String): Try[File] = Try {
    val file = File.createTempFile("storage-store-impl", ".tmp")

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

object StorageStoreImpl {

  lazy val default: Try[StorageStoreImpl] =
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
      .map { StorageStoreImpl(_) }

}
