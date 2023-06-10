package ie.nok.gcp.storage

import java.net.URL
import java.util.concurrent.TimeUnit

import com.google.api.gax.paging.Page
import com.google.cloud.storage.HmacKey.HmacKeyState
import com.google.cloud.storage.Storage._
import com.google.cloud.storage.{
  Acl,
  Blob,
  BlobId,
  BlobInfo,
  BucketInfo,
  CopyWriter,
  StorageBatch,
  StorageOptions
}
import com.google.cloud.{Policy, ReadChannel, WriteChannel}
import zio._

import scala.jdk.CollectionConverters._

object Storage {
  trait Service {
    def batch: ZIO[Any, Throwable, StorageBatch]
    def compose(composeRequest: ComposeRequest): ZIO[Any, Throwable, Blob]
    def copy(copyRequest: CopyRequest): ZIO[Any, Throwable, CopyWriter]
    def create(
        blobInfo: BlobInfo,
        content: Array[Byte],
        offset: Int,
        length: Int,
        options: List[BlobTargetOption]
    ): ZIO[Any, Throwable, Blob]
    def create(
        blobInfo: BlobInfo,
        content: Array[Byte],
        options: List[BlobTargetOption]
    ): ZIO[Any, Throwable, Blob]
    def createAcl(blobId: BlobId, acl: Acl): ZIO[Any, Throwable, Acl]
    def createAcl(
        bucket: String,
        acl: Acl,
        options: List[BucketSourceOption]
    ): ZIO[Any, Throwable, Acl]
    def createHmacKey(
        serviceAccount: com.google.cloud.storage.ServiceAccount,
        options: List[CreateHmacKeyOption]
    ): ZIO[Any, Throwable, com.google.cloud.storage.HmacKey]
    def delete(blobIds: List[BlobId]): ZIO[Any, Throwable, List[Boolean]]
    def delete(
        blobId: BlobId,
        options: List[BlobSourceOption]
    ): ZIO[Any, Throwable, Boolean]
    def delete(blobIds: Iterable[BlobId]): ZIO[Any, Throwable, List[Boolean]]
    def delete(
        bucket: String,
        options: List[BucketSourceOption]
    ): ZIO[Any, Throwable, Boolean]
    def delete(
        bucket: String,
        blob: String,
        options: List[BlobSourceOption]
    ): ZIO[Any, Throwable, Boolean]
    def deleteAcl(
        blob: BlobId,
        entity: Acl.Entity
    ): ZIO[Any, Throwable, Boolean]
    def deleteAcl(
        bucket: String,
        entity: Acl.Entity,
        options: List[BucketSourceOption]
    ): ZIO[Any, Throwable, Boolean]
    def deleteDefaultAcl(
        bucket: String,
        entity: Acl.Entity
    ): ZIO[Any, Throwable, Boolean]
    def deleteHmacKey(
        hmacKeyMetaData: com.google.cloud.storage.HmacKey.HmacKeyMetadata,
        options: List[DeleteHmacKeyOption]
    ): ZIO[Any, Throwable, Unit]
    def get(blobIds: List[BlobId]): ZIO[Any, Throwable, List[Blob]]
    def get(
        blobId: BlobId,
        options: List[BlobGetOption]
    ): ZIO[Any, Throwable, Option[Blob]]
    def get(blobIds: Iterable[BlobId]): ZIO[Any, Throwable, List[Blob]]
    def getAcl(
        blob: BlobId,
        entity: Acl.Entity
    ): ZIO[Any, Throwable, Option[Acl]]
    def getAcl(
        bucket: String,
        entity: Acl.Entity,
        options: List[BucketSourceOption]
    ): ZIO[Any, Throwable, Option[Acl]]
    def getDefaultAcl(
        bucket: String,
        entity: Acl.Entity
    ): ZIO[Any, Throwable, Option[Acl]]
    def getHmacKey(
        accessId: String,
        options: List[GetHmacKeyOption]
    ): ZIO[Any, Throwable, com.google.cloud.storage.HmacKey.HmacKeyMetadata]
    def getIamPolicy(
        bucket: String,
        options: List[BucketSourceOption]
    ): ZIO[Any, Throwable, Policy]
    def getServiceAccount(
        projectId: String
    ): ZIO[Any, Throwable, com.google.cloud.storage.ServiceAccount]
    def list(
        options: List[BucketListOption]
    ): ZIO[
      Any,
      Throwable,
      com.google.api.gax.paging.Page[com.google.cloud.storage.Bucket]
    ]
    def list(
        bucket: String,
        options: List[BlobListOption]
    ): ZIO[Any, Throwable, Page[Blob]]
    def listAcls(blob: BlobId): ZIO[Any, Throwable, List[Acl]]
    def listAcls(
        bucket: String,
        options: List[BucketSourceOption]
    ): ZIO[Any, Throwable, List[Acl]]
    def listDefaultAcls(bucket: String): ZIO[Any, Throwable, List[Acl]]
    def listHmacKeys(
        options: List[ListHmacKeysOption]
    ): ZIO[Any, Throwable, com.google.api.gax.paging.Page[
      com.google.cloud.storage.HmacKey.HmacKeyMetadata
    ]]
    def lockRetentionPolicy(
        bucketInfo: BucketInfo,
        options: List[BucketTargetOption]
    ): ZIO[Any, Throwable, com.google.cloud.storage.Bucket]
    def readAllBytes(
        blob: BlobId,
        options: List[BlobSourceOption]
    ): ZIO[Any, Throwable, Array[Byte]]
    def readAllBytes(
        bucket: String,
        blob: String,
        options: List[BlobSourceOption]
    ): ZIO[Any, Throwable, Array[Byte]]
    def reader(
        blob: BlobId,
        options: List[BlobSourceOption]
    ): ZIO[Any, Throwable, ReadChannel]
    def reader(
        bucket: String,
        blob: String,
        options: List[BlobSourceOption]
    ): ZIO[Any, Throwable, ReadChannel]
    def setIamPolicy(
        bucket: String,
        policy: Policy,
        options: List[BucketSourceOption]
    ): ZIO[Any, Throwable, Policy]
    def signUrl(
        blobInfo: BlobInfo,
        duration: Long,
        unit: TimeUnit,
        options: List[SignUrlOption]
    ): ZIO[Any, Throwable, URL]
    def testIamPermissions(
        bucket: String,
        permissions: List[String],
        options: List[BucketSourceOption]
    ): ZIO[Any, Throwable, List[Boolean]]
    def update(blobInfos: List[BlobInfo]): ZIO[Any, Throwable, List[Blob]]
    def update(
        blobInfo: BlobInfo,
        options: List[BlobTargetOption]
    ): ZIO[Any, Throwable, Blob]
    def update(
        bucketInfo: BucketInfo,
        options: List[BucketTargetOption]
    ): ZIO[Any, Throwable, com.google.cloud.storage.Bucket]
    def updateAcl(blobId: BlobId, acl: Acl): ZIO[Any, Throwable, Acl]
    def updateAcl(
        bucket: String,
        acl: Acl,
        options: List[BucketSourceOption]
    ): ZIO[Any, Throwable, Acl]
    def updateDefaultAcl(bucket: String, acl: Acl): ZIO[Any, Throwable, Acl]
    def updateHmacKeyState(
        hmacKeyMetaData: com.google.cloud.storage.HmacKey.HmacKeyMetadata,
        state: HmacKeyState,
        options: List[UpdateHmacKeyOption]
    ): ZIO[Any, Throwable, com.google.cloud.storage.HmacKey.HmacKeyMetadata]
    def writer(
        blobInfo: BlobInfo,
        options: List[BlobWriteOption]
    ): ZIO[Any, Throwable, WriteChannel]
    def writer(signedURL: URL): ZIO[Any, Throwable, WriteChannel]
  }

  def live: ZLayer[Scope, Throwable, Storage.Service] =
    ZLayer.fromZIO {

      val acquire = ZIO.attempt(StorageOptions.newBuilder().build().getService)

      ZIO.fromAutoCloseable(acquire).map { storage =>
        new Service {
          override def batch: ZIO[Any, Throwable, StorageBatch] =
            ZIO.attempt(storage.batch())

          override def compose(
              composeRequest: ComposeRequest
          ): ZIO[Any, Throwable, Blob] =
            ZIO.attempt(storage.compose(composeRequest))

          override def copy(
              copyRequest: CopyRequest
          ): ZIO[Any, Throwable, CopyWriter] =
            ZIO.attempt(storage.copy(copyRequest))

          override def create(
              blobInfo: BlobInfo,
              content: Array[Byte],
              offset: Int,
              length: Int,
              options: List[BlobTargetOption]
          ): ZIO[Any, Throwable, Blob] =
            ZIO.attempt(
              storage.create(blobInfo, content, offset, length, options: _*)
            )

          override def create(
              blobInfo: BlobInfo,
              content: Array[Byte],
              options: List[BlobTargetOption]
          ): ZIO[Any, Throwable, Blob] =
            ZIO.attempt(storage.create(blobInfo, content, options: _*))

          override def createAcl(blobId: BlobId, acl: Acl)
              : ZIO[Any, Throwable, Acl] =
            ZIO.attempt(storage.createAcl(blobId, acl))

          override def createAcl(
              bucket: String,
              acl: Acl,
              options: List[BucketSourceOption]
          ): ZIO[Any, Throwable, Acl] =
            ZIO.attempt(storage.createAcl(bucket, acl, options: _*))

          override def createHmacKey(
              serviceAccount: com.google.cloud.storage.ServiceAccount,
              options: List[CreateHmacKeyOption]
          ): ZIO[Any, Throwable, com.google.cloud.storage.HmacKey] =
            ZIO.attempt(storage.createHmacKey(serviceAccount, options: _*))

          override def delete(
              blobIds: List[BlobId]
          ): ZIO[Any, Throwable, List[Boolean]] =
            ZIO.attempt(
              storage.delete(blobIds: _*).asScala.toList.map(Boolean.unbox(_))
            )

          override def delete(blobId: BlobId, options: List[BlobSourceOption])
              : ZIO[Any, Throwable, Boolean] =
            ZIO.attempt(storage.delete(blobId, options: _*))

          override def delete(
              blobIds: Iterable[BlobId]
          ): ZIO[Any, Throwable, List[Boolean]] =
            ZIO.attempt(
              storage
                .delete(blobIds.asJavaCollection)
                .asScala
                .toList
                .map(Boolean.unbox(_))
            )

          override def delete(bucket: String, options: List[BucketSourceOption])
              : ZIO[Any, Throwable, Boolean] =
            ZIO.attempt(storage.delete(bucket, options: _*))

          override def delete(
              bucket: String,
              blob: String,
              options: List[BlobSourceOption]
          ): ZIO[Any, Throwable, Boolean] =
            ZIO.attempt(storage.delete(bucket, blob, options: _*))

          override def deleteAcl(blob: BlobId, entity: Acl.Entity)
              : ZIO[Any, Throwable, Boolean] =
            ZIO.attempt(storage.deleteAcl(blob, entity))

          override def deleteAcl(
              bucket: String,
              entity: Acl.Entity,
              options: List[BucketSourceOption]
          ): ZIO[Any, Throwable, Boolean] =
            ZIO.attempt(storage.deleteAcl(bucket, entity, options: _*))

          override def deleteDefaultAcl(bucket: String, entity: Acl.Entity)
              : ZIO[Any, Throwable, Boolean] =
            ZIO.attempt(storage.deleteDefaultAcl(bucket, entity))

          override def deleteHmacKey(
              hmacKeyMetaData: com.google.cloud.storage.HmacKey.HmacKeyMetadata,
              options: List[DeleteHmacKeyOption]
          ): ZIO[Any, Throwable, Unit] =
            ZIO.attempt(storage.deleteHmacKey(hmacKeyMetaData, options: _*))

          override def get(
              blobIds: List[BlobId]
          ): ZIO[Any, Throwable, List[Blob]] =
            ZIO.attempt(storage.get(blobIds.asJavaCollection).asScala.toList)

          override def get(blobId: BlobId, options: List[BlobGetOption])
              : ZIO[Any, Throwable, Option[Blob]] =
            ZIO.attempt(Option(storage.get(blobId, options: _*)))

          override def get(
              blobIds: Iterable[BlobId]
          ): ZIO[Any, Throwable, List[Blob]] =
            ZIO.attempt(storage.get(blobIds.asJavaCollection).asScala.toList)

          override def getAcl(blob: BlobId, entity: Acl.Entity)
              : ZIO[Any, Throwable, Option[Acl]] =
            ZIO.attempt(Option(storage.getAcl(blob, entity)))

          override def getAcl(
              bucket: String,
              entity: Acl.Entity,
              options: List[BucketSourceOption]
          ): ZIO[Any, Throwable, Option[Acl]] =
            ZIO.attempt(Option(storage.getAcl(bucket, entity, options: _*)))

          override def getDefaultAcl(bucket: String, entity: Acl.Entity)
              : ZIO[Any, Throwable, Option[Acl]] =
            ZIO.attempt(Option(storage.getDefaultAcl(bucket, entity)))

          override def getHmacKey(
              accessId: String,
              options: List[GetHmacKeyOption]
          ): ZIO[
            Any,
            Throwable,
            com.google.cloud.storage.HmacKey.HmacKeyMetadata
          ] =
            ZIO.attempt(storage.getHmacKey(accessId, options: _*))

          override def getIamPolicy(
              bucket: String,
              options: List[BucketSourceOption]
          ): ZIO[Any, Throwable, Policy] =
            ZIO.attempt(storage.getIamPolicy(bucket, options: _*))

          override def getServiceAccount(
              projectId: String
          ): ZIO[Any, Throwable, com.google.cloud.storage.ServiceAccount] =
            ZIO.attempt(storage.getServiceAccount(projectId))

          override def list(
              options: List[BucketListOption]
          ): ZIO[
            Any,
            Throwable,
            com.google.api.gax.paging.Page[com.google.cloud.storage.Bucket]
          ] = ZIO.attempt(storage.list(options: _*))

          override def list(bucket: String, options: List[BlobListOption])
              : ZIO[Any, Throwable, com.google.api.gax.paging.Page[Blob]] =
            ZIO.attempt(storage.list(bucket, options: _*))

          override def listAcls(blob: BlobId): ZIO[Any, Throwable, List[Acl]] =
            ZIO.attempt(storage.listAcls(blob).asScala.toList)

          override def listAcls(
              bucket: String,
              options: List[BucketSourceOption]
          ): ZIO[Any, Throwable, List[Acl]] =
            ZIO.attempt(storage.listAcls(bucket, options: _*).asScala.toList)

          override def listDefaultAcls(
              bucket: String
          ): ZIO[Any, Throwable, List[Acl]] =
            ZIO.attempt(storage.listDefaultAcls(bucket).asScala.toList)

          override def listHmacKeys(
              options: List[ListHmacKeysOption]
          ): ZIO[Any, Throwable, com.google.api.gax.paging.Page[
            com.google.cloud.storage.HmacKey.HmacKeyMetadata
          ]] =
            ZIO.attempt(storage.listHmacKeys(options: _*))

          override def lockRetentionPolicy(
              bucketInfo: BucketInfo,
              options: List[BucketTargetOption]
          ): ZIO[Any, Throwable, com.google.cloud.storage.Bucket] =
            ZIO.attempt(storage.lockRetentionPolicy(bucketInfo, options: _*))

          override def readAllBytes(
              blob: BlobId,
              options: List[BlobSourceOption]
          ): ZIO[Any, Throwable, Array[Byte]] =
            ZIO.attempt(storage.readAllBytes(blob, options: _*))

          override def readAllBytes(
              bucket: String,
              blob: String,
              options: List[BlobSourceOption]
          ): ZIO[Any, Throwable, Array[Byte]] =
            ZIO.attempt(storage.readAllBytes(bucket, blob, options: _*))

          override def reader(blob: BlobId, options: List[BlobSourceOption])
              : ZIO[Any, Throwable, ReadChannel] =
            ZIO.attempt(storage.reader(blob, options: _*))

          override def reader(
              bucket: String,
              blob: String,
              options: List[BlobSourceOption]
          ): ZIO[Any, Throwable, ReadChannel] =
            ZIO.attempt(storage.reader(bucket, blob, options: _*))

          override def setIamPolicy(
              bucket: String,
              policy: Policy,
              options: List[BucketSourceOption]
          ): ZIO[Any, Throwable, Policy] =
            ZIO.attempt(storage.setIamPolicy(bucket, policy, options: _*))

          override def signUrl(
              blobInfo: BlobInfo,
              duration: Long,
              unit: TimeUnit,
              options: List[SignUrlOption]
          ): ZIO[Any, Throwable, URL] =
            ZIO.attempt(storage.signUrl(blobInfo, duration, unit, options: _*))

          override def testIamPermissions(
              bucket: String,
              permissions: List[String],
              options: List[BucketSourceOption]
          ): ZIO[Any, Throwable, List[Boolean]] =
            ZIO.attempt(
              storage
                .testIamPermissions(bucket, permissions.asJava, options: _*)
                .asScala
                .toList
                .map(Boolean.unbox(_))
            )
          override def update(
              blobInfos: List[BlobInfo]
          ): ZIO[Any, Throwable, List[Blob]] =
            ZIO.attempt(storage.update(blobInfos: _*).asScala.toList)

          override def update(
              blobInfo: BlobInfo,
              options: List[BlobTargetOption]
          ): ZIO[Any, Throwable, Blob] =
            ZIO.attempt(storage.update(blobInfo, options: _*))

          override def update(
              bucketInfo: BucketInfo,
              options: List[BucketTargetOption]
          ): ZIO[Any, Throwable, com.google.cloud.storage.Bucket] =
            ZIO.attempt(storage.update(bucketInfo, options: _*))

          override def updateAcl(blobId: BlobId, acl: Acl)
              : ZIO[Any, Throwable, Acl] =
            ZIO.attempt(storage.updateAcl(blobId, acl))

          override def updateAcl(
              bucket: String,
              acl: Acl,
              options: List[BucketSourceOption]
          ): ZIO[Any, Throwable, Acl] =
            ZIO.attempt(storage.updateAcl(bucket, acl, options: _*))

          override def updateDefaultAcl(bucket: String, acl: Acl)
              : ZIO[Any, Throwable, Acl] =
            ZIO.attempt(storage.updateDefaultAcl(bucket, acl))

          override def updateHmacKeyState(
              hmacKeyMetaData: com.google.cloud.storage.HmacKey.HmacKeyMetadata,
              state: HmacKeyState,
              options: List[UpdateHmacKeyOption]
          ): ZIO[
            Any,
            Throwable,
            com.google.cloud.storage.HmacKey.HmacKeyMetadata
          ] =
            ZIO.attempt(
              storage.updateHmacKeyState(hmacKeyMetaData, state, options: _*)
            )

          override def writer(
              blobInfo: BlobInfo,
              options: List[BlobWriteOption]
          ): ZIO[Any, Throwable, WriteChannel] =
            ZIO.attempt(storage.writer(blobInfo, options: _*))

          override def writer(
              signedURL: URL
          ): ZIO[Any, Throwable, WriteChannel] =
            ZIO.attempt(storage.writer(signedURL))
        }
      }
    }

}
