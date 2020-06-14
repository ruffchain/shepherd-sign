package com.rfc.bufferwriter;
import  com.rfc.bufferwriter.WriteOp;

public class WriteOpLong extends  WriteOp{
    private long value;

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public WriteOpLong(byte type, long value, int enc, int size) {
        super(type, enc, size);
        this.value = value;
    }
}
