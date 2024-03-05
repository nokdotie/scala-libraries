package ie.nok.gcp

import com.google.auth.oauth2.GoogleCredentials
import java.io.ByteArrayInputStream
import scala.util.Try
import scala.util.chaining.*

object Credentials {

  def fromResource(resource: String): Try[GoogleCredentials] = Try {
    getClass
      .getResourceAsStream(resource)
      .pipe { GoogleCredentials.fromStream }
  }

  def fromEnvironmentVariable(name: String): Try[GoogleCredentials] = Try {
    sys
      .env(name)
      .getBytes
      .pipe { ByteArrayInputStream(_) }
      .pipe { GoogleCredentials.fromStream }
  }

  lazy val default: Try[GoogleCredentials] =
    fromEnvironmentVariable("GCP_CREDENTIALS")
      .orElse { Try { GoogleCredentials.getApplicationDefault() } }

}
