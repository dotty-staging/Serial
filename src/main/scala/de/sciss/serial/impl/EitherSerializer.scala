/*
 *  EitherSerializer.scala
 *  (Serial)
 *
 * Copyright (c) 2011-2018 Hanns Holger Rutz. All rights reserved.
 *
 * This software is published under the GNU Lesser General Public License v2.1+
 *
 *
 * For further information, please contact Hanns Holger Rutz at
 * contact@sciss.de
 */

package de.sciss.serial
package impl

import scala.annotation.switch

abstract class AbstractEitherSerializer[Tx, Acc, A, B]
  extends Serializer[Tx, Acc, Either[A, B]] {

  protected def peer1: Serializer[Tx, Acc, A]
  protected def peer2: Serializer[Tx, Acc, B]

  final def write(either: Either[A, B], out: DataOutput): Unit =
    either match {
      case Left (a) => out.writeByte(0); peer1.write(a, out)
      case Right(b) => out.writeByte(1); peer2.write(b, out)
    }

  final def read(in: DataInput, acc: Acc)(implicit tx: Tx): Either[A, B] = (in.readByte(): @switch) match {
    case 0 => Left (peer1.read(in, acc))
    case 1 => Right(peer2.read(in, acc))
  }
}

final class EitherSerializer[Tx, Acc, A, B](protected val peer1: Serializer[Tx, Acc, A],
                                            protected val peer2: Serializer[Tx, Acc, B])
  extends AbstractEitherSerializer[Tx, Acc, A, B]

final class ImmutableEitherSerializer[A, B](protected val peer1: Serializer.Immutable[A],
                                            protected val peer2: Serializer.Immutable[B])
  extends AbstractEitherSerializer[Any, Any, A, B] with ImmutableSerializer[Either[A, B]] {

  def read(in: DataInput): Either[A, B] = read(in, ())(())
}
