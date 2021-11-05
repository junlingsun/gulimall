package com.xxx.gulimall.elasticsearch.constant;

public enum IndexConstant {

    PRODUCT_INDEX(1, "gulimall_product"),
    PAGE_SIZE(2, "records per page");
    Integer code;
    String name;


    IndexConstant(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
