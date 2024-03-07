package ie.nok.stores.google.storage

import ie.nok.env.Environment
import java.io.File
import java.time.Instant
import scala.io.Source
import scala.util.chaining.scalaUtilChainingOps

class StorageConventionSuite extends munit.FunSuite {

  test("StorageConvention.blobNameLatest should not start with a slash") {
    StorageConvention
      .blobNameLatest("abc", "def")
      .tap { blobName => assert(!blobName.startsWith("/")) }

    StorageConvention
      .blobNameLatest("/abc", "def")
      .tap { blobName => assert(!blobName.startsWith("/")) }
  }

  test("StorageConvention.blobNameLatest should not contain a double slash") {
    StorageConvention
      .blobNameLatest("abc", "def")
      .tap { blobName => assert(!blobName.contains("//")) }

    StorageConvention
      .blobNameLatest("abc/", "def")
      .tap { blobName => assert(!blobName.contains("//")) }
  }

  test("StorageConvention.blobNameLatest should not contain a double dot") {
    StorageConvention
      .blobNameLatest("abc", "def")
      .tap { blobName => assert(!blobName.contains("..")) }

    StorageConvention
      .blobNameLatest("abc", ".def")
      .tap { blobName => assert(!blobName.contains("..")) }
  }

  test("StorageConvention.blobNameVersioned should not start with a slash") {
    StorageConvention
      .blobNameVersioned("abc", Instant.now, "def")
      .tap { blobName => assert(!blobName.startsWith("/")) }

    StorageConvention
      .blobNameVersioned("/abc", Instant.now, "def")
      .tap { blobName => assert(!blobName.startsWith("/")) }
  }

  test("StorageConvention.blobNameVersioned should not contain a double slash") {
    StorageConvention
      .blobNameVersioned("abc", Instant.now, "def")
      .tap { blobName => assert(!blobName.contains("//")) }

    StorageConvention
      .blobNameVersioned("abc/", Instant.now, "def")
      .tap { blobName => assert(!blobName.contains("//")) }
  }

  test("StorageConvention.blobNameVersioned should not contain a double dot") {
    StorageConvention
      .blobNameVersioned("abc", Instant.now, "def")
      .tap { blobName => assert(!blobName.contains("..")) }

    StorageConvention
      .blobNameVersioned("abc", Instant.now, ".def")
      .tap { blobName => assert(!blobName.contains("..")) }
  }

  test("StorageConvention.blobNameVersionedGlobPatternForDay should not start with a slash") {
    StorageConvention
      .blobNameVersionedGlobPatternForDay("abc", Instant.now, "def")
      .tap { blobName => assert(!blobName.startsWith("/")) }

    StorageConvention
      .blobNameVersionedGlobPatternForDay("/abc", Instant.now, "def")
      .tap { blobName => assert(!blobName.startsWith("/")) }
  }

  test("StorageConvention.blobNameVersionedGlobPatternForDay should not contain a double slash") {
    StorageConvention
      .blobNameVersionedGlobPatternForDay("abc", Instant.now, "def")
      .tap { blobName => assert(!blobName.contains("//")) }

    StorageConvention
      .blobNameVersionedGlobPatternForDay("abc/", Instant.now, "def")
      .tap { blobName => assert(!blobName.contains("//")) }
  }

  test("StorageConvention.blobNameVersionedGlobPatternForDay should not contain a double dot") {
    StorageConvention
      .blobNameVersionedGlobPatternForDay("abc", Instant.now, "def")
      .tap { blobName => assert(!blobName.contains("..")) }

    StorageConvention
      .blobNameVersionedGlobPatternForDay("abc", Instant.now, ".def")
      .tap { blobName => assert(!blobName.contains("..")) }
  }
}
