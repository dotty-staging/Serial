/*
 *  package.scala
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
