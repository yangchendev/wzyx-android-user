package com.allelink.wzyx.ui.camera;

import android.net.Uri;

/**
 * @author yangc
 * @version 1.0
 * @filename CameraImageBean
 * @date 2017/11/27
 * @description 存储一些中间值
 * @email 1048027353@qq.com
 */

public final class CameraImageBean {
    private Uri mPath = null;
    /**
    * 单例模式
    */
    private static final CameraImageBean INSTANCE = new CameraImageBean();
    public static CameraImageBean getInstance(){
        return INSTANCE;
    }
    /**
    * 获取路径
    */
    public Uri getPath(){
        return mPath;
    }
    /**
    * 设置路径
    */
    public void setPath(Uri mPath){
        this.mPath = mPath;
    }
}
