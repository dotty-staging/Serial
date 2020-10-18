package de.sciss.serial

import java.io.File

import org.scalatest.funsuite.AnyFunSuite

// runs on the JVM only
class FileSuite extends AnyFunSuite {
  test("file based (de)serialization is run on various primitives") {
    val f = File.createTempFile("serial", ".test")
    val dout = DataOutput.open(f)
    dout.writeByte(1)
    dout.writeByte(-1)
    dout.writeShort(2)
    dout.writeShort(-2)
    dout.writeInt(3)
    dout.writeInt(-3)
    dout.writeLong(4)
    dout.writeLong(-4)
    dout.write(Array[Byte](5,6,7))
    assert(dout.size === 33)
    dout.writeUTF("")         // +2
    dout.writeUTF("foo.bar")  // +9
    dout.writeBoolean(true)
    dout.writeBoolean(false)
    assert(dout.size === 46)
    dout.close()

    val din = DataInput.open(f)
    assert(din.size === 46)
    assert(din.readByte() === 1)
    assert(din.readByte() === -1)
    assert(din.readShort() === 2)
    assert(din.readShort() === -2)
    assert(din.readInt() === 3)
    assert(din.readInt() === -3)
    assert(din.readLong() === 4)
    assert(din.readLong() === -4)
    val arr = new Array[Byte](3)
    din.readFully(arr)
    assert(arr === Array[Byte](5,6,7))
    assert(din.readUTF() === "")
    assert(din.readUTF() === "foo.bar")
    assert(din.readBoolean() === true)
    assert(din.readBoolean() === false)
    din.close()
  }
}