/*
 *  Implicits.scala
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

object Implicits {
  implicit object writerFromWritable extends Writer[Writable] {
    def write(w: Writable, out: DataOutput): Unit = w.write(out)
  }

  implicit def serializerFromWritableAndTReader[T, A <: Writable](implicit reader: TReader[T, A]): TFormat[T, A] =
    new TReaderWrapper(reader)

  private final class TReaderWrapper[T, A <: Writable](reader: TReader[T, A]) extends WritableFormat[T, A] {
    override def readT(in: DataInput)(implicit tx: T): A = reader.readT(in)
  }
}