package ie.nok.gcp.safe

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.{Storage, StorageOptions}

import java.io.ByteArrayInputStream
import scala.util.Try
import scala.util.chaining.*

object GcpStorage {

  def googleCloudStorage(googleCredentials: GoogleCredentials): Try[Storage] =
    Try {
      StorageOptions.getDefaultInstance
        .toBuilder()
        .setCredentials(googleCredentials)
        .build()
        .getService
    }
}
