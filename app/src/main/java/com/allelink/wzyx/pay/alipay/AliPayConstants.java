package com.allelink.wzyx.pay.alipay;

/**
 * 支付宝支付常用参数
 * @author yangc
 * @version 1.0
 * @date 2017/12/13
 * @email yangchendev@qq.com
 */
public final class AliPayConstants {
    /**
    * APP_ID 测试时使用的是沙箱应用的APP_ID
    */
    public static final String APP_ID = "2016082500309556";
    /**
     * 订单支付成功
     */
    public static final String ALI_PAY_STATUS_SUCCESS = "9000";
    /**
     * 订单处理中
     */
    public static final String ALI_PAY_STATUS_PAYING = "8000";
    /**
     * 订单支付失败
     */
    public static final String ALI_PAY_STATUS_FAIL = "4000";
    /**
     * 用户取消
     */
    public static final String ALI_PAY_STATUS_CANCEL = "6001";
    /**
     * 支付网络错误
     */
    public static final String ALI_PAY_STATUS_CONNECT_ERROR = "6002";
}
