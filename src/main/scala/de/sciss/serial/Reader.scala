/*
 *  Reader.scala
 *  (Serial)
 *
 * Copyright (c) 2011-2014 Hanns Holger Rutz. All rights reserved.
 *
 * This software is published under the GNU Lesser General Public License v2.1+
 *
 *
 * For further information, please contact Hanns Holger Rutz at
 * contact@sciss.de
 */

package de.sciss
package serial

import scala.{specialized => spec}
import serial.{SpecGroup => ialized}

trait Reader[-Tx, @spec(Unit) -Acc, @spec(ialized) +A] {
  def read(in: DataInput, access: Acc)(implicit tx: Tx): A
}
