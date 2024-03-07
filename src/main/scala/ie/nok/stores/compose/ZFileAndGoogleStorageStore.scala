package ie.nok.stores.compose

import ie.nok.stores.files.{ZFileStore, ZFileStoreImpl}
import ie.nok.stores.google.storage.{ZStorageStore, ZStorageStoreImpl}
import scala.util.Try
import scala.util.chaining.scalaUtilChainingOps
import zio.{Tag, ZIO, ZLayer}
import zio.json.JsonCodec
import zio.stream.ZStream

trait ZFileAndGoogleStorageStore[A] {
  def write[R](bucketName: String, blobName: String, lines: ZStream[R, Throwable, A]): ZIO[R, Throwable, Unit]
  def write[R](bucketAndBlobNames: Iterable[(String, String)], lines: ZStream[R, Throwable, A]): ZIO[R, Throwable, Unit]

  def read(bucketName: String, blobName: String): ZStream[Any, Throwable, A]
  def listBlobNames(bucketName: String, blobGlobPattern: String): ZStream[Any, Throwable, String]
}

object ZFileAndGoogleStorageStore {

  def write[R, A: Tag](bucketName: String, blobName: String, lines: ZStream[R, Throwable, A]): ZIO[R & ZFileAndGoogleStorageStore[A], Throwable, Unit] =
    ZIO.serviceWithZIO[ZFileAndGoogleStorageStore[A]](_.write(bucketName, blobName, lines))

  def write[R, A: Tag](
      bucketAndBlobNames: Iterable[(String, String)],
      lines: ZStream[R, Throwable, A]
  ): ZIO[R & ZFileAndGoogleStorageStore[A], Throwable, Unit] =
    ZIO.serviceWithZIO[ZFileAndGoogleStorageStore[A]](_.write(bucketAndBlobNames, lines))

  def read[A: Tag](bucketName: String, blobName: String): ZStream[ZFileAndGoogleStorageStore[A], Throwable, A] =
    ZStream.serviceWithStream[ZFileAndGoogleStorageStore[A]](_.read(bucketName, blobName))

  def listBlobNames[A: Tag](bucketName: String, blobGlobPattern: String): ZStream[ZFileAndGoogleStorageStore[A], Throwable, String] =
    ZStream.serviceWithStream[ZFileAndGoogleStorageStore[A]](_.listBlobNames(bucketName, blobGlobPattern))
}

class ZFileAndGoogleStorageStoreImpl[A](
    zFileStore: ZFileStore[A],
    zStorageStore: ZStorageStore
) extends ZFileAndGoogleStorageStore[A] {

  override def write[R](bucketName: String, blobName: String, lines: ZStream[R, Throwable, A]): ZIO[R, Throwable, Unit] =
    write(Iterable((bucketName, blobName)), lines)

  override def write[R](bucketAndBlobNames: Iterable[(String, String)], lines: ZStream[R, Throwable, A]): ZIO[R, Throwable, Unit] = for {
    file <- zFileStore.write(lines)
    _ <- bucketAndBlobNames
      .map { (bucket, blob) => zStorageStore.write(bucket, blob, file) }
      .pipe { ZIO.collectAll }
    _ <- ZIO.attempt { file.delete() }
  } yield ()

  override def read(bucketName: String, blobName: String): ZStream[Any, Throwable, A] =
    zStorageStore
      .read(bucketName, blobName)
      .pipe { ZStream.fromZIO }
      .flatMap { file => zFileStore.read(file) }

  override def listBlobNames(bucketName: String, blobGlobPattern: String): ZStream[Any, Throwable, String] =
    zStorageStore.listBlobNames(bucketName, blobGlobPattern)

}

object ZFileAndGoogleStorageStoreImpl {
  def layer[A: JsonCodec: Tag]: ZLayer[Any, Throwable, ZFileAndGoogleStorageStore[A]] =
    ZFileStoreImpl
      .layer[A]
      .and(ZStorageStoreImpl.layer)
      .map { env =>
        val zFileStore    = env.get[ZFileStore[A]]
        val zStorageStore = env.get[ZStorageStore]
        val z             = new ZFileAndGoogleStorageStoreImpl(zFileStore, zStorageStore)

        env.add(z)
      }
}
