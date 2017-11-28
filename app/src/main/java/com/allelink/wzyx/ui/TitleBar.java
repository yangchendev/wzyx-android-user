package com.allelink.wzyx.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allelink.wzyx.R;
import com.allelink.wzyx.utils.density.DensityUtil;

/**
 * @author yangc
 * @version 1.0
 * @filename TitleBar
 * @date 2017/11/23
 * @description 自定义titleBar
 * @email 1048027353@qq.com
 */

public class TitleBar extends RelativeLayout{
    /**
    * 定义自定义属性的值
    */
    private String mLeftButtonText;
    private int mLeftButtonTextColor;
    private float mLeftButtonTextSize;
    private Drawable mLeftButtonBackground;
    private String mTitleText;
    private int mTitleTextColor;
    private float mTitleTextSize;
    private String mRightButtonText;
    private int mRightButtonTextColor;
    private float mRightButtonTextSize;
    private Drawable mRightButtonBackground;
    /**
    * UI
    */
    private ImageView mLeftButton;
    private TextView mLeftButtonTextView;
    private TextView mTitleTextView;
    private ImageView mRightButton;
    private TextView mRightButtonTextView;
    public TitleBar(Context context) {
       this(context,null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
        initView(context);
    }

    /**
     * 初始化自定义属性值
     * @param context 上下文
     * @param attrs 属性资源
     */
    private void init(Context context,AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        //左边按钮属性赋值
        mLeftButtonText = typedArray.getString(R.styleable.TitleBar_leftButtonText);
        mLeftButtonTextColor = typedArray.getColor(R.styleable.TitleBar_leftButtonTextColor, Color.WHITE);
        mLeftButtonTextSize = typedArray.getDimension(R.styleable.TitleBar_leftButtonTextSize,
                DensityUtil.sp2px(18));
        mLeftButtonBackground = typedArray.getDrawable(R.styleable.TitleBar_leftButtonBackground);
        //中间文字赋值
        mTitleText = typedArray.getString(R.styleable.TitleBar_titleText);
        mTitleTextColor = typedArray.getColor(R.styleable.TitleBar_titleColor, Color.WHITE);
        mTitleTextSize = typedArray.getDimension(R.styleable.TitleBar_titleSize,
                DensityUtil.sp2px(18));

        //右边按钮属性赋值
        mRightButtonText = typedArray.getString(R.styleable.TitleBar_rightButtonText);
        mRightButtonTextColor = typedArray.getColor(R.styleable.TitleBar_rightButtonTextColor, Color.WHITE);
        mRightButtonTextSize = typedArray.getDimension(R.styleable.TitleBar_rightButtonTextSize,
                DensityUtil.sp2px(18));
        mRightButtonBackground = typedArray.getDrawable(R.styleable.TitleBar_rightButtonBackground);
        //回收typeArray以免发生内存泄漏
        typedArray.recycle();
    }

    /**
     * 构建控件
     * @param context 上下文
     */
    private void initView(Context context){
        if(mLeftButtonBackground == null && mLeftButtonText != null){
            //当用户没有为左侧按钮设置背景，但设置了文字时------添加左侧文字按钮
            mLeftButtonTextView = new TextView(context);
            mLeftButtonTextView.setText(mLeftButtonText);
            mLeftButtonTextView.setTextColor(mLeftButtonTextColor);
            mLeftButtonTextView.setTextSize(mLeftButtonTextSize);
            //设置文字在父布局中的属性
            RelativeLayout.LayoutParams leftParams = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT
            );
            leftParams.addRule(RelativeLayout.CENTER_VERTICAL);
            leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            leftParams.setMargins(DensityUtil.dp2px(10),0,0,0);
            //添加子view到父布局中
            addView(mLeftButtonTextView,leftParams);
        }else if(mLeftButtonBackground != null){
            mLeftButton = new ImageView(context);
            mLeftButton.setImageDrawable(mLeftButtonBackground);
            //设置文字在父布局中的属性
            RelativeLayout.LayoutParams leftParams = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT
            );
            leftParams.addRule(RelativeLayout.CENTER_VERTICAL);
            leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            leftParams.setMargins(DensityUtil.dp2px(4),0,0,0);
            //添加子view到父布局中
            addView(mLeftButton,leftParams);
        }
        if(mTitleText != null){
            //添加中间的标题
            mTitleTextView = new TextView(context);
            mTitleTextView.setText(mTitleText);
            mTitleTextView.setTextSize(mTitleTextSize);
            mTitleTextView.setTextColor(mTitleTextColor);
            //设置文字在父布局中的属性
            RelativeLayout.LayoutParams leftParams = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT
            );
            leftParams.addRule(RelativeLayout.CENTER_VERTICAL);
            leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            leftParams.setMargins(DensityUtil.dp2px(40),0,0,0);
            //添加子view到父布局中
            addView(mTitleTextView,leftParams);
        }
        if(mRightButtonBackground == null && mRightButtonText != null){
            //当用户没有为左侧按钮设置背景，但设置了文字时------添加左侧文字按钮
            mRightButtonTextView = new TextView(context);
            mRightButtonTextView.setText(mRightButtonText);
            mRightButtonTextView.setTextColor(mRightButtonTextColor);
            mRightButtonTextView.setTextSize(mRightButtonTextSize);
            //设置文字在父布局中的属性
            RelativeLayout.LayoutParams rightParams = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT
            );
            rightParams.addRule(RelativeLayout.CENTER_VERTICAL);
            rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            rightParams.setMargins(0,0,DensityUtil.dp2px(10),0);
            //添加子view到父布局中
            addView(mRightButtonTextView,rightParams);
        }else if(mRightButtonBackground != null){
            mRightButton = new ImageView(context);
            mRightButton.setImageDrawable(mRightButtonBackground);
            //设置文字在父布局中的属性
            RelativeLayout.LayoutParams rightParams = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT
            );
            rightParams.addRule(RelativeLayout.CENTER_VERTICAL);
            rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            rightParams.setMargins(0,0,DensityUtil.dp2px(10),0);
            //添加子view到父布局中
            addView(mRightButton,rightParams);
        }
    }
    /**
    * 按钮点击事件回调接口
    */
    public interface onTitleBarButtonClickListener{
        /**
        * 左边按钮点击事件
        */
        void onLeftClick();
        /**
         * 右边按钮点击事件
         */
        void onRightClick();
    }

    /**
     * 提供给用户调用的设置点击事件的方法
     * @param listener 接口
     * @return TODO
     */
    public void setOnTitleBarButtonClickListener(final onTitleBarButtonClickListener listener){
        if(listener != null){
            if(mLeftButton != null){
                mLeftButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onLeftClick();
                    }
                });
            }
            if(mRightButton != null){
                mRightButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onRightClick();
                    }
                });
            }
            if(mTitleTextView != null){
                mTitleTextView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onLeftClick();
                    }
                });
            }
        }

    }
}
