package com.allelink.wzyx.model;

/**
 * 单个活动的详细信息
 * @author yangc
 * @version 1.0
 * @date 2017/12/9
 * @email 1048027353@qq.com
 */
public class ActivityDetailItem {
    /**
    * 活动ID
    */
    private String activityId = null;
    /**
    * 商家ID
    */
    private String sellerId = null;
    /**
    * 图片地址（可能包含多张图片）
    */
    private String imageUrl = null;
    /**
    * 活动名称
    */
    private String activityName = null;
    /**
    * 活动费用
    */
    private String cost = null;
    /**
    * 活动类型
    */
    private String activityType = null;
    /**
    * 总人数
    */
    private String totalNumber = null;
    /**
    * 已报名人数
    */
    private String enrollNumber = null;
    /**
    * 活动开始时间
    */
    private String beginTime = null;
    /**
    * 活动结束时间
    */
    private String endTime = null;
    /**
    * 活动详情
    */
    private String activityInfo = null;
    /**
    * 活动地址
    */
    private String address = null;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(String totalNumber) {
        this.totalNumber = totalNumber;
    }

    public String getEnrollNumber() {
        return enrollNumber;
    }

    public void setEnrollNumber(String enrollNumber) {
        this.enrollNumber = enrollNumber;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getActivityInfo() {
        return activityInfo;
    }

    public void setActivityInfo(String activityInfo) {
        this.activityInfo = activityInfo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
