package com.allelink.wzyx.net.callback;

/**
 * @author yangc
 * @version TODO
 * @filename IError
 * @date 2017/11/2
 * @description TODO
 * @email 1048027353@qq.com
 */

public interface IError {

    /**
     * 请求错误时的回调方法
     * @param code 错误码，msg 错误信息
     */
    void onError(int code,String msg);
}
