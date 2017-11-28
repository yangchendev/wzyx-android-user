package com.allelink.wzyx.net.callback;

/**
 * @author yangc
 * @version 1.0
 * @filename ISuccess
 * @date 2017/11/2
 * @description 成功回调接口
 * @email 1048027353@qq.com
 */

public interface ISuccess {
    /**
     * 网络请求成功时的回调
     * @param response 成功返回的数据
     */
    void onSuccess(String response);
}
