package com.allelink.wzyx.utils.log;

import com.orhanobut.logger.Logger;

/**
 * @author yangc
 * @version 1.0
 * @filename LogUtil
 * @date 2017/11/3
 * @description log 工具类,控制debug版和release版的日志输出
 * @email 1048027353@qq.com
 */

public final class LogUtil {
    public static final int VERBOSE = 5;
    public static final int DEBUG = 4;
    public static final int INFO = 3;
    public static final int WARN = 2;
    public static final int ERROR = 1;
    public static final int NOTHING = 0;
    public static int LEVEL = VERBOSE;
    public static void v(String tag,String msg){
        if(LEVEL >= VERBOSE){
            Logger.t(tag).v(msg);
        }
    }
    public static void d(String tag,String msg){
        if(LEVEL >= DEBUG){
            Logger.t(tag).d(msg);
        }
    }
    public static void i(String tag,String msg){
        if(LEVEL >= INFO){
            Logger.t(tag).i(msg);
        }
    }
    public static void w(String tag,String msg){
        if(LEVEL >= WARN){
            Logger.t(tag).w(msg);
        }
    }
    public static void e(String tag,String msg){
        if(LEVEL >= ERROR){
            Logger.t(tag).e(msg);
        }
    }
    /**
     * @param jsonContent
     * @return TODO
     * @description 输出json格式的日志
     */
    public static void json(String tag,String jsonContent){
        if(LEVEL >= ERROR){
            Logger.t(tag).json(jsonContent);
        }
    }

}
