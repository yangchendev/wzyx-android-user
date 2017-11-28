package com.allelink.wzyx.app.user;

/**
 * @author yangc
 * @version 1.0
 * @filename IUpdateUserInfoListener
 * @date 2017/11/25
 * @description 更新用户信息接口
 * @email 1048027353@qq.com
 */

public interface IUpdateUserInfoListener {
    /**
     * 成功回调
     * @param response 返回的数据
     */
    void onUpdateUserInfoSuccess(String response);
    /**
     * 失败回调
     * @param error 返回的错误信息
     */
    void onUpdateUserInfoFailure(String error);
}
