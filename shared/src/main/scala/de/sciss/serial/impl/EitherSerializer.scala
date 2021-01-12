/*
 *  EitherSerializer.scala
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
package impl

final class EitherTFormat[-T, A, B](peer1: TFormat[T, A],
                                    peer2: TFormat[T, B])
  extends TFormat[T, Either[A, B]] {

  def write(either: Either[A, B], out: DataOutput): Unit =
    either match {
      case Left (a) => out.writeByte(0); peer1.write(a, out)
      case Right(b) => out.writeByte(1); peer2.write(b, out)
    }

  def readT(in: DataInput)(implicit tx: T): Either[A, B] =
    in.readByte() match {
      case 0 => Left (peer1.readT(in))
      case 1 => Right(peer2.readT(in))
    }
}

final class ConstEitherFormat[A, B](peer1: ConstFormat[A],
                                    peer2: ConstFormat[B])
  extends ConstFormat[Either[A, B]] {

  def write(either: Either[A, B], out: DataOutput): Unit =
    either match {
      case Left (a) => out.writeByte(0); peer1.write(a, out)
      case Right(b) => out.writeByte(1); peer2.write(b, out)
    }

  def read(in: DataInput): Either[A, B] =
    in.readByte() match {
      case 0 => Left (peer1.read(in))
      case 1 => Right(peer2.read(in))
    }
}
