package com.allelink.wzyx.net.callback;

/**
 * @author yangc
 * @version 1.0
 * @filename IRequest
 * @date 2017/11/2
 * @description 请求接口
 * @email 1048027353@qq.com
 */

public interface IRequest {
    /**
    * 请求开始时调用
    */
    void onRequestStart();
    /**
    * 请求结束时调用
    */
    void onRequestEnd();
}
