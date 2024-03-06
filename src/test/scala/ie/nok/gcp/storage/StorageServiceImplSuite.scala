package ie.nok.gcp.storage

import ie.nok.env.Environment
import java.io.File
import java.time.Instant
import scala.io.Source
import scala.util.chaining.scalaUtilChainingOps

class StorageServiceImplSuite extends munit.FunSuite {
  val instance = StorageServiceImpl.default.get

  val environment = Environment.default
  val bucket      = StorageConvention.bucketName

  test("StorageServiceImpl write and read file to Google Cloud Storage") {
    val blobName = "/scala-libraries/test/blob.txt"
    val uploaded = getClass()
      .getResource("/gcp/storage/blob.txt")
      .toURI()
      .pipe { File(_) }

    instance
      .write(bucket, blobName, uploaded)
      .getOrElse { fail("Failed to write to Google Storage") }

    val downloaded = instance
      .read(bucket, blobName)
      .getOrElse { fail("Failed to read from Google Storage") }

    assertEquals(
      Source.fromFile(uploaded).mkString,
      Source.fromFile(downloaded).mkString
    )
  }

  test("StorageServiceImpl list files written to Google Cloud Storage") {
    val instant = Instant.now()

    val blobName = StorageConvention.blobNameVersioned("/scala-libraries/test", instant, "txt")
    val uploaded = getClass()
      .getResource("/gcp/storage/blob.txt")
      .toURI()
      .pipe { File(_) }

    instance
      .write(bucket, blobName, uploaded)
      .getOrElse { fail("Failed to write to Google Storage") }

    val blobNameGlobPattern = StorageConvention.blobNameVersionedGlobPatternForDay("/scala-libraries/test", instant, "txt")
    val blobNames = instance
      .listBlobNames(bucket, blobNameGlobPattern)
      .getOrElse { fail("Failed to list from Google Storage") }
      .toList

    assert(blobNames.contains(blobName))
  }
}
