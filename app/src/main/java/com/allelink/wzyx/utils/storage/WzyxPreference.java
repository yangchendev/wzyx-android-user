package com.allelink.wzyx.utils.storage;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.allelink.wzyx.app.WzyxApplication;

/**
 * @author yangc
 * @version 1.0
 * @filename WzyxPreference
 * @date 2017/11/10
 * @description 本地数据存储
 * @email 1048027353@qq.com
 */

public class WzyxPreference {
    /**
    * 一些需要用到的键
    */
    public static final String KEY_NICKNAME = "nickname";
    public static final String KEY_PHONE_NUMBER = "phoneNumber";
    public static final String KEY_BIRTHDAY = "birthday";
    public static final String KEY_AVATAR_URL = "avatar_url";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_PAY_SUCCESS = "pay";
    public static final String KEY_TOTAL_POINTS = "totalPoints";
    /**
    * 获取全局SharedPreferences
    */
    private static final SharedPreferences PREFERENCES =
            PreferenceManager.getDefaultSharedPreferences(WzyxApplication.getContext());
    private static final String APP_PREFERENCE_KEY = "profile";
    private static SharedPreferences getAppPreference(){
        return PREFERENCES;
    }

    /**
    * 设置登录状态
    */
    public static void setAppFlag(String key,boolean flag){
        getAppPreference()
                .edit()
                .putBoolean(key,flag)
                .apply();
    }

    /**
     * 获得登录状态
     */
    public static boolean getAppFlag(String key){
        return getAppPreference().getBoolean(key,false);
    }

    /**
     * 添加用户信息
     * @param key 键
     * @param val 值
     */
    public static void addCustomAppProfile(String key,String val){
        getAppPreference()
                .edit()
                .putString(key,val)
                .apply();
    }
    /**
     * 获取用户具体某一信息（如：昵称、生日）
     * @param key 键
     */
    public static String getCustomAppProfile(String key){
        return getAppPreference().getString(key,"");
    }

    /**
    * 清除数据
    */
    public static void clearAppPreferences(){
        getAppPreference()
                .edit()
                .clear()
                .apply();
    }
}
