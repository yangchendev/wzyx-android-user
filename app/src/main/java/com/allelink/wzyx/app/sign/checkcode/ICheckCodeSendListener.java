package com.allelink.wzyx.app.sign.checkcode;

/**
 * @author yangc
 * @version 1.0
 * @filename ICheckCodeListener
 * @date 2017/11/10
 * @description 验证码发送回调接口
 * @email 1048027353@qq.com
 */

public interface ICheckCodeSendListener {
    /**
     * 成功回调
     * @param response 返回的数据
     */
    void onCheckCodeSendSuccess(String response);
    /**
     * 失败回调
     * @param error 返回的错误信息
     */
    void onCheckCodeSendFailure(String error);
}
