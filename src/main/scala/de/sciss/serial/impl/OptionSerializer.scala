/*
 *  OptionSerializer.scala
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

abstract class AbstractOptionSerializer[Tx, Acc, A]
  extends Serializer[Tx, Acc, Option[A]] {

  protected def peer: Serializer[Tx, Acc, A]

  final def write(opt: Option[A], out: DataOutput): Unit =
    opt match {
      case Some(v)  => out.writeByte(1); peer.write(v, out)
      case _        => out.writeByte(0)
    }

  final def read(in: DataInput, acc: Acc)(implicit tx: Tx): Option[A] = (in.readByte(): @switch) match {
    case 1 => Some(peer.read(in, acc))
    case 0 => None
  }
}

final class OptionSerializer[Tx, Acc, A](protected val peer: Serializer[Tx, Acc, A])
  extends AbstractOptionSerializer[Tx, Acc, A]

final class ImmutableOptionSerializer[A](protected val peer: Serializer.Immutable[A])
  extends AbstractOptionSerializer[Any, Any, A] with ImmutableSerializer[Option[A]] {

  def read(in: DataInput): Option[A] = read(in, ())(())
}
