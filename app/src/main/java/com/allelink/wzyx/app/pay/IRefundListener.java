package com.allelink.wzyx.app.pay;

/**
 * 退款回调
 * @author yangc
 * @version 1.0
 * @date 2017/12/22
 * @email yangchendev@qq.com
 */
public interface IRefundListener {
    /**
     * 成功回调
     */
    void onSuccess();
    /**
     * 失败回调
     * @param errorMessage 错误信息
     */
    void onFailure(String errorMessage);

}
