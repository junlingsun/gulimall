package com.xxx.common.constant;


public enum AttrType {

    BASETYPE (1),
    SALETYPE (0);

    private int type;

    AttrType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
