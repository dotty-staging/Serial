package de.sciss.serial

import scala.collection.immutable.{IndexedSeq => Vec}

trait ImplicitResolution {
  // issue #2
  implicitly[de.sciss.serial.Serializer[Any, Any, Vec[Int]]]

  implicitly[Serializer.Immutable[Vec[Int]]]

  trait Foo extends Writable

  {
    import Implicits._

    implicitly[Writer[Foo]]
  }

  object Bar {
    implicit def serializer: ImmutableSerializer[Bar] = ???
  }
  trait Bar extends Writable
  implicitly[Writer[Bar]]

  implicitly[Writer[Int]]
}
