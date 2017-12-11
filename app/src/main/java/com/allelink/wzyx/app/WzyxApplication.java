package com.allelink.wzyx.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.allelink.wzyx.utils.log.LogUtil;
import com.blankj.utilcode.util.Utils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.BuildConfig;
import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;

/**
 * @author yangc
 * @version 1.1
 * @filename WzyxApplication
 * @date 2017/11/3
 * @description Application类，用于初始化
 * @email 1048027353@qq.com
 * @updateDate 2017/11/22
 */

public class WzyxApplication extends Application {
    /**
    * activity集合，用来统一管理activity
    */
    private List<Activity> activityList = new ArrayList<>();
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化Logger
        Logger.addLogAdapter(new AndroidLogAdapter());
        //控制日志输出
        LogUtil.LEVEL = LogUtil.VERBOSE;
        context = getApplicationContext();
        //初始化栈视图功能
        Fragmentation.builder()
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(BuildConfig.DEBUG)
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        //处理异常
                    }
                })
                .install();
        //AndroidUtilCode初始化
        Utils.init(this);

    }
    public static Context getContext(){
        return context;
    }

    /**
     * 添加activity
     * @param activity
     */
    public void addActivity(Activity activity){
        activityList.add(activity);
    }

    /**
     * 移除某个activity
     * @param activity
     */
    public void finishSingleActivity(Activity activity){
        activityList.remove(activity);
        if(!activity.isFinishing()){
            activity.finish();
        }
    }

    /**
    * 移除所有的activity(通常会在一键退出的时候使用)
    */
    public void finishAllActivity(){
        for (Activity activity : activityList){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
        activityList.clear();
        android.os.Process.killProcess(android.os.Process.myPid());

    }

    /**
     * 清除除了自己的其他activity
     * @param activity
     */
    public void finishAllActivityExceptMine(Activity activity){
        for(Activity ac : activityList){
            if(ac == activity){
                //如果是当前的activity，不做任何处理
            }else if(!ac.isFinishing()){
                //activityList.remove(ac);
                ac.finish();
            }
        }
    }
}
