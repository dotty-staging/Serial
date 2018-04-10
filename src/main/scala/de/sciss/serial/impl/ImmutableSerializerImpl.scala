/*
 *  ImmutableSerializerImpl.scala
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

trait ImmutableSerializerImpl[A] extends ImmutableSerializer[A] {
  def read(in: DataInput, access: Any)(implicit tx: Any): A = read(in)
}

trait ImmutableReaderImpl[A] extends ImmutableReader[A] {
  def read(in: DataInput, access: Any)(implicit tx: Any): A = read(in)
}
