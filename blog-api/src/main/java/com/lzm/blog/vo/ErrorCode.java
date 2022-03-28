package com.lzm.blog.vo;

public enum ErrorCode {
    PARAMS_ERROR(10001,"params error"),
    ACCOUNT_PWD_NOT_EXIST(10002,"account or password does not exist"),
    TOKEN_INVALID(10003,"the Token is invalid"),
    NO_PERMISSION(70001,"no permission"),
    SESSION_TIME_OUT(90001,"session time out"),
    NO_LOGIN(90002,"no login"),
    ACCOUNT_EXIST(90003, "Account already exists!" );

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private int code;
    private String msg;

}
