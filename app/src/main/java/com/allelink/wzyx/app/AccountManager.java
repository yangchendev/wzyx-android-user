package com.allelink.wzyx.app;

import com.allelink.wzyx.utils.storage.WzyxPreference;

/**
 * @author yangc
 * @version 1.0
 * @filename AccountManager
 * @date 2017/11/10
 * @description 账号管理类
 * @email 1048027353@qq.com
 */

public class AccountManager {
    private enum SignTag{
        /**
        * 登录标记
        */
        SIGN_TAG
    }

    /**
     * 保存用户登录状态，登录后调用
     * @param state 登录状态
     */
    public static void setSignState(boolean state){
        WzyxPreference.setAppFlag(SignTag.SIGN_TAG.name(),state);
    }

    /**
     * 获取用户登录状态
     * @return 用户登录的状态
     */
    private static boolean isSignIn(){
        return WzyxPreference.getAppFlag(SignTag.SIGN_TAG.name());
    }

    /**
     * 检查用户登录与否，并回调
     * @param userChecker 用户登录与否的接口
     */
    public static void checkAccount(IUserChecker userChecker){
        if(isSignIn()){
            userChecker.onSignIn();
        }else{
            userChecker.onNotSignIn();
        }
    }
}
