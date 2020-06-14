package com.rfc.bufferwriter;

public class WriteOp {
    public final static byte SEEK = 0;
    public final static byte UI8 = 1;
    public final static byte UI16 = 2;
    public final static byte UI16BE = 3;
    public final static byte UI32 = 4;
    public final static byte UI32BE = 5;
    public final static byte UI64 = 6;
    public final static byte UI64BE = 7;
    public final static byte I8 = 10;
    public final static byte I16 = 11;
    public final static byte I16BE = 12;
    public final static byte I32 = 13;
    public final static byte I32BE = 14;
    public final static byte I64 = 15;
    public final static byte I64BE = 16;
    public final static byte FL = 19;
    public final static byte FLBE = 20;
    public final static byte DBL = 21;
    public final static byte DBLBE = 22;
    public final static byte VARINT = 23;
    public final static byte VARINT2 = 25;
    public final static byte BYTES = 27;
    public final static byte STR = 28;
    public final static byte CHECKSUM = 29;
    public final static byte FILL = 30;

    private byte type;
    private int enc;
    private int size;

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public int getEnc() {
        return enc;
    }

    public void setEnc(int enc) {
        this.enc = enc;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public WriteOp(byte type, int enc, int size) {
        this.type = type;
        this.enc = enc;
        this.size = size;
    }
}
