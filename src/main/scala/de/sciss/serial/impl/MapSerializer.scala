/*
 *  MapSerializer.scala
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

abstract class AbstractMapSerializer[Tx, Acc, A, B]
  extends Serializer[Tx, Acc, Map[A, B]] {

  protected def key   : Serializer[Tx, Acc, A]
  protected def value : Serializer[Tx, Acc, B]

  final def write(coll: Map[A, B], out: DataOutput): Unit = {
    val sz = coll.size
    out.writeInt(coll.size)
    if (sz > 0) {
      val _key    = key
      val _value  = value
      coll.foreach { tup =>
        _key  .write(tup._1, out)
        _value.write(tup._2, out)
      }
    }
  }

  final def read(in: DataInput, acc: Acc)(implicit tx: Tx): Map[A, B] = {
    val sz = in.readInt()
    if (sz == 0) Map.empty
    else {
      val b = Map.newBuilder[A, B]
      b.sizeHint(sz)
      val _key    = key
      val _value  = value
      var rem = sz
      while (rem > 0) {
        val _1 = _key  .read(in, acc)
        val _2 = _value.read(in, acc)
        b += ((_1, _2))
        rem -= 1
      }
      b.result()
    }
  }
}

final class MapSerializer[Tx, Acc, A, B](protected val key  : Serializer[Tx, Acc, A],
                                         protected val value: Serializer[Tx, Acc, B])
  extends AbstractMapSerializer[Tx, Acc, A, B]

final class ImmutableMapSerializer[A, B](protected val key  : Serializer.Immutable[A],
                                         protected val value: Serializer.Immutable[B])
  extends AbstractMapSerializer[Any, Any, A, B] with ImmutableSerializer[Map[A, B]] {

  def read(in: DataInput): Map[A, B] = read(in, ())(())
}