package com.allelink.wzyx.app.user;

/**
 * @author yangc
 * @version 1.0
 * @filename IUploadAvatarListener
 * @date 2017/11/27
 * @description 上传头像接口
 * @email 1048027353@qq.com
 */

public interface IUploadAvatarListener {

    /**
     * 成功回调接口
     * @param avatarUrl 头像地址
     */
    void onUploadAvatarSuccess(String avatarUrl);
    /**
     * 失败回调接口
     * @param error 错误信息
     */
    void onUploadAvatarFailure(String error);
}
