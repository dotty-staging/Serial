/*
 *  package.scala
 *  (Serial)
 *
 *  Copyright (c) 2011-2013 Hanns Holger Rutz. All rights reserved.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *
 *  For further information, please contact Hanns Holger Rutz at
 *  contact@sciss.de
 */

package de.sciss

package object serial {
  /**
   * Specialization group consisting of all specializable types except `Byte` and `Short`.
   *
   * (AnyRef specialization seems currently disabled in Scala)
   */
  val SpecGroup = new Specializable.Group((
      scala.Int,  scala.Long,    scala.Float, scala.Double,
      scala.Char, scala.Boolean, scala.Unit /* , scala.AnyRef */
    ))
}