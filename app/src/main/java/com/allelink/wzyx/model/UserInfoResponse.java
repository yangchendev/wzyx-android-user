package com.allelink.wzyx.model;

/**
 * @author yangc
 * @version TODO
 * @filename UserInfoResponse
 * @date 2017/11/25
 * @description TODO
 * @email 1048027353@qq.com
 */

public class UserInfoResponse {
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
    /**
    * 用户信息
    */
    private User data = null;

    public User getData() {
        return data;
    }

    public void setData(User user) {
        this.data = user;
    }

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
