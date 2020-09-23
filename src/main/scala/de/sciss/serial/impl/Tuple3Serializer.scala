/*
 *  Tuple3Serializer.scala
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

final class Tuple3TFormat[T, A1, A2, A3](peer1: TFormat[T, A1],
                                         peer2: TFormat[T, A2],
                                         peer3: TFormat[T, A3])
  extends TFormat[T, (A1, A2, A3)] {

  def write(tup: (A1, A2, A3), out: DataOutput): Unit = {
    peer1.write(tup._1, out)
    peer2.write(tup._2, out)
    peer3.write(tup._3, out)
  }

  def readT(in: DataInput)(implicit tx: T): (A1, A2, A3) = {
    val a1 = peer1.readT(in)
    val a2 = peer2.readT(in)
    val a3 = peer3.readT(in)
    (a1, a2, a3)
  }
}

final class ConstTuple3Format[A1, A2, A3](peer1: ConstFormat[A1],
                                          peer2: ConstFormat[A2],
                                          peer3: ConstFormat[A3])
  extends ConstFormat[(A1, A2, A3)] {

  def write(tup: (A1, A2, A3), out: DataOutput): Unit = {
    peer1.write(tup._1, out)
    peer2.write(tup._2, out)
    peer3.write(tup._3, out)
  }

  def read(in: DataInput): (A1, A2, A3) = {
    val a1 = peer1.read(in)
    val a2 = peer2.read(in)
    val a3 = peer3.read(in)
    (a1, a2, a3)
  }
}
