package com.allelink.wzyx.model;

/**
 * 活动信息
 * @author yangc
 * @date 2017/12/7
 * @version 1.0
 * @email 1048027353@qq.com
 */


public class ActivityItem {
    /**
    * 活动id
    */
    private String activityId = null;
    /**
    * 图片地址
    */
    private String imageUrl = null;
    /**
    * 活动名称
    */
    private String activityName = null;
    /**
    * 费用
    */
    private String cost = null;
    /**
    * 活动类型
    */
    private String activityType = null;
    /**
    * 总报名人数
    */
    private String totalNumber = null;
    /**
    * 已报名人数
    */
    private String enrollNumber = null;
    /**
    * 距离
    */
    private String distance = null;
    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
