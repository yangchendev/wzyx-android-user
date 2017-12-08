package com.allelink.citypicker.map;

/**
 * TODO
 *
 * @author yangc
 * @version TODO
 * @date 2017/12/8$
 * @email 1048027353@qq.com
 */
public interface LocateListener {

    /**
     * 定位成功回调接口
     * @param city 返回的城市名
     * @param lat  纬度
     * @param lng 经度
     */
    void onLocateSuccess(String city, double lat, double lng);
    /**
     * 定位失败回调接口
     * @param errorMessage 错误信息
     */
    void onLocateFailure(String errorMessage);
}
