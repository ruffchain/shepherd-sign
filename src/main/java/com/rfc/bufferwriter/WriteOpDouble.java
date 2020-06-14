package com.rfc.bufferwriter;

import  com.rfc.bufferwriter.WriteOp;

public class WriteOpDouble extends WriteOp{
    private   Double value;

    public WriteOpDouble(byte type, Double value, int enc, int size) {
        super(type, enc, size);
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
