package ie.nok.gcp.safe

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.{Storage, StorageOptions}

import java.io.ByteArrayInputStream
import scala.util.Try
import scala.util.chaining.*

object GcpCredentials {

  def fromResource(resource: String): Try[GoogleCredentials] = Try {
    getClass
      .getResourceAsStream(resource)
      .pipe { GoogleCredentials.fromStream }
  }

  val googleCredentialsFromEnv: Try[GoogleCredentials] = Try {
    sys
      .env("GCP_CREDENTIALS")
      .getBytes
      .pipe { ByteArrayInputStream(_) }
      .pipe { GoogleCredentials.fromStream }
  }

  val defaultGoogleCredentials: Try[GoogleCredentials] = Try {
    GoogleCredentials.getApplicationDefault()
  }

  val googleCredentials: Try[GoogleCredentials] = googleCredentialsFromEnv.orElse(defaultGoogleCredentials)
}
