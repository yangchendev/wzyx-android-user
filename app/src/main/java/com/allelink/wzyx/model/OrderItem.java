package com.allelink.wzyx.model;

/**
 * 订单信息
 * @author yangc
 * @version 1.0
 * @date 2017/12/9
 * @email yangchendev@qq.com
 */
public class OrderItem {
    /**
    * 订单Id
    */
    private String orderId = null;
    /**
    * 活动名称
    */
    private String activityName = null;
    /**
    * 订单创建时间
    */
    private String createTime = null;
    /**
    * 花费金额
    */
    private String cost = null;
    /**
    * 订单状态
    */
    private String orderState = null;
    /**
    * 订单图片
    */
    private String imageUrl = null;
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
