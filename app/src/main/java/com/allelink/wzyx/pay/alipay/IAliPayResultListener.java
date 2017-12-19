package com.allelink.wzyx.pay.alipay;

/**
 * 支付宝支付结果回调接口
 * @author yangc
 * @version 1.0
 * @date 2017/12/13
 * @email yangchendev@qq.com
 */
public interface IAliPayResultListener {
    /**
    * 支付成功
    */
    void onAliPaySuccess();
    /**
    * 支付中
    */
    void onAliPaying();
    /**
    * 支付失败
    */
    void onAliPayFail();
    /**
    * 支付取消
    */
    void onAliPayCancel();
    /**
    * 网络连接错误
    */
    void onAliPayConnectError();

}
