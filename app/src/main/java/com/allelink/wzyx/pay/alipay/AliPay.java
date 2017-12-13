package com.allelink.wzyx.pay.alipay;

import android.app.Activity;

/**
 * 支付宝支付
 * @author yangc
 * @version 1.0
 * @date 2017/12/13
 * @email yangchendev@qq.com
 */
public class AliPay {
    private IAliPayResultListener mIAliPayResultListener = null;
    private Activity mActivity = null;
    private String mOrderId = "123456";

    private AliPay(Activity activity){
        this.mActivity = activity;
    }
    /**
    * 简单工厂模式
    */
    public static AliPay create(Activity activity){
        return new AliPay(activity);
    }
    /**
    * 设置支付结果监听器
    */
    public AliPay setPayResultListener(IAliPayResultListener listener){
        this.mIAliPayResultListener = listener;
        return this;
    }
    /**
    * 设置订单号
    */
    public AliPay setOrderId(String orderId){
        this.mOrderId = orderId;
        return this;
    }

    /**
    * TODO
    */
    private void aliPay(String orderId){
        //1.通过服务器获取签名后的订单信息
        //2.获取成功后发起支付宝支付

    }

}
