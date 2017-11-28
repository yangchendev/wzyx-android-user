package com.allelink.wzyx.utils.callback;

import android.support.annotation.Nullable;

/**
 * @author yangc
 * @version 1.0
 * @filename IGlobalCallback
 * @date 2017/11/27
 * @description 全局callback接口
 * @email 1048027353@qq.com
 */

public interface IGlobalCallback<T> {

    /**
     * callback 回调
     * @param args 泛型参数
     */
    void executeCallback(@Nullable T args);
}

