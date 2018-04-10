/*
 *  Tuple3Serializer.scala
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

abstract class AbstractTuple3Serializer[Tx, Acc, A1, A2, A3]
  extends Serializer[Tx, Acc, (A1, A2, A3)] {

  protected val peer1: Serializer[Tx, Acc, A1]
  protected val peer2: Serializer[Tx, Acc, A2]
  protected val peer3: Serializer[Tx, Acc, A3]

  final def write(tup: (A1, A2, A3), out: DataOutput): Unit = {
    peer1.write(tup._1, out)
    peer2.write(tup._2, out)
    peer3.write(tup._3, out)
  }

  final def read(in: DataInput, acc: Acc)(implicit tx: Tx): (A1, A2, A3) = {
    val a1 = peer1.read(in, acc)
    val a2 = peer2.read(in, acc)
    val a3 = peer3.read(in, acc)
    (a1, a2, a3)
  }
}

final class Tuple3Serializer[Tx, Acc, A1, A2, A3](protected val peer1: Serializer[Tx, Acc, A1],
                                                  protected val peer2: Serializer[Tx, Acc, A2],
                                                  protected val peer3: Serializer[Tx, Acc, A3])
  extends AbstractTuple3Serializer[Tx, Acc, A1, A2, A3]

final class ImmutableTuple3Serializer[A1, A2, A3](protected val peer1: Serializer.Immutable[A1],
                                                  protected val peer2: Serializer.Immutable[A2],
                                                  protected val peer3: Serializer.Immutable[A3])
  extends AbstractTuple3Serializer[Any, Any, A1, A2, A3] with ImmutableSerializer[(A1, A2, A3)] {

  def read(in: DataInput): (A1, A2, A3) = read(in, ())(())
}
