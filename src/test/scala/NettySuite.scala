import _root_.zio.http.{Client, ClientConfig}
import ie.nok.zio.ZIOOps

import scala.util.chaining.scalaUtilChainingOps

class NettySuite extends munit.FunSuite {
  test("Netty works") {
    Client
      .request("http://example.com/")
      .provide(
        ClientConfig.default,
        Client.fromConfig
      )
      .pipe(ZIOOps.unsafeRun)
  }
}
