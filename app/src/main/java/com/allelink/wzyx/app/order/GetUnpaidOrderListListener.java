package com.allelink.wzyx.app.order;

import com.allelink.wzyx.model.OrderItem;

import java.util.List;

/**
 * 获取未付款订单集合接口
 * @author yangc
 * @version 1.0
 * @date 2017/12/9
 * @email yangchendev@qq.com
 */
public interface GetUnpaidOrderListListener {

    /**
     * 成功回调
     * @param orderItems 未付款订单列表集合
     */
    void onSuccess(List<OrderItem> orderItems);

    /**
     * 失败回调
     * @param errorMessage 错误信息
     */
    void onFailure(String errorMessage);



}
