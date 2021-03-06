package com.rfc.bufferwriter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.util.Vector;

import com.rfc.Digest;
import com.rfc.Encoding;

public class BufferWriter {
    private Vector<WriteOp> ops;
    private int offset;

    public BufferWriter() {
        this.ops = new Vector<WriteOp>();
        this.offset = 0;
    }
    public int getSize(){
        return this.offset;
    }
    public void seek(int mOffset) {
        offset += mOffset;
        ops.add(new WriteOpInt(WriteOp.SEEK, mOffset, 0, 0));
    }
    public void destroy() {
        ops.clear();
        offset = 0;
    }
    public byte[] render() throws  Exception{
        byte[] data = new byte[offset];
        int off = 0;
        int mInt = 0;
        long mLong = 0;
        float mFloat = 0;
        double mDouble = 0;
        byte[] mByte;

        for (int i = 0; i < ops.size(); i++) {
            WriteOp op = ops.get(i);

            switch (op.getType()) {
                case WriteOp.SEEK:
                    off += ((WriteOpInt) op).getValue();
                    break;
                case WriteOp.UI8:
                case WriteOp.I8: // lyc: 重复代码合并
                    // off = data.writeInt8(op.value, off, true);
                    // off = data.writeUInt8(op.value, off, true);
                    mInt = ((WriteOpInt) op).getValue();
                    data[off++] = (byte) (mInt & 0xFF);
                    break;
                case WriteOp.UI16:
                case WriteOp.I16:
                    // off = data.writeInt16LE(op.value, off, true);
                    // off = data.writeUInt16LE(op.value, off, true);

                    mInt = ((WriteOpInt) op).getValue();
                    data[off++] = (byte) (mInt & 0xFF);
                    data[off++] = (byte) ((mInt >> 8) & 0xFF);
                    break;
                case WriteOp.UI16BE:
                case WriteOp.I16BE:
                    // off = data.writeInt16BE(op.value, off, true);
                    // off = data.writeUInt16BE(op.value, off, true);

                    mInt = ((WriteOpInt) op).getValue();
                    data[off++] = (byte) ((mInt >> 8) & 0xFF);
                    data[off++] = (byte) (mInt & 0xFF);
                    break;
                case WriteOp.UI32:
                case WriteOp.I32:
                    // off = data.writeInt32LE(op.value, off, true);

                    // off = data.writeUInt32LE(op.value, off, true);
                    mInt = ((WriteOpInt) op).getValue();

                    data[off++] = (byte) (mInt & 0xFF);
                    data[off++] = (byte) ((mInt >> 8) & 0xFF);
                    data[off++] = (byte) ((mInt >> 16) & 0xFF);
                    data[off++] = (byte) ((mInt >> 24) & 0xFF);

                    break;
                case WriteOp.UI32BE:
                case WriteOp.I32BE:
                    // off = data.writeInt32BE(op.value, off, true);
                    // off = data.writeUInt32BE(op.value, off, true);
                    mInt = ((WriteOpInt) op).getValue();
                    data[off++] = (byte) ((mInt >> 24) & 0xFF);
                    data[off++] = (byte) ((mInt >> 16) & 0xFF);
                    data[off++] = (byte) ((mInt >> 8) & 0xFF);
                    data[off++] = (byte) (mInt & 0xFF);
                    break;
                case WriteOp.UI64:

                case WriteOp.I64:
                    // off = Encoding.writeI64(data, op.value, off);
                    // off = Encoding.writeU64(data, op.value, off);
                    mLong = ((WriteOpLong) op).getValue();
                    data[off++] = (byte) (mLong & 0xFF);
                    data[off++] = (byte) ((mLong >> 8) & 0xFF);
                    data[off++] = (byte) ((mLong >> 16) & 0xFF);
                    data[off++] = (byte) ((mLong >> 24) & 0xFF);
                    data[off++] = (byte) ((mLong >> 32) & 0xFF);
                    data[off++] = (byte) ((mLong >> 40) & 0xFF);
                    data[off++] = (byte) ((mLong >> 48) & 0xFF);
                    data[off++] = (byte) ((mLong >> 56) & 0xFF);

                    break;
                case WriteOp.UI64BE:
                case WriteOp.I64BE:
                    // off = Encoding.writeI64BE(data, op.value, off);
                    // off = Encoding.writeU64BE(data, op.value, off);
                    mLong = ((WriteOpLong) op).getValue();

                    data[off++] = (byte) ((mLong >> 56) & 0xFF);
                    data[off++] = (byte) ((mLong >> 48) & 0xFF);
                    data[off++] = (byte) ((mLong >> 40) & 0xFF);
                    data[off++] = (byte) ((mLong >> 32) & 0xFF);
                    data[off++] = (byte) ((mLong >> 24) & 0xFF);
                    data[off++] = (byte) ((mLong >> 16) & 0xFF);
                    data[off++] = (byte) ((mLong >> 8) & 0xFF);
                    data[off++] = (byte) (mLong & 0xFF);

                    break;

                case WriteOp.FL:
                    // off = data.writeFloatLE(op.value, off, true);
                    mFloat = ((WriteOpFloat) op).getValue();
                    ByteBuffer buf = ByteBuffer.allocate(4);
                    buf.putFloat(mFloat);
                    byte[] mbuf = new byte[4];
                    buf.get(mbuf);
                    data[off++] = mbuf[3];
                    data[off++] = mbuf[2];
                    data[off++] = mbuf[1];
                    data[off++] = mbuf[0];

                    break;
                case WriteOp.FLBE:
                    // off = data.writeFloatBE(op.value, off, true);
                    mFloat = ((WriteOpFloat) op).getValue();
                    buf = ByteBuffer.allocate(4);
                    buf.putFloat(mFloat);
                    mbuf = new byte[4];
                    buf.get(mbuf);
                    data[off++] = mbuf[0];
                    data[off++] = mbuf[1];
                    data[off++] = mbuf[2];
                    data[off++] = mbuf[3];
                    break;
                case WriteOp.DBL:
                    // off = data.writeDoubleLE(op.value, off, true);
                    mDouble = ((WriteOpDouble) op).getValue();
                    buf = ByteBuffer.allocate(8);
                    buf.putDouble(mDouble);
                    mbuf = new byte[8];
                    buf.get(mbuf);
                    data[off++] = mbuf[7];
                    data[off++] = mbuf[6];
                    data[off++] = mbuf[5];
                    data[off++] = mbuf[4];
                    data[off++] = mbuf[3];
                    data[off++] = mbuf[2];
                    data[off++] = mbuf[1];
                    data[off++] = mbuf[0];
                    break;
                case WriteOp.DBLBE:
                    // off = data.writeDoubleBE(op.value, off, true);
                    mDouble = ((WriteOpDouble) op).getValue();
                    buf = ByteBuffer.allocate(8);
                    buf.putDouble(mDouble);
                    mbuf = new byte[8];
                    buf.get(mbuf);
                    data[off++] = mbuf[0];
                    data[off++] = mbuf[1];
                    data[off++] = mbuf[2];
                    data[off++] = mbuf[3];
                    data[off++] = mbuf[4];
                    data[off++] = mbuf[5];
                    data[off++] = mbuf[6];
                    data[off++] = mbuf[7];
                    break;
                case WriteOp.VARINT:
                    // off = Encoding.writeVarint(data, op.value, off);
                    // break;
                    // case WriteOp.VARINT2:
                    // // off = Encoding.writeVarint2(data, op.value, off);
                    mInt = ((WriteOpInt) op).getValue();
                    if (mInt < 0xfd) {
                        data[off++] = (byte) (mInt & 0xff);

                    } else if (mInt <= 0xffff) {
                        data[off++] = (byte) (0xfd);
                        data[off++] = (byte) (mInt & 0xff);
                        data[off++] = (byte) ((mInt >> 8) & 0xff);

                    } else if (mInt <= 0xffffffff) {
                        data[off++] = (byte) 0xfe;
                        data[off++] = (byte) (mInt & 0xff);
                        data[off++] = (byte) ((mInt >> 8) & 0xff);
                        data[off++] = (byte) ((mInt >> 16) & 0xff);
                        data[off++] = (byte) (mInt >> 24);
                    } else {
                        // We dont have number that big
                        throw new Exception("Excessive large number");
                    }
                    break;
                case WriteOp.STR:
                case WriteOp.BYTES:
                case WriteOp.FILL:
                    // data.fill(op.value, off, off + op.size);
                    // off += op.size;
                    // off += op.value.copy(data, off);
                    mByte = ((WriteOpBytes) op).getValue();
                    for (byte b : mByte) {
                        data[off++] = b;
                    }
                    break;
                // case WriteOp.STR:
                // // off += data.write(op.value, off, op.enc);
                // break;
                case WriteOp.CHECKSUM:
                    // off += digest.hash256(data.slice(0, off)).copy(data, off, 0, 4);
                    // compute sha256, 256bit, 32byte!
                    mByte = Digest.hash256(data, off);
                    for (i = 0; i < mByte.length; i++) {
                        data[off++] = mByte[i];
                    }

                    break;
                default:
                    assert (false);
                    break;
            }
        }

        assert (off == data.length);

        destroy();

        return data;
    }

    private void writeU8(int value) {
        offset += 1;
        ops.add(new WriteOpInt(WriteOp.UI8, value, 0, 0));
    }

    private void writeU16(int value) {
        offset += 2;
        ops.add(new WriteOpInt(WriteOp.UI16, value, 0, 0));
    }

    private void writeU16BE(int value) {
        offset += 2;
        ops.add(new WriteOpInt(WriteOp.UI16BE, value, 0, 0));

    }

    public void writeU32(int value) {
        offset += 4;
        ops.add(new WriteOpInt(WriteOp.UI32, value, 0, 0));
    }

    private void writeU32BE(int value) {
        offset += 4;
        ops.add(new WriteOpInt(WriteOp.UI32BE, value, 0, 0));

    }

    private void writeU64(long value) {
        offset += 8;
        ops.add(new WriteOpLong(WriteOp.UI64, value, 0, 0));

    }

    private void writeU64BE(long value) {
        offset += 8;
        ops.add(new WriteOpLong(WriteOp.UI64BE, value, 0, 0));

    }

    private void writeI8(int value) {
        this.offset += 1;
        ops.add(new WriteOpInt(WriteOp.I8, value, 0, 0));
    }

    private void writeI16(int value) {
        this.offset += 2;
        ops.add(new WriteOpInt(WriteOp.I16, value, 0, 0));
    }

    private void writeI16BE(int value) {
        this.offset += 2;
        ops.add(new WriteOpInt(WriteOp.I16BE, value, 0, 0));
    }

    private void writeI32(int value) {
        this.offset += 4;
        ops.add(new WriteOpInt(WriteOp.I32, value, 0, 0));
    }

    private void writeI32BE(int value) {
        this.offset += 4;
        ops.add(new WriteOpInt(WriteOp.I32BE, value, 0, 0));
    }

    private void writeI64(int value) {
        this.offset += 8;
        ops.add(new WriteOpInt(WriteOp.I64, value, 0, 0));
    }

    private void writeI64BE(int value) {
        this.offset += 8;
        ops.add(new WriteOpInt(WriteOp.I64BE, value, 0, 0));
    }

    private void writeFloat(Float value) {
        this.offset += 4;
        ops.add(new WriteOpFloat(WriteOp.FL, value, 0, 0));
    }

    private void writeFloatBE(Float value) {
        this.offset += 4;
        ops.add(new WriteOpFloat(WriteOp.FLBE, value, 0, 0));
    }

    private void writeOpDouble(Double value) {
        this.offset += 4;
        ops.add(new WriteOpDouble(WriteOp.DBL, value, 0, 0));
    }

    private void writeOpDoubleBE(Double value) {
        this.offset += 4;
        ops.add(new WriteOpDouble(WriteOp.DBLBE, value, 0, 0));
    }

    // private void WriteVariant(int value) {
    // this.offset += 4;
    // ops.add(new WriteOpDouble(WriteOp.DBL, value, 0, 0));
    // }
    public void writeBytes(byte[] value) {
        if (value.length == 0) {
            return;
        }
        offset += value.length;
        ops.add(new WriteOpBytes(WriteOp.BYTES, value, 0, 0));
    }

    public void writeBigNumber(BigDecimal val) {
        val = val.setScale(Encoding.MAX_DECIMAL_LEN, RoundingMode.HALF_UP);
        String str = val.stripTrailingZeros().toPlainString();
        BigDecimal valTemp = new BigDecimal(str);
        String strTemp = valTemp + "";
        strTemp = strTemp.replace('E', 'e');
        this.writeVarString(strTemp);

    }

    private void copy(byte[] value, int start, int end) {
        assert (end >= start);
        byte[] bytes = new byte[end - start];
        if (bytes.length >= 0)
            System.arraycopy(value, start, bytes, 0, bytes.length);
        writeBytes(bytes);
    }

    private void writeString(String value) {
        byte[] bytes = value.getBytes();
        writeBytes(bytes);
    }

    public void writeVarString(String value) {
        if (value.length() == 0) {
            this.offset += Encoding.sizeVarint(0);
            this.ops.add(new WriteOpInt(WriteOp.VARINT, 0, 0, 0));
        }
        int size = value.length();
        this.offset += Encoding.sizeVarint(size);
        this.offset += size;

        this.ops.add(new WriteOpInt(WriteOp.VARINT, size, 0, 0));
        this.ops.add(new WriteOpBytes(WriteOp.STR, value.getBytes(), 0, 0));
    }

    private void writeNullString(String value) {
        writeString(value);
        writeU8(0);
    }

    private void writeChecksum() {
        offset += 32;
        ops.add(new WriteOpInt(WriteOp.CHECKSUM, 0, 0, 0));
    }

    private void fill(int value, int size) {
        assert (size >= 0);

        if (size == 0) {
            return;
        }
        offset += size;
        byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes[i] = (byte) (value & 0xff);
        }
        writeBytes(bytes);
    }
}
