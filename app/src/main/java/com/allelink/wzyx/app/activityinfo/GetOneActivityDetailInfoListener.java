package com.allelink.wzyx.app.activityinfo;

import com.allelink.wzyx.model.ActivityDetailItem;

/**
 * 某个活动的详细信息接口
 * @author yangc
 * @date 2017/12/9
 * @version 1.0
 * @email yangchendev@qq.com
 */

public interface GetOneActivityDetailInfoListener {
    
    /**
     * 成功回调
     * @param activityDetailItem 某个活动的详细信息
     */
    void onSuccess(ActivityDetailItem activityDetailItem);

    /**
     * 失败回调
     * @param errorMessage 错误信息
     */
    void onFailure(String errorMessage);
}
