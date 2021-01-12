/*
 *  CollectionSerializer.scala
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

import scala.collection.immutable.{IndexedSeq => Vec, Set => ISet}
import scala.collection.mutable

abstract class CollectionTFormat[-T, A, That <: Traversable[A]] extends TFormat[T, That] {
  protected def newBuilder: mutable.Builder[A, That]
  protected def empty     : That

  def peer: TFormat[T, A]

  final def write(coll: That, out: DataOutput): Unit = {
    out.writeInt(coll.size)
    val ser = peer
    coll.foreach(ser.write(_, out))
  }

  final override def readT(in: DataInput)(implicit tx: T): That = {
    val sz = in.readInt()
    if (sz == 0) empty
    else {
      val b = newBuilder
      b.sizeHint(sz)
      val ser = peer
      var rem = sz
      while (rem > 0) {
        b += ser.readT(in)
        rem -= 1
      }
      b.result()
    }
  }
}

abstract class ConstCollectionFormat[A, That <: Traversable[A]]
  extends ConstFormat[That] {

  protected def newBuilder: mutable.Builder[A, That]
  protected def empty     : That

  def peer: ConstFormat[A]

  final def write(coll: That, out: DataOutput): Unit = {
    out.writeInt(coll.size)
    val ser = peer
    coll.foreach(ser.write(_, out))
  }

  final override def read(in: DataInput): That = {
    val sz = in.readInt()
    if (sz == 0) empty
    else {
      val b = newBuilder
      b.sizeHint(sz)
      val ser = peer
      var rem = sz
      while (rem > 0) {
        b += ser.read(in)
        rem -= 1
      }
      b.result()
    }
  }
}

final class ListTFormat[-T, A](val peer: TFormat[T, A])
  extends CollectionTFormat[T, A, List[A]] {

  protected def newBuilder: mutable.Builder[A, List[A]] = List.newBuilder[A]

  protected def empty: List[A] = Nil
}

final class SetTFormat[-T, A](val peer: TFormat[T, A])
  extends CollectionTFormat[T, A, ISet[A]] {

  protected def newBuilder: mutable.Builder[A, ISet[A]] = ISet.newBuilder[A]

  protected def empty: ISet[A] = ISet.empty
}

final class VecTFormat[-T, A](val peer: TFormat[T, A])
  extends CollectionTFormat[T, A, Vec[A]] {

  protected def newBuilder: mutable.Builder[A, Vec[A]] = Vector.newBuilder[A]

  protected def empty: Vec[A] = Vector.empty
}

final class ConstListFormat[A](val peer: ConstFormat[A])
  extends ConstCollectionFormat[A, List[A]] {

  protected def newBuilder: mutable.Builder[A, List[A]] = List.newBuilder[A]

  protected def empty: List[A] = Nil
}

final class ConstSetFormat[A](val peer: ConstFormat[A])
  extends ConstCollectionFormat[A, ISet[A]] {

  protected def newBuilder: mutable.Builder[A, ISet[A]] = ISet.newBuilder[A]

  protected def empty: ISet[A] = ISet.empty
}

final class ConstVecFormat[A](val peer: ConstFormat[A])
  extends ConstCollectionFormat[A, Vec[A]] {
  
  protected def newBuilder: mutable.Builder[A, Vec[A]] = Vector.newBuilder[A]

  protected def empty: Vec[A] = Vector.empty
}