package de.sciss.serial

trait ByteArrayStream {
  def reset(): Unit
  def toByteArray: Array[Byte]
  def buffer: Array[Byte]
}