package ie.nok.file

import ie.nok.zio.ZIOOps
import zio.json.JsonCodec
import zio.stream.ZStream
import scala.util.chaining.scalaUtilChainingOps

class ZFileImplSuite extends munit.FunSuite {

  case class Line(foo: String, bar: Int, baz: Boolean)
  given JsonCodec[Line] = JsonCodec.derived[Line]

  test("ZFileServiceImpl write and read file") {
    val written = List(
      Line("foo", 1, true),
      Line("bar", 2, false),
      Line("baz", 3, true)
    )

    val stream = ZStream.fromIterable(written)

    ZFileService
      .write[Any, Line](stream)
      .flatMap { file => ZFileService.read[Line](file).runCollect }
      .map { _.toList }
      .map { read => assert(written == read) }
      .provide(ZFileServiceImpl.layer[Line])
      .pipe { ZIOOps.unsafeRun }
  }
}
