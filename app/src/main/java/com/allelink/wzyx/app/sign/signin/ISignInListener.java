package com.allelink.wzyx.app.sign.signin;

/**
 * @author yangc
 * @version 1.0
 * @filename ISignInListener
 * @date 2017/11/9
 * @description 登录接口
 * @email 1048027353@qq.com
 */

public interface ISignInListener {
    /**
     * 登录成功回调
     * @param response 返回的数据
     */
    void onSignInSuccess(String response);
    /**
     * 登录失败回调
     * @param error 返回的错误信息
     */
    void onSignInFailure(String error);
}
