package com.allelink.wzyx.utils.toast;

import android.content.Context;
import android.widget.Toast;

/**
 * @author yangc
 * @version 1.0
 * @filename ToastUtil
 * @date 2017/11/9
 * @description Toast统一管理类
 * @email 1048027353@qq.com
 */

public class ToastUtil {
    /**
    * 全局唯一的toast
    */
    private static Toast TOAST;
    /**
    * 防止该类被实例化
    */
    private ToastUtil(){
        throw new UnsupportedOperationException("ToastUtil不能被实例化");
    }
    /**
     * 短时间显示 toast
     * @param context 上下文
     * @param sequence  需要显示的信息
     */
    public static void toastShort(Context context,CharSequence sequence){
        if(TOAST == null){
            TOAST = Toast.makeText(context,sequence,Toast.LENGTH_SHORT);
        }else {
            TOAST.setText(sequence);
        }
        TOAST.show();
    }

    /**
     * 短时间显示 toast
     * @param context 上下文
     * @param stringId  需要显示的字符串的id
     */
    public static void toastShort(Context context,int stringId){
        if(TOAST == null){
            TOAST = Toast.makeText(context,stringId,Toast.LENGTH_SHORT);
        }else {
            TOAST.setText(stringId);
        }
        TOAST.show();
    }

    /**
     * 长时间显示 toast
     * @param context 上下文
     * @param sequence  需要显示的信息
     */
    public static void toastLong(Context context,CharSequence sequence){
        if(TOAST == null){
            TOAST = Toast.makeText(context,sequence,Toast.LENGTH_LONG);
        }else {
            TOAST.setText(sequence);
        }
        TOAST.show();
    }

    /**
     * 长时间显示 toast
     * @param context 上下文
     * @param stringId  需要显示的字符串的id
     */
    public static void toastLong(Context context,int stringId){
        if(TOAST == null){
            TOAST = Toast.makeText(context,stringId,Toast.LENGTH_SHORT);
        }else {
            TOAST.setText(stringId);
        }
        TOAST.show();
    }

    /**
    * 隐藏toast
    */
    public static void hideToast(){
        if(TOAST != null){
            TOAST.cancel();
        }
    }
}
