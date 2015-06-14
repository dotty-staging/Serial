package de.sciss.serial.impl

object Packed {
  def putInt(v: Int, buf: Array[Byte], pos: Int): Int =
    if      (v < -119) putLargeNegInt(v, buf, pos)
    else if (v >  119) putLargePosInt(v, buf, pos)
    else {
      buf(pos) = v.toByte
      1
    }

  @inline private[this] def putLargePosInt(v0: Int, buf: Array[Byte], pos: Int): Int = {
    val v   = v0 - 119
    buf(pos + 1) = v.toByte
    if ((v & 0xFFFFFF00) == 0) {
      buf(pos) = 120.toByte
      return 2
    }
    buf(pos + 2) = (v >>> 8).toByte
    if ((v & 0xFFFF0000) == 0) {
      buf(pos) = 121.toByte
      return 3
    }
    buf(pos + 3) = (v >>> 16).toByte
    if ((v & 0xFF000000) == 0) {
      buf(pos) = 122.toByte
      return 4
    }
    buf(pos + 4) = (v >>> 24).toByte
    buf(pos) = 123.toByte
    5
  }

  @inline private[this] def putLargeNegInt(v0: Int, buf: Array[Byte], pos: Int): Int = {
    val v   = -v0 - 119
    buf(pos + 1) = v.toByte
    if ((v & 0xFFFFFF00) == 0) {
      buf(pos) = -120.toByte
      return 2
    }
    buf(pos + 2) = (v >>> 8).toByte
    if ((v & 0xFFFF0000) == 0) {
      buf(pos) = -121.toByte
      return 3
    }
    buf(pos + 3) = (v >>> 16).toByte
    if ((v & 0xFF000000) == 0) {
      buf(pos) = -122.toByte
      return 4
    }
    buf(pos + 4) = (v >>> 24).toByte
    buf(pos) = -123.toByte
    5
  }

  def getReadLength(buf: Array[Byte], pos: Int): Int = {
    val b = buf(pos)
    if (b < -119) -b - 118 else if (b > 119) b - 118 else 1
  }

  def getInt(buf: Array[Byte], pos: Int): Int = {
    var neg     = false
    var readLen = 0

    val b1 = buf(pos)
    if (b1 < -119) {
      neg     = true
      readLen = -b1 - 118
    }
    else if (b1 > 119) {
      // neg = false
      readLen = b1 - 118
    } else {
      return b1
    }

    var value = buf(pos + 1) & 0xFF
    if (readLen > 2) {
      value |= (buf(pos + 2) & 0xFF) << 8
      if (readLen > 3) {
        value |= (buf(pos + 3) & 0xFF) << 16
        if (readLen > 4) {
          value |= (buf(pos + 4) & 0xFF) << 24
        }
      }
    }

    if (neg) -value - 119 else value + 119
  }
}
