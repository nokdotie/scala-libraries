package ie.nok.gcp

import com.google.api.gax.paging.Page
import com.google.cloud.storage.HmacKey.HmacKeyState
import com.google.cloud.storage.Storage._
import com.google.cloud.storage.{
  Acl,
  Blob,
  BlobId,
  BlobInfo,
  Bucket,
  BucketInfo,
  CopyWriter,
  HmacKey,
  ServiceAccount,
  StorageBatch,
  Storage => _
}
import java.nio.file.Path
import java.net.URL
import java.util.concurrent.TimeUnit
import zio.ZIO

package object storage {

  type Storage = Storage.Service

  def batch: ZIO[Storage, Throwable, StorageBatch] =
    ZIO.environmentWithZIO(_.get.batch)

  def compose(composeRequest: ComposeRequest): ZIO[Storage, Throwable, Blob] =
    ZIO.environmentWithZIO(_.get.compose(composeRequest))

  def copy(copyRequest: CopyRequest): ZIO[Storage, Throwable, CopyWriter] =
    ZIO.environmentWithZIO(_.get.copy(copyRequest))

  def create(
      blobInfo: BlobInfo,
      content: Array[Byte],
      offset: Int,
      length: Int,
      options: List[BlobTargetOption]
  ): ZIO[Storage, Throwable, Blob] =
    ZIO.environmentWithZIO(
      _.get.create(blobInfo, content, offset, length, options)
    )

  def create(
      blobInfo: BlobInfo,
      content: Array[Byte],
      options: List[BlobTargetOption]
  ): ZIO[Storage, Throwable, Blob] =
    ZIO.environmentWithZIO(_.get.create(blobInfo: BlobInfo, content, options))

  def create(
      blobInfo: BlobInfo,
      options: List[BlobTargetOption]
  ): ZIO[Storage, Throwable, Blob] =
    ZIO.environmentWithZIO(_.get.create(blobInfo, Array.empty, options))

  def createAcl(blobId: BlobId, acl: Acl): ZIO[Storage, Throwable, Acl] =
    ZIO.environmentWithZIO(_.get.createAcl(blobId, acl))

  def createAcl(bucket: String, acl: Acl): ZIO[Storage, Throwable, Acl] =
    ZIO.environmentWithZIO(_.get.createAcl(bucket, acl, List.empty))

  def createAcl(
      bucket: String,
      acl: Acl,
      options: List[BucketSourceOption]
  ): ZIO[Storage, Throwable, Acl] =
    ZIO.environmentWithZIO(_.get.createAcl(bucket, acl, options))

  def createHmacKey(
      serviceAccount: ServiceAccount,
      options: List[CreateHmacKeyOption]
  ): ZIO[Storage, Throwable, HmacKey] =
    ZIO.environmentWithZIO(_.get.createHmacKey(serviceAccount, options))

  def createFrom(
      blobInfo: BlobInfo,
      path: Path,
      options: List[BlobWriteOption]
  ): ZIO[Storage, Throwable, Blob] =
    ZIO.environmentWithZIO(_.get.createFrom(blobInfo, path, options))

  def delete(blobIds: List[BlobId]): ZIO[Storage, Throwable, List[Boolean]] =
    ZIO.environmentWithZIO(_.get.delete(blobIds))

  def delete(blobId: BlobId): ZIO[Storage, Throwable, Boolean] =
    ZIO.environmentWithZIO(_.get.delete(blobId, List.empty))

  def delete(
      blobId: BlobId,
      options: List[BlobSourceOption]
  ): ZIO[Storage, Throwable, Boolean] =
    ZIO.environmentWithZIO(_.get.delete(blobId, options))

  def delete(
      blobIds: Iterable[BlobId]
  ): ZIO[Storage, Throwable, List[Boolean]] =
    ZIO.environmentWithZIO(_.get.delete(blobIds))

  def delete(
      bucket: String,
      options: List[BucketSourceOption]
  ): ZIO[Storage, Throwable, Boolean] =
    ZIO.environmentWithZIO(_.get.delete(bucket, options))

  def delete(
      bucket: String,
      blob: String,
      options: List[BlobSourceOption]
  ): ZIO[Storage, Throwable, Boolean] =
    ZIO.environmentWithZIO(_.get.delete(bucket, blob, options))

  def deleteAcl(
      blobId: BlobId,
      entity: Acl.Entity
  ): ZIO[Storage, Throwable, Boolean] =
    ZIO.environmentWithZIO(_.get.deleteAcl(blobId, entity))

  def deleteAcl(
      bucket: String,
      entity: Acl.Entity
  ): ZIO[Storage, Throwable, Boolean] =
    ZIO.environmentWithZIO(_.get.deleteAcl(bucket, entity, List.empty))

  def deleteAcl(
      bucket: String,
      entity: Acl.Entity,
      options: List[BucketSourceOption]
  ): ZIO[Storage, Throwable, Boolean] =
    ZIO.environmentWithZIO(_.get.deleteAcl(bucket, entity, options))

  def deleteDefaultAcl(
      bucket: String,
      entity: Acl.Entity
  ): ZIO[Storage, Throwable, Boolean] =
    ZIO.environmentWithZIO(_.get.deleteDefaultAcl(bucket, entity))

  def deleteHmacKey(
      hmacKeyMetaData: com.google.cloud.storage.HmacKey.HmacKeyMetadata,
      options: List[DeleteHmacKeyOption]
  ): ZIO[Storage, Throwable, Unit] =
    ZIO.environmentWithZIO(_.get.deleteHmacKey(hmacKeyMetaData, options))

  def get(blobIds: List[BlobId]): ZIO[Storage, Throwable, List[Blob]] =
    ZIO.environmentWithZIO(_.get.get(blobIds))

  def get(blobId: BlobId): ZIO[Storage, Throwable, Option[Blob]] =
    ZIO.environmentWithZIO(_.get.get(blobId, List.empty))

  def get(
      blobId: BlobId,
      options: List[BlobGetOption]
  ): ZIO[Storage, Throwable, Option[Blob]] =
    ZIO.environmentWithZIO(_.get.get(blobId, options))

  def get(blobIds: Iterable[BlobId]): ZIO[Storage, Throwable, List[Blob]] =
    ZIO.environmentWithZIO(_.get.get(blobIds))

  def getAcl(
      blob: BlobId,
      entity: Acl.Entity
  ): ZIO[Storage, Throwable, Option[Acl]] =
    ZIO.environmentWithZIO(_.get.getAcl(blob, entity))

  def getAcl(
      bucket: String,
      entity: Acl.Entity
  ): ZIO[Storage, Throwable, Option[Acl]] =
    ZIO.environmentWithZIO(_.get.getAcl(bucket, entity, List.empty))

  def getAcl(
      bucket: String,
      entity: Acl.Entity,
      options: List[BucketSourceOption]
  ): ZIO[Storage, Throwable, Option[Acl]] =
    ZIO.environmentWithZIO(_.get.getAcl(bucket, entity, options))

  def getDefaultAcl(
      bucket: String,
      entity: Acl.Entity
  ): ZIO[Storage, Throwable, Option[Acl]] =
    ZIO.environmentWithZIO(_.get.getDefaultAcl(bucket, entity))

  def getHmacKey(
      accessId: String,
      options: List[GetHmacKeyOption]
  ): ZIO[Storage, Throwable, HmacKey.HmacKeyMetadata] =
    ZIO.environmentWithZIO(_.get.getHmacKey(accessId, options))

  def getIamPolicy(
      bucket: String,
      options: List[BucketSourceOption]
  ): ZIO[Storage, Throwable, com.google.cloud.Policy] =
    ZIO.environmentWithZIO(_.get.getIamPolicy(bucket, options))

  def getServiceAccount(
      projectId: String
  ): ZIO[Storage, Throwable, ServiceAccount] =
    ZIO.environmentWithZIO(_.get.getServiceAccount(projectId))

  def list(
      options: List[BucketListOption]
  ): ZIO[Storage, Throwable, Page[Bucket]] =
    ZIO.environmentWithZIO(_.get.list(options))

  def list(
      bucket: String,
      options: List[BlobListOption]
  ): ZIO[Storage, Throwable, Page[Blob]] =
    ZIO.environmentWithZIO(_.get.list(bucket, options))

  def listAcls(blob: BlobId): ZIO[Storage, Throwable, List[Acl]] =
    ZIO.environmentWithZIO(_.get.listAcls(blob))

  def listAcls(bucket: String): ZIO[Storage, Throwable, List[Acl]] =
    ZIO.environmentWithZIO(_.get.listAcls(bucket, List.empty))

  def listAcls(
      bucket: String,
      options: List[BucketSourceOption]
  ): ZIO[Storage, Throwable, List[Acl]] =
    ZIO.environmentWithZIO(_.get.listAcls(bucket, options))

  def listDefaultAcls(bucket: String): ZIO[Storage, Throwable, List[Acl]] =
    ZIO.environmentWithZIO(_.get.listDefaultAcls(bucket))

  def listHmacKeys(
      options: List[com.google.cloud.storage.Storage.ListHmacKeysOption]
  ): ZIO[Storage, Throwable, com.google.api.gax.paging.Page[
    com.google.cloud.storage.HmacKey.HmacKeyMetadata
  ]] =
    ZIO.environmentWithZIO(_.get.listHmacKeys(options))

  def lockRetentionPolicy(
      bucketInfo: BucketInfo,
      options: List[BucketTargetOption]
  ): ZIO[Storage, Throwable, Bucket] =
    ZIO.environmentWithZIO(_.get.lockRetentionPolicy(bucketInfo, options))

  def readAllBytes(
      blob: BlobId,
      options: List[BlobSourceOption]
  ): ZIO[Storage, Throwable, Array[Byte]] =
    ZIO.environmentWithZIO(_.get.readAllBytes(blob, options))

  def readAllBytes(
      bucket: String,
      blob: String,
      options: List[BlobSourceOption]
  ): ZIO[Storage, Throwable, Array[Byte]] =
    ZIO.environmentWithZIO(_.get.readAllBytes(bucket, blob, options))

  def reader(
      blobId: BlobId,
      options: List[BlobSourceOption]
  ): ZIO[Storage, Throwable, com.google.cloud.ReadChannel] =
    ZIO.environmentWithZIO(_.get.reader(blobId, options))

  def reader(
      bucket: String,
      blob: String,
      options: List[BlobSourceOption]
  ): ZIO[Storage, Throwable, com.google.cloud.ReadChannel] =
    ZIO.environmentWithZIO(_.get.reader(bucket, blob, options))

  def setIamPolicy(
      bucket: String,
      policy: com.google.cloud.Policy,
      options: List[BucketSourceOption]
  ): ZIO[Storage, Throwable, com.google.cloud.Policy] =
    ZIO.environmentWithZIO(_.get.setIamPolicy(bucket, policy, options))

  def signUrl(
      blobInfo: BlobInfo,
      duration: Long,
      unit: TimeUnit,
      options: List[SignUrlOption]
  ): ZIO[Storage, Throwable, URL] =
    ZIO.environmentWithZIO(_.get.signUrl(blobInfo, duration, unit, options))

  def testIamPermissions(
      bucket: String,
      permissions: List[String],
      options: List[BucketSourceOption]
  ): ZIO[Storage, Throwable, List[Boolean]] =
    ZIO.environmentWithZIO(
      _.get.testIamPermissions(bucket, permissions, options)
    )

  def update(blobInfos: List[BlobInfo]): ZIO[Storage, Throwable, List[Blob]] =
    ZIO.environmentWithZIO(_.get.update(blobInfos))

  def update(blobInfo: BlobInfo): ZIO[Storage, Throwable, Blob] =
    ZIO.environmentWithZIO(_.get.update(blobInfo, List.empty))

  def update(
      blobInfo: BlobInfo,
      options: List[BlobTargetOption]
  ): ZIO[Storage, Throwable, Blob] =
    ZIO.environmentWithZIO(_.get.update(blobInfo, options))

  def update(
      bucketInfo: com.google.cloud.storage.BucketInfo,
      options: List[BucketTargetOption]
  ): ZIO[Storage, Throwable, Bucket] =
    ZIO.environmentWithZIO(_.get.update(bucketInfo, options))

  def updateAcl(blobId: BlobId, acl: Acl): ZIO[Storage, Throwable, Acl] =
    ZIO.environmentWithZIO(_.get.updateAcl(blobId, acl))

  def updateAcl(bucket: String, acl: Acl): ZIO[Storage, Throwable, Acl] =
    ZIO.environmentWithZIO(_.get.updateAcl(bucket, acl, List.empty))

  def updateAcl(
      bucket: String,
      acl: Acl,
      options: List[BucketSourceOption]
  ): ZIO[Storage, Throwable, Acl] =
    ZIO.environmentWithZIO(_.get.updateAcl(bucket, acl, options))

  def updateDefaultAcl(bucket: String, acl: Acl): ZIO[Storage, Throwable, Acl] =
    ZIO.environmentWithZIO(_.get.updateDefaultAcl(bucket, acl))

  def updateHmacKeyState(
      hmacKeyMetaData: com.google.cloud.storage.HmacKey.HmacKeyMetadata,
      state: HmacKeyState,
      options: List[UpdateHmacKeyOption]
  ): ZIO[Storage, Throwable, com.google.cloud.storage.HmacKey.HmacKeyMetadata] =
    ZIO.environmentWithZIO(
      _.get.updateHmacKeyState(hmacKeyMetaData, state, options)
    )

  def writer(
      blobInfo: BlobInfo,
      options: List[BlobWriteOption]
  ): ZIO[Storage, Throwable, com.google.cloud.WriteChannel] =
    ZIO.environmentWithZIO(_.get.writer(blobInfo, options))

  def writer(
      signedURL: URL
  ): ZIO[Storage, Throwable, com.google.cloud.WriteChannel] =
    ZIO.environmentWithZIO(_.get.writer(signedURL))
}
