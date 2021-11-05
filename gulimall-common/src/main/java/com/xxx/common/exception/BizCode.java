package com.xxx.common.exception;

public enum BizCode {

    VALID_EXCEPTION(10001, "parameter not valid"),
    UNKNOWN_EXCEPTION(10000, "unknown error");

    private Integer code;
    private String msg;

    BizCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
