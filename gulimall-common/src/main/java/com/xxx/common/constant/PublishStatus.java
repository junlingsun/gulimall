package com.xxx.common.constant;

public enum PublishStatus {
    PRODUCT_UP(1),
    PRODUCT_NEW(0),
    PRODUCT_DOWN(2);

    private Integer type;

    PublishStatus(Integer type) {
        this.type = type;
    }

    public Integer getType(){
        return this.type;
    }
}
