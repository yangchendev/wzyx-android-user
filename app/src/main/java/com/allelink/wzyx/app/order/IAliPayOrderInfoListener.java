package com.allelink.wzyx.app.order;

/**
 * 支付宝订单信息接口
 * @author yangc
 * @version 1.0
 * @date 2017/12/19
 * @email yangchendev@qq.com
 */
public interface IAliPayOrderInfoListener {
    /**
     * 成功回调
     * @param orderInfo 订单号
     */
    void onSuccess(String orderInfo);

    /**
     * 失败回调
     * @param errorMessage 错误信息
     */
    void onFailure(String errorMessage);
}
