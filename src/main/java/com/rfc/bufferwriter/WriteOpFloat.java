package com.rfc.bufferwriter;
import  com.rfc.bufferwriter.WriteOp;

public class WriteOpFloat extends WriteOp{
    private Float value;

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public WriteOpFloat(byte type, Float value,int enc, int size) {
        super(type, enc, size);
        this.value = value;
    }
}
