package com.allelink.wzyx.utils.dimen;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.allelink.wzyx.app.WzyxApplication;

/**
 * @author yangc
 * @version 1.0
 * @filename DimenUtil
 * @date 2017/11/26
 * @description 获取屏幕尺寸工具类
 * @email 1048027353@qq.com
 */

public class DimenUtil {
    public static int getScreenWidth() {
        final Resources resources = WzyxApplication.getContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight() {
        final Resources resources = WzyxApplication.getContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }
}
