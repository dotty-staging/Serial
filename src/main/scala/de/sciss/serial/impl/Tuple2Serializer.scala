/*
 *  Tuple2Serializer.scala
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

abstract class AbstractTuple2Serializer[Tx, Acc, A1, A2]
  extends Serializer[Tx, Acc, (A1, A2)] {

  protected def peer1: Serializer[Tx, Acc, A1]
  protected def peer2: Serializer[Tx, Acc, A2]

  final def write(tup: (A1, A2), out: DataOutput): Unit = {
    peer1.write(tup._1, out)
    peer2.write(tup._2, out)
  }

  final def read(in: DataInput, acc: Acc)(implicit tx: Tx): (A1, A2) = {
    val a1 = peer1.read(in, acc)
    val a2 = peer2.read(in, acc)
    (a1, a2)
  }
}

final class Tuple2Serializer[Tx, Acc, A1, A2](protected val peer1: Serializer[Tx, Acc, A1],
                                              protected val peer2: Serializer[Tx, Acc, A2])
  extends AbstractTuple2Serializer[Tx, Acc, A1, A2]

final class ImmutableTuple2Serializer[A1, A2](protected val peer1: Serializer.Immutable[A1],
                                              protected val peer2: Serializer.Immutable[A2])
  extends AbstractTuple2Serializer[Any, Any, A1, A2] with ImmutableSerializer[(A1, A2)] {

  def read(in: DataInput): (A1, A2) = read(in, ())(())
}