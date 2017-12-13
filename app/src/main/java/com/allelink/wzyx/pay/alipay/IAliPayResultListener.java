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
    void onPaySuccess();
    /**
    * 支付中
    */
    void onPaying();
    /**
    * 支付失败
    */
    void onPayFail();
    /**
    * 支付取消
    */
    void onPayCancel();
    /**
    * 网络连接错误
    */
    void onPayConnectError();

}
