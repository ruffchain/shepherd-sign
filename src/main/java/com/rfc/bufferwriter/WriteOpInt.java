package com.rfc.bufferwriter;

import com.rfc.bufferwriter.WriteOp;

public class WriteOpInt extends WriteOp{
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public WriteOpInt(byte type, int val, int enc, int size) {
        super(type, enc, size);
        this.value = val;
    }
}
