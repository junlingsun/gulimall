package com.xxx.common.constant;

public enum PurchaseStatus {
    CREATED(0),
    ASSIGNED(1),
    PICKED(2),
    COMPLETED(3),
    ABNORMAL(4);



    private Integer status;

    PurchaseStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
