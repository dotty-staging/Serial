/*
 *  Reader.scala
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

object TReader {
  type Const[A] = TReader[Any, A]
}

trait Reader[+A] {
  def read(in: DataInput): A
}

trait TReader[-T, +A] {
  def readT(in: DataInput)(implicit tx: T): A
}

trait ConstReader[+A] extends TReader[Any, A] with Reader[A] {
  final def readT(in: DataInput)(implicit tx: Any): A = read(in)
}
