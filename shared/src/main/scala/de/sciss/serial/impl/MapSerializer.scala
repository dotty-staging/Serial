/*
 *  MapSerializer.scala
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

import scala.collection.immutable.{Map => IMap}

final class MapTFormat[-T, A, B](key  : TFormat[T, A],
                                 value: TFormat[T, B])
  extends TFormat[T, IMap[A, B]] {

  def write(coll: IMap[A, B], out: DataOutput): Unit = {
    val sz = coll.size
    out.writeInt(coll.size)
    if (sz > 0) {
      coll.foreach { tup =>
        key  .write(tup._1, out)
        value.write(tup._2, out)
      }
    }
  }

  def readT(in: DataInput)(implicit tx: T): IMap[A, B] = {
    val sz = in.readInt()
    if (sz == 0) IMap.empty
    else {
      val b = IMap.newBuilder[A, B]
      b.sizeHint(sz)
      var rem = sz
      while (rem > 0) {
        val _1 = key  .readT(in)
        val _2 = value.readT(in)
        b += ((_1, _2))
        rem -= 1
      }
      b.result()
    }
  }
}

final class ConstMapFormat[A, B](key  : ConstFormat[A],
                                 value: ConstFormat[B])
  extends ConstFormat[IMap[A, B]] {

  def write(coll: IMap[A, B], out: DataOutput): Unit = {
    val sz = coll.size
    out.writeInt(coll.size)
    if (sz > 0) {
      coll.foreach { tup =>
        key  .write(tup._1, out)
        value.write(tup._2, out)
      }
    }
  }

  def read(in: DataInput): IMap[A, B] = {
    val sz = in.readInt()
    if (sz == 0) IMap.empty
    else {
      val b = IMap.newBuilder[A, B]
      b.sizeHint(sz)
      var rem = sz
      while (rem > 0) {
        val _1 = key  .read(in)
        val _2 = value.read(in)
        b += ((_1, _2))
        rem -= 1
      }
      b.result()
    }
  }
}