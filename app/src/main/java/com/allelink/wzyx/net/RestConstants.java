package com.allelink.wzyx.net;

/**
 * @author yangc
 * @version 1.0
 * @filename RestConstants
 * @date 2017/11/2
 * @description 保存网络框架所用到的常量
 * @email 1048027353@qq.com
 */

public class RestConstants {
    /**
    * 服务器主机的地址
    */
    public static final String BASE_URL = "http://101.132.191.9:8083/";
    /**
     * 注册的地址
     */
    public static final String REGISTER_URL = BASE_URL + "alllink/Register/toregistered";
    /**
     * 登录的地址
     */
    public static final String SIGN_IN_URL = BASE_URL + "alllink/Login/tologin";
    /**
     * 发送验证码的地址
     */
    public static final String SMS_URL = BASE_URL + "alllink/SendMessage/sms";
    /**
     * 修改密码的地址
     */
    public static final String RESET_PASSWORD_URL = BASE_URL + "alllink/user/resetPassword";
    /**
     * 注册时发送验证码的标志位
     */
    public static final String CHECK_CODE_FOR_REGISTER = "0";
    /**
     * 修改密码时发送验证码的标志位
     */
    public static final String CHECK_CODE_FOR_RESET_PASSWORD = "1";
    /**
     * 请求用户信息的接口
     */
    public static final String GET_USER_INFO_URL = BASE_URL + "alllink/user/getUserBasicInfo";
    /**
     * 更新用户信息的接口
     */
    public static final String UPDATE_USER_INFO_URL = BASE_URL + "alllink/user/updateUserInfo";
    /**
     * 上传用户头像的接口
     */
    public static final String UPLOAD_AVATAR_PIC_URL = BASE_URL + "alllink/user/uploadPic";
}
