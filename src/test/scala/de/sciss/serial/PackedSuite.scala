package de.sciss.serial

import org.scalatest.FunSuite

class PackedSuite extends FunSuite {
  test("byte array (de)serialization is run with packed integers") {
    val dout = DataOutput()
    var i = 0
    var prev = -1
    while (i > prev) {
      prev = i
      dout.writePackedInt(i)
      dout.writePackedInt(-i)
      i = (i * 1.5).toInt + 1
    }
    println(s"OUT SIZE = ${dout.size}")
    // assert(dout.size === 33)

    val din = DataInput(dout.toByteArray)
    i = 0
    prev = -1
    while (i > prev) {
      val j = din.readPackedInt()
      assert(j === i)
      val k = din.readPackedInt()
      assert(k === -i)
      i = (i * 1.5).toInt + 1
    }
  }
}