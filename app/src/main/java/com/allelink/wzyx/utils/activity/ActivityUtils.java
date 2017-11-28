package com.allelink.wzyx.utils.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * @author yangc
 * @version 1.0
 * @filename ActivityUtils
 * @date 2017/11/14
 * @description 活动工具类
 * @email 1048027353@qq.com
 */

public final class ActivityUtils {
    /**
    *  私有构造方法，防止通过 new 来实例化
    */
    private ActivityUtils(){
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 启动一个activity
     * @param context 当前要启动其他activity的上下文
     * @param clz 要启动的activity
     */
    public static void startActivity(Context context,@NonNull final Class<?> clz){
        Intent intent = new Intent(context, clz);
        context.startActivity(intent);
    }

}
