package de.sciss.serial

import scala.collection.immutable.{IndexedSeq => Vec}

trait ImplicitResolution {
  // issue #2
  implicitly[TFormat[Any, Vec[Int]]]

  implicitly[TFormat.Constant[Vec[Int]]]

  trait Foo extends Writable

  {
    import Implicits._

    implicitly[Writer[Foo]]
  }

  object Bar {
    implicit def serializer: ConstFormat[Bar] = ???
  }
  trait Bar extends Writable
  implicitly[Writer[Bar]]

  implicitly[Writer[Int]]
}
