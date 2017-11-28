package com.allelink.wzyx.app.sign.register;

/**
 * @author yangc
 * @version 1.0
 * @filename ISignListener
 * @date 2017/11/9
 * @description 注册接口
 * @email 1048027353@qq.com
 */

public interface IRegisterListener {
    /**
    * 注册成功回调
     * @param response 返回的数据
    */
    void onRegisterSuccess(String response);
    /**
     * 注册失败回调
     * @param error 返回的错误信息
     */
    void onRegisterFailure(String error);
}
