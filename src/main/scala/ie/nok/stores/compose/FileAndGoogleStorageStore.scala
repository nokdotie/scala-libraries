package ie.nok.stores.compose

import ie.nok.stores.files.{FileStore, FileStoreImpl}
import ie.nok.stores.google.storage.{StorageStore, StorageStoreImpl}
import scala.util.Try
import zio.json.JsonCodec

trait FileAndGoogleStorageStore[A] {
  def write(bucketName: String, blobName: String, lines: Iterator[A]): Try[Unit]
  def write(bucketAndBlobNames: Iterable[(String, String)], lines: Iterator[A]): Try[Unit]

  def read(bucketName: String, blobName: String): Try[Iterator[A]]
  def listBlobNames(bucketName: String, blobGlobPattern: String): Try[Iterable[String]]
}

class FileAndGoogleStorageStoreImpl[A](
    fileStore: FileStore[A],
    storageStore: StorageStore
) extends FileAndGoogleStorageStore[A] {

  override def write(bucketName: String, blobName: String, lines: Iterator[A]): Try[Unit] =
    write(Iterable((bucketName, blobName)), lines)

  override def write(bucketAndBlobNames: Iterable[(String, String)], lines: Iterator[A]): Try[Unit] = for {
    file <- fileStore.write(lines)
    _ <- Try {
      bucketAndBlobNames
        .map { (bucket, blob) => storageStore.write(bucket, blob, file) }
        .map { _.get }
    }
    _ <- Try { file.delete() }
  } yield ()

  override def read(bucketName: String, blobName: String): Try[Iterator[A]] =
    storageStore.read(bucketName, blobName).flatMap(fileStore.read)

  override def listBlobNames(bucketName: String, blobGlobPattern: String): Try[Iterable[String]] =
    storageStore.listBlobNames(bucketName, blobGlobPattern)

}

object FileAndGoogleStorageStoreImpl {
  def default[A: JsonCodec]: FileAndGoogleStorageStore[A] =
    new FileAndGoogleStorageStoreImpl(
      FileStoreImpl.default[A],
      StorageStoreImpl.default.get
    )
}
