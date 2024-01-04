package ie.nok.zio

import zio.{Exit, Runtime, Unsafe, ZIO}

object ZIOOps {
  def unsafeRun[E, A](a: ZIO[Any, E, A]): Exit[E, A] =
    Unsafe.unsafe { implicit unsafe =>
      Runtime.default.unsafe.run(a)
    }
}
