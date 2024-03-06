package ie.nok.gcp.storage

import ie.nok.env.Environment
import java.time.{Instant, LocalDate, ZoneOffset}
import java.time.format.DateTimeFormatter
import scala.util.chaining.scalaUtilChainingOps

object StorageConvention {

  def bucket(env: Environment): String =
    env.pipe {
      case Environment.Production => "nok-ie"
      case Environment.Other      => "nok-ie-dev"
    }

  private def blobName(prefix: String, name: String, extension: String): String =
    s"${prefix.stripPrefix("/").stripSuffix("/")}/${name.stripPrefix("/").stripSuffix(".")}.${extension.stripPrefix(".")}".trim()

  def blobNameLatest(prefix: String, extension: String): String =
    blobName(prefix, "latest", extension)

  def blobNameVersioned(prefix: String, instant: Instant, extension: String): String =
    DateTimeFormatter
      .ofPattern("yyyyMMddHHmmss")
      .withZone(ZoneOffset.UTC)
      .format(instant)
      .pipe { version => blobName(prefix, version, extension) }

  def blobNameVersionedGlobPatternForDay(prefix: String, instant: Instant, extension: String): String =
    DateTimeFormatter
      .ofPattern("yyyyMMdd")
      .withZone(ZoneOffset.UTC)
      .format(instant)
      .pipe { versionPrefix => blobName(prefix, s"$versionPrefix??????", extension) }
}
