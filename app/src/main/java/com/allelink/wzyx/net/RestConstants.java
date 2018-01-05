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
    * http://101.132.191.9:8083/
     * http://192.168.1.105:8086/
    */
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
     * 请求用户信息的接口的地址
     */
    public static final String GET_USER_INFO_URL = BASE_URL + "alllink/user/getUserBasicInfo";
    /**
     * 更新用户信息的接口的地址
     */
    public static final String UPDATE_USER_INFO_URL = BASE_URL + "alllink/user/updateUserInfo";
    /**
     * 上传用户头像的接口的地址
     */
    public static final String UPLOAD_AVATAR_PIC_URL = BASE_URL + "alllink/user/uploadPic";
    /**
    * 请求默认活动列表信息的接口的地址
    */
    public static final String GET_DEFAULT_ACTIVITY_INFO_LIST_URL = BASE_URL + "alllink/activity/getDefaultActivityList";
    /**
     * 请求筛选后的活动列表信息的接口的地址
     */
    public static final String GET_ACTIVITY_INFO_LIST_URL = BASE_URL + "alllink/activity/getActivityList";
    /**
    * 请求某个活动具体信息的接口的地址
    */
    public static final String GET_ACTIVITY_DETAIL_INFO_URL = BASE_URL + "alllink/activity/getActivityInfo";
    /**
    * 活动报名接口
    */
    public static final String APPLY_ACTIVITY_URL = BASE_URL + "alllink/order/apply";
    /**
    * 获取未付款订单列表的接口的地址
    */
    public static final String GET_ORDER_LIST_URL = BASE_URL + "alllink/order/getOrderList";
    /**
    * 删除订单的接口的地址
    */
    public static final String DELETE_ORDER_URL = BASE_URL + "alllink/order/modifyOrderState";
    /**
    * 服务器图片目录地址
    */
    public static final String IMAGE_ROOT_URL = BASE_URL ;
    /**
    * 支付宝支付获取订单信息的地址
    */
    public static final String ALI_PAY_GET_ORDER_STRING_URL = BASE_URL + "alllink/alipay/payApp";
    /**
    * 通知后台订单支付成功
    */
    public static final String NOTIFY_UPDATE_ORDER_INFO_URL = BASE_URL+"alllink/pay/updateOrderInfo";
    /**
    * 申请退款地址
    */
    public static final String APPLY_REFUND_URL = BASE_URL+"alllink/pay/refundCheck";
}
