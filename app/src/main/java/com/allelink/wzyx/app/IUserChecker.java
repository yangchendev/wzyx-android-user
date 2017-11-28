package com.allelink.wzyx.app;

/**
 * @author yangc
 * @version 1.0
 * @filename IUserChecker
 * @date 2017/11/14
 * @description 用户登录与否的接口
 * @email 1048027353@qq.com
 */

public interface IUserChecker {
    /**
    * 用户已登录接口
    */
    void onSignIn();
    /**
    * 用户未登录接口
    */
    void onNotSignIn();
}
