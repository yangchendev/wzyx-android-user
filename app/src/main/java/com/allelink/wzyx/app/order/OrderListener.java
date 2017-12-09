package com.allelink.wzyx.app.order;

/**
 * 活动报名回调接口
 * @author yangc
 * @version 1.0
 * @date 2017/12/9
 * @email yangchendev@qq.com
 */
public interface OrderListener {
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
