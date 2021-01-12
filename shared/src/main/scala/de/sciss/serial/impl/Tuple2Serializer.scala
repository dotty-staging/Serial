/*
 *  Tuple2Serializer.scala
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

final class Tuple2TFormat[-T, A1, A2](peer1: TFormat[T, A1],
                                      peer2: TFormat[T, A2])
  extends TFormat[T, (A1, A2)] {

  def write(tup: (A1, A2), out: DataOutput): Unit = {
    peer1.write(tup._1, out)
    peer2.write(tup._2, out)
  }

  def readT(in: DataInput)(implicit tx: T): (A1, A2) = {
    val a1 = peer1.readT(in)
    val a2 = peer2.readT(in)
    (a1, a2)
  }
}

final class ConstTuple2Format[A1, A2](peer1: ConstFormat[A1],
                                      peer2: ConstFormat[A2])
  extends ConstFormat[(A1, A2)] {

  def write(tup: (A1, A2), out: DataOutput): Unit = {
    peer1.write(tup._1, out)
    peer2.write(tup._2, out)
  }

  def read(in: DataInput): (A1, A2) = {
    val a1 = peer1.read(in)
    val a2 = peer2.read(in)
    (a1, a2)
  }
}