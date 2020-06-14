package com.rfc.bufferwriter;
import  com.rfc.bufferwriter.WriteOp;

public class WriteOpBytes extends WriteOp{
    private byte[] value;

    public WriteOpBytes(byte type, byte[] value, int enc, int size) {
        super(type, enc, size);
        this.value = value.clone();
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value.clone();
    }
}
