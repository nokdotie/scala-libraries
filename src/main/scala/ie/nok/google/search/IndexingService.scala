package ie.nok.google.search

import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.indexing.v3.{Indexing, IndexingScopes}
import com.google.api.services.indexing.v3.model.UrlNotification
import com.google.auth.oauth2.GoogleCredentials
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.auth.http.HttpCredentialsAdapter
import ie.nok.google.Credentials
import scala.util.chaining.scalaUtilChainingOps
import scala.util.Try

trait IndexingService {
  def update(url: String): Try[Unit]
  def delete(url: String): Try[Unit]
}

class IndexingServiceImpl(indexing: Indexing) extends IndexingService {

  def update(url: String): Try[Unit] = UrlNotification().setUrl(url).setType("URL_UPDATED").pipe(request)
  def delete(url: String): Try[Unit] = UrlNotification().setUrl(url).setType("URL_DELETED").pipe(request)

  private def request(urlNotification: UrlNotification): Try[Unit] = Try {
    indexing
      .urlNotifications()
      .publish(urlNotification)
      .execute()
  }
}

object IndexingServiceImpl {

  private def fromCredentials(credentials: GoogleCredentials): IndexingService = {
    val transport              = GoogleNetHttpTransport.newTrustedTransport();
    val jsonFactory            = JacksonFactory()
    val httpRequestInitializer = HttpCredentialsAdapter(credentials);
    val indexing               = Indexing(transport, jsonFactory, httpRequestInitializer)

    IndexingServiceImpl(indexing)
  }

  lazy val default: Try[IndexingService] = Credentials
    .fromResource("/gcp/google-search-console.json")
    .map { _.createScoped(IndexingScopes.INDEXING) }
    .map { fromCredentials }

}
