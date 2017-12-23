package com.allelink.wzyx.ui.dialog;

/**
 * 对话框取消和确定按钮接口
 * @author yangc
 * @version 1.0
 * @date 2017/12/23
 * @email yangchendev@qq.com
 */
public interface IDialogListener {
    /**
    * 确定按钮点击回调
    */
    void onPositiveButtonClick();
    /**
    * 取消按钮点击回调
    */
    void onNegativeButtonClick();
}
