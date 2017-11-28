package com.allelink.wzyx.utils.callback;

import java.util.WeakHashMap;

/**
 * @author yangc
 * @version 1.0
 * @filename CallbackManager
 * @date 2017/11/27
 * @description 全局callback管理类
 * @email 1048027353@qq.com
 */

public class CallbackManager {
    private static final WeakHashMap<Object, IGlobalCallback> CALLBACKS = new WeakHashMap<>();
    /**
    * 单例模式
    */
    private static class Holder{
        private static final CallbackManager INSTANCE = new CallbackManager();
    }
    public static CallbackManager getInstance(){
        return Holder.INSTANCE;
    }

    /**
     * 添加回调
     * @param tag 标签
     * @param callback 回调
     * @return CallbackManager
     */
    public CallbackManager addCallback(Object tag,IGlobalCallback callback){
        CALLBACKS.put(tag, callback);
        return this;
    }

    /**
     * 获取回调
     * @param tag 标签
     * @return callback
     */
    public IGlobalCallback getCallback(Object tag){
        return CALLBACKS.get(tag);
    }
}
