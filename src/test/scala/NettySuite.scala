package ie.nok

import ie.nok.zio.ZIO
import scala.util.chaining.scalaUtilChainingOps
import _root_.zio.http.{Client, ClientConfig}

class NettySuite extends munit.FunSuite {
  test("Netty works") {
    Client.request("http://example.com/")
    .provide(
        ClientConfig.default,
        Client.fromConfig,
    )
    .pipe(ZIO.unsafeRun).toEither.pipe {
      case Left(a) => fail("Netty failed", a)
      case Right(_) => true
    }
  }
}
