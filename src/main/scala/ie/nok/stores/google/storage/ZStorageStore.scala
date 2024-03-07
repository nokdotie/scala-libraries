package ie.nok.stores.google.storage

import com.google.cloud.storage.{Storage, BlobInfo}
import com.google.cloud.storage.Storage.BlobListOption
import ie.nok.google.Credentials
import java.io.File
import scala.jdk.CollectionConverters._
import scala.util.chaining.scalaUtilChainingOps
import scala.util.Try
import com.google.cloud.storage.StorageOptions
import zio.{ZIO, ZLayer}
import zio.stream.ZStream

trait ZStorageStore {
  def write(bucketName: String, blobName: String, file: File): ZIO[Any, Throwable, Unit]
  def read(bucketName: String, blobName: String): ZIO[Any, Throwable, File]
  def listBlobNames(bucketName: String, blobGlobPattern: String): ZStream[Any, Throwable, String]
}

object ZStorageStore {

  def write(bucketName: String, blobName: String, file: File): ZIO[ZStorageStore, Throwable, Unit] =
    ZIO.serviceWithZIO[ZStorageStore](_.write(bucketName, blobName, file))
  def read(bucketName: String, blobName: String): ZIO[ZStorageStore, Throwable, File] = ZIO.serviceWithZIO[ZStorageStore](_.read(bucketName, blobName))
  def listBlobNames(bucketName: String, blobGlobPattern: String): ZStream[ZStorageStore, Throwable, String] =
    ZStream.serviceWithStream[ZStorageStore](_.listBlobNames(bucketName, blobGlobPattern))

}

class ZStorageStoreImpl(storageStore: StorageStore) extends ZStorageStore {

  def write(bucketName: String, blobName: String, file: File): ZIO[Any, Throwable, Unit] = ZIO.fromTry {
    storageStore.write(bucketName, blobName, file)
  }

  def read(bucketName: String, blobName: String): ZIO[Any, Throwable, File] = ZIO.fromTry {
    storageStore.read(bucketName, blobName)
  }

  def listBlobNames(bucketName: String, blobGlobPattern: String): ZStream[Any, Throwable, String] = ZIO
    .fromTry {
      storageStore.listBlobNames(bucketName, blobGlobPattern)
    }
    .pipe { ZStream.fromIterableZIO }

}

object ZStorageStoreImpl {

  val layer: ZLayer[Any, Throwable, ZStorageStore] =
    ZIO
      .fromTry { StorageStoreImpl.default }
      .map { ZStorageStoreImpl(_) }
      .pipe { ZLayer.fromZIO(_) }

}
