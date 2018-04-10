package de.sciss.serial

object Implicits {
  implicit object writerFromWritable extends Writer[Writable] {
    def write(w: Writable, out: DataOutput): Unit = w.write(out)
  }

  implicit def serializerFromWritableAndReader[Tx, Acc, A <: Writable](implicit reader: Reader[Tx, Acc, A]): Serializer[Tx, Acc, A] =
    new ReaderWrapper(reader)

  private final class ReaderWrapper[Tx, Acc, A <: Writable](reader: Reader[Tx, Acc, A]) extends Serializer[Tx, Acc, A] {
    def write(v: A, out: DataOutput): Unit = v.write(out)

    def read(in: DataInput, access: Acc)(implicit tx: Tx): A = reader.read(in, access)
  }
}