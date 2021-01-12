/*
 *  Serializer.scala
 *  (Serial)
 *
 * Copyright (c) 2011-2021 Hanns Holger Rutz. All rights reserved.
 *
 * This software is published under the GNU Lesser General Public License v2.1+
 *
 *
 * For further information, please contact Hanns Holger Rutz at
 * contact@sciss.de
 */

package de.sciss.serial

import de.sciss.serial.impl._

import scala.collection.immutable.{IndexedSeq => Vec}

object TFormat {
  type Constant[A] = TFormat[Any, A]

  // ---- primitives ----

  implicit final object Unit extends ConstFormat[scala.Unit] {
    def write(v: scala.Unit, out: DataOutput): Unit = ()

    def read(in: DataInput): Unit = ()
  }

  implicit final object Boolean extends ConstFormat[scala.Boolean] {
    def write(v: scala.Boolean, out: DataOutput): Unit = out.writeBoolean(v)

    def read(in: DataInput): scala.Boolean = in.readBoolean()
  }

  implicit final object Char extends ConstFormat[scala.Char] {
    def write(v: scala.Char, out: DataOutput): Unit = out.writeChar(v.toInt)

    def read(in: DataInput): scala.Char = in.readChar()
  }

  implicit final object Int extends ConstFormat[scala.Int] {
    def write(v: scala.Int, out: DataOutput): Unit = out.writeInt(v)

    def read(in: DataInput): scala.Int = in.readInt()
  }

  implicit final object Float extends ConstFormat[scala.Float] {
    def write(v: scala.Float, out: DataOutput): Unit = out.writeFloat(v)

    def read(in: DataInput): scala.Float = in.readFloat()
  }

  implicit final object Long extends ConstFormat[scala.Long] {
    def write(v: scala.Long, out: DataOutput): Unit = out.writeLong(v)

    def read(in: DataInput): scala.Long = in.readLong()
  }

  implicit final object Double extends ConstFormat[scala.Double] {
    def write(v: scala.Double, out: DataOutput): Unit = out.writeDouble(v)

    def read(in: DataInput): scala.Double = in.readDouble()
  }

  implicit final object String extends ConstFormat[java.lang.String] {
    def write(v: java.lang.String, out: DataOutput): Unit = out.writeUTF(v)

    def read(in: DataInput): java.lang.String = in.readUTF()
  }

  implicit final object File extends ConstFormat[java.io.File] {
    def write(v: java.io.File, out: DataOutput): Unit = out.writeUTF(v.getPath)

    def read(in: DataInput): java.io.File = new java.io.File(in.readUTF())
  }

  // ---- higher-kinded ----

  implicit def option[T, A](implicit peer: TFormat[T, A]): TFormat[T, Option[A]] =
    new OptionTFormat[T, A](peer)

  implicit def either[T, A, B](implicit peer1: TFormat[T, A],
                               peer2: TFormat[T, B]): TFormat[T, Either[A, B]] =
    new EitherTFormat[T, A, B](peer1, peer2)

  implicit def tuple2[T, A1, A2](implicit peer1: TFormat[T, A1],
                                 peer2: TFormat[T, A2]): TFormat[T, (A1, A2) ] =
    new Tuple2TFormat[T, A1, A2](peer1, peer2)

  implicit def tuple3[T, A1, A2, A3](implicit peer1: TFormat[T, A1],
                                     peer2: TFormat[T, A2],
                                     peer3: TFormat[T, A3]): TFormat[T, (A1, A2, A3)] =
    new Tuple3TFormat[T, A1, A2, A3](peer1, peer2, peer3)

  implicit def list[T, A](implicit peer: TFormat[T, A]): TFormat[T, List[A]] =
    new ListTFormat[T, A](peer)

  implicit def set[T, A](implicit peer: TFormat[T, A]): TFormat[T, Set[A]] =
    new SetTFormat[T, A](peer)

  implicit def vec[T, A](implicit peer: TFormat[T, A]): TFormat[T, Vec[A]] =
    new  VecTFormat[T, A](peer)

  implicit def map[T, A, B](implicit key  : TFormat[T, A],
                            value: TFormat[T, B]): TFormat[T, Map[A, B]] =
    new MapTFormat[T, A, B](key, value)
}

trait Format[A] extends Reader[A] with Writer[A]

trait TFormat[-T, A] extends TReader[T, A] with Writer[A]

object ConstFormat {
  // ---- higher-kinded ----

  implicit def option[A](implicit peer: ConstFormat[A]): ConstFormat[Option[A]] =
    new ConstOptionFormat[A](peer)

  implicit def either[A, B](implicit peer1: ConstFormat[A],
                            peer2: ConstFormat[B]): ConstFormat[Either[A, B]] =
    new ConstEitherFormat[A, B](peer1, peer2)

  implicit def tuple2[A1, A2](implicit peer1: ConstFormat[A1],
                              peer2: ConstFormat[A2]): ConstFormat[(A1, A2)] =
    new ConstTuple2Format[A1, A2](peer1, peer2)

  implicit def tuple3[A1, A2, A3](implicit peer1: ConstFormat[A1],
                                  peer2: ConstFormat[A2],
                                  peer3: ConstFormat[A3]): ConstFormat[(A1, A2, A3)] =
    new ConstTuple3Format[A1, A2, A3](peer1, peer2, peer3)

  implicit def list[A](implicit peer: ConstFormat[A]): ConstFormat[List[A]] =
    new ConstListFormat[A](peer)

  implicit def set[A](implicit peer: ConstFormat[A]): ConstFormat[Set[A]] =
    new ConstSetFormat[A](peer)

  implicit def vec[A](implicit peer: ConstFormat[A]): ConstFormat[Vec[A]] =
    new ConstVecFormat[A](peer)

  implicit def map[A, B](implicit key: ConstFormat[A],
                         value: ConstFormat[B]): ConstFormat[Map[A, B]] =
    new ConstMapFormat[A, B](key, value)
}
trait ConstFormat[A] extends TFormat[Any, A] with Format[A] with ConstReader[A]

trait WritableFormat[-T, A <: Writable] extends TFormat[T, A] {
  final def write(value: A, out: DataOutput): Unit = value.write(out)
}
