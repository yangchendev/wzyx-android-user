package com.allelink.wzyx.app.activityinfo;

import com.allelink.wzyx.model.ActivityItem;

import java.util.List;

/**
 * 活动信息接口
 * @author yangc
 * @version 1.0
 * @date 2017/12/8
 * @email 1048027353@qq.com
 */
public interface GetActivityInfoListener {
    
    /**
     * 成功回调
     * @param activityItems 活动列表集合
     */
    void onSuccess(List<ActivityItem> activityItems);

    /**
     * 失败回调
     * @param errorMessage 错误信息
     */
    void onFailure(String errorMessage);
}
