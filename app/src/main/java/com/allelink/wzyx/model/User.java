package com.allelink.wzyx.model;

/**
 * @author yangc
 * @version 1.1
 * @filename User
 * @date 2017/11/9
 * @description 用户信息类
 * @email 1048027353@qq.com
 */

public class User {

    /**
    * 昵称
    */
    private String nickname = null;
    /**
    * 手机号码
    */
    private String phoneNumber = null;
    /**
    * 性别
    */
    private String gender = null;
    /**
    * 头像
    */
    private String avatar = null;
    /**
    * 生日
    */
    private String birthday = null;
    /**
    * 用户Id
    */
    private String userId = null;
    /**
    * 积分
    */
    private String totalPoints = null;
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(String totalPoints) {
        this.totalPoints = totalPoints;
    }

    @Override
    public String toString() {
        return "昵称："+nickname+"\n"+"手机号："+phoneNumber+"\n"+"性别："+gender+"\n"+"生日："
                +birthday+"\n"+"头像地址："+avatar+"\n"+"用户Id："+userId;
    }
}
