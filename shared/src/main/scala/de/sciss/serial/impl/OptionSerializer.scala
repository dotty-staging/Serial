/*
 *  OptionSerializer.scala
 *  (Serial)
 *
 * Copyright (c) 2011-2020 Hanns Holger Rutz. All rights reserved.
 *
 * This software is published under the GNU Lesser General Public License v2.1+
 *
 *
 * For further information, please contact Hanns Holger Rutz at
 * contact@sciss.de
 */

package de.sciss.serial
package impl

final class OptionTFormat[-T, A](peer: TFormat[T, A])
  extends TFormat[T, Option[A]] {

  def write(opt: Option[A], out: DataOutput): Unit =
    opt match {
      case Some(v)  => out.writeByte(1); peer.write(v, out)
      case _        => out.writeByte(0)
    }

  def readT(in: DataInput)(implicit tx: T): Option[A] =
    in.readByte() match {
      case 1 => Some(peer.readT(in))
      case 0 => None
    }
}

final class ConstOptionFormat[A](peer: ConstFormat[A])
  extends ConstFormat[Option[A]] {

  def write(opt: Option[A], out: DataOutput): Unit =
    opt match {
      case Some(v)  => out.writeByte(1); peer.write(v, out)
      case _        => out.writeByte(0)
    }

  def read(in: DataInput): Option[A] =
    in.readByte() match {
      case 1 => Some(peer.read(in))
      case 0 => None
    }
}
