package com.allelink.wzyx.ui.camera;

import com.yalantis.ucrop.UCrop;

/**
 * @author yangc
 * @version 1.0
 * @filename RequestCodes
 * @date 2017/11/27
 * @description 拍照、裁剪图片的请求码
 * @email 1048027353@qq.com
 */

public class RequestCodes {
    public static final int TAKE_PHOTO = 4;
    public static final int PICK_PHOTO = 5;
    public static final int CROP_PHOTO = UCrop.REQUEST_CROP;
    public static final int CROP_ERROR = UCrop.RESULT_ERROR;
}
