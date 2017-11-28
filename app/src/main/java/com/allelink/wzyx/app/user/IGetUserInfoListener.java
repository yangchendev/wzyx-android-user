package com.allelink.wzyx.app.user;

import com.allelink.wzyx.model.User;

/**
 * @author yangc
 * @version 1.0
 * @filename IGetUserInfoListener
 * @date 2017/11/25
 * @description 获取用户信息接口
 * @email 1048027353@qq.com
 */

public interface IGetUserInfoListener {
    /**
     * 成功回调
     * @param user 返回的数据
     */
    void onGetUserInfoSuccess(User user);
    /**
     * 失败回调
     * @param error 返回的错误信息
     */
    void onGetUserInfoFailure(String error);
}
