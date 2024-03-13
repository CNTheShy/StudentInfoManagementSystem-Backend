package com.xust.sims.dto;


public enum ResponseCode {
    /**
     * 请求成功
     */
    SUCCESS(200, "require successfully"),
    /**
     * 权限不足
     */
    WARN(403, "Permission is insufficient. Contact the administrator"),
    /**
     * 尚未登录
     */
    NOT_LOGIN(206, "Not logged in yet, please log in"),
    /**
     * 验证码错误
     */
    IMAGE_CODE_ERROR(405, "Verification code error"),
    /**
     * 发生其他错误
     */
    ERROR(406, "error:406");
    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }

    private int code;
    private String msg;
}
