package com.allelink.wzyx.model;

/**
 * @author yangc
 * @version 1.0
 * @filename CommonResponse
 * @date 2017/11/9
 * @description 登录时服务器返回的数据类
 * @email 1048027353@qq.com
 */

public class CommonResponse {
    /**
    * result 成功还是失败
    */
    private String result = null;
    /**
    * 成功或失败的信息
    */
    private String message = null;
    /**
    * 异常信息
    */
    private String exception = null;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}
