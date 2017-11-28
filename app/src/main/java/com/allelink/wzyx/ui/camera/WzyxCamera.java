package com.allelink.wzyx.ui.camera;

import android.app.Activity;
import android.net.Uri;

import com.allelink.wzyx.utils.file.FileUtil;

/**
 * @author yangc
 * @version 1.0
 * @filename WzyxCamera
 * @date 2017/11/27
 * @description 相机调用类
 * @email 1048027353@qq.com
 */

public class WzyxCamera {

    /**
     * 生成裁剪后的图片
     * @return 图片uri
     */
    public static Uri createCropFile() {
        return Uri.parse
                (FileUtil.createFile("crop_image",
                        FileUtil.getFileNameByTime("IMG", "jpg")).getPath());
    }

    /**
     * 开启选择对话框
     * @param activity 需要开启对话框的activity
     */
    public static void start(Activity activity) {
        new CameraHandler(activity).beginCameraDialog();
    }
}
