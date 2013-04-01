/*
 *  FileWrapperImpl.scala
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

package de.sciss.serial
package impl

import java.io.{InputStream, OutputStream, File, RandomAccessFile}

private[impl] final class FileInputStreamImpl(peer: RandomAccessFile)
  extends InputStream {

  def read(): Int = peer.read()
  override def read(b: Array[Byte], off: Int, len: Int) = peer.read(b, off, len)
  override def skip(n: Long): Long = {
    val oldPos  = peer.getFilePointer
    val newPos  = math.min(peer.length(), oldPos + n)
    peer.seek(newPos)
    newPos - oldPos
  }
  override def available(): Int = { math.min(0x7FFFFFFFL, peer.length() - peer.getFilePointer).toInt }
  override def close() { peer.close() }
}

private[impl] final class FileOutputStreamImpl(peer: RandomAccessFile) extends OutputStream {
  def write(b: Int) {
    peer.write(b)
  }

  override def write(b: Array[Byte], off: Int, len: Int) {
    peer.write(b, off, len)
  }

  override def close() { peer.close() }
}

private[serial] abstract class FileWrapperImpl(file: File, mode: String)
  extends RandomAccessFile(file, mode) with DataInput with DataOutput {

  final def asInputStream : InputStream  = new FileInputStreamImpl (this)
  final def asOutputStream: OutputStream = new FileOutputStreamImpl(this)

  final def size: Int = {
    val n = length()
    require(n <= 0x7FFFFFFFL, "File too large")
    n.toInt
  }

  final def position: Int = {
    val n = getFilePointer
    require(n <= 0x7FFFFFFFL, "File too large")
    n.toInt
  }

  final def position_=(value: Int) {
    seek(value)
  }
}