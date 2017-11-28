package com.allelink.wzyx.utils.density;

import android.util.TypedValue;

import com.allelink.wzyx.app.WzyxApplication;

/**
 * @author yangc
 * @version 1.0
 * @filename DensityUtil
 * @date 2017/11/23
 * @description px sp dp 单位转换工具类
 * @email 1048027353@qq.com
 */

public class DensityUtil {
    private DensityUtil(){
        throw new UnsupportedOperationException("cannot be instantiated!");
    }

    /**
     * dp 转 px
     * @param dpValue 需要转换的dp值
     * @return 转换后的px值
     */
    public static int dp2px(float dpValue){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, WzyxApplication.getContext().getResources().getDisplayMetrics());
    }
    /**
     * sp 转 px
     * @param spValue 需要转换的sp值
     * @return 转换后的px值
     */
    public static int sp2px(float spValue){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, WzyxApplication.getContext().getResources().getDisplayMetrics());
    }

    /**
     * px 转 dp
     * @param pxValue 需要转换的px值
     * @return 转换好的的dp值
     */
    public static float px2dp(float pxValue){
        final float scale = WzyxApplication.getContext().getResources().getDisplayMetrics().density;
        return (pxValue / scale);
    }
    /**
     * px 转 sp
     * @param pxValue 需要转换的px值
     * @return 转换好的的sp值
     */
    public static float px2sp(float pxValue){
        final float scale = WzyxApplication.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (pxValue / scale);
    }
}
