package com.allelink.wzyx.ui.dialog;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * 对话框处理类
 * 使用步骤：DialogManager.create().with().setDialogListener().setDialogLayout().setButtonResId().setTitle().setContent().show();
 * @author yangc
 * @version 1.0
 * @date 2017/12/23
 * @email yangchendev@qq.com
 */
public class DialogManager implements View.OnClickListener {
    private  AlertDialog mDialog;
    private Context mContext;
    private int mDialogLayoutId;
    private int mPositiveButtonId;
    private int mNegativeButtonId;
    private IDialogListener mListener;
    private Window window;
    private TextView mTitle;
    private Button mBtnPositive;
    private Button mBtnNegative;
    /**
    * 单例模式
    */
    private DialogManager(){

    }

    private static class Holder{
        private static final DialogManager INSTANCE = new DialogManager();
    }
    private static DialogManager getInstance(){
        return Holder.INSTANCE;
    }

    public static DialogManager create(){
        return getInstance();
    }

    /**
     * 添加上下文
     * @param context 上下文
     * @return DialogManager
     */
    public DialogManager with(Context context){
        this.mContext = context;
        //创建dialog
        mDialog = new AlertDialog.Builder(mContext).create();
        //禁止触摸屏幕取消对话框
        mDialog.setCanceledOnTouchOutside(false);
        return this;
    }

    /**
     * 设置监听回调
     * @param listener 按钮监听器
     * @return DialogManager
     */
    public DialogManager setDialogListener(IDialogListener listener){
        this.mListener = listener;
        return this;
    }
    /**
     * 设置对话框的布局
     * @param layoutId 布局ID
     * @return DialogManager
     */
    public DialogManager setDialogLayout(@LayoutRes int layoutId){
        mDialogLayoutId = layoutId;
        //获取window，方便设置属性
        mDialog.show();
        window = mDialog.getWindow();
        if(window != null){
            //设置dialog的自定义布局
            window.setContentView(layoutId);
            //设置对话框的显示位置
            window.setGravity(Gravity.CENTER);
            //设置透明背景
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //获取属性
            final WindowManager.LayoutParams params = window.getAttributes();
            //宽度
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            //弹出对话框背景为灰色
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = 0.5f;
            window.setAttributes(params);
        }
        return this;
    }

    /**
     * 设置按钮id
     * @param positiveButtonId 确定按钮
     * @param negativeButtonId 取消按钮
     * @return DialogManager
     */
    public DialogManager setButtonResId(@IdRes int positiveButtonId,CharSequence positiveButtonText,
                                        @IdRes int negativeButtonId,CharSequence negativeButtonText){
        mPositiveButtonId = positiveButtonId;
        mNegativeButtonId = negativeButtonId;
        window = mDialog.getWindow();
        if(window != null){
            mBtnPositive = window.findViewById(positiveButtonId);
            mBtnPositive.setOnClickListener(this);
            mBtnNegative = window.findViewById(negativeButtonId);
            mBtnNegative.setOnClickListener(this);
            mBtnPositive.setText(positiveButtonText);
            mBtnNegative.setText(negativeButtonText);
        }
        return this;
    }

    /**
     * 设置标题
     * @param titleId 标题id
     * @param title 标题文字
     * @return DialogManager
     */
    public DialogManager setTitle(@IdRes int titleId,String title){
        if(window != null){
            mTitle = window.findViewById(titleId);
            mTitle.setText(title);
        }
        return this;
    }
    /**
     * 设置内容
     * @param contentId 标题id
     * @param content 标题文字
     * @return DialogManager
     */
    public DialogManager setContent(@IdRes int contentId,String content){
        if(window != null){
            mTitle = window.findViewById(contentId);
            mTitle.setText(content);
        }
        return this;
    }
    /**
    * 弹出对话框
    */
    public void show(){

    }
    /**
    * 按钮点击事件
    */
    @Override
    public void onClick(View v) {
        if (v.getId() == mPositiveButtonId){
            //确定按钮点击事件
            mListener.onPositiveButtonClick();
            mDialog.cancel();
        }else if(v.getId() == mNegativeButtonId){
            //取消按钮点击事件
            mListener.onNegativeButtonClick();
            mDialog.cancel();
        }
    }
}
