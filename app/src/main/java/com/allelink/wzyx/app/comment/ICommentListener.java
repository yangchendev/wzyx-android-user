package com.allelink.wzyx.app.comment;

/**
 * Created by sushijun on 2018/1/5.
 */

public interface ICommentListener {
    /**
     * 成功回调
     * @param jsonData 订单号
     */
    void onSuccess(String jsonData);
    /**
     * 失败回调
     * @param errorMessage 错误信息
     */
    void onFailure(String errorMessage);
}
