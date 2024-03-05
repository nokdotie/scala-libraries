package ie.nok.zio

import zio.{Exit, Runtime, Unsafe, ZIO}

object ZIOOps {
  def unsafeRun[E <: Throwable, A](a: ZIO[Any, E, A]): Unit =
    Unsafe
      .unsafe { implicit unsafe =>
        Runtime.default.unsafe.run(a)
      }
      .getOrElse { cause =>
        throw cause.squash
      }
}
