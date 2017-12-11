package com.allelink.wzyx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.widget.Button;

import com.allelink.wzyx.R;
import com.allelink.wzyx.activity.base.BaseActivity;
import com.allelink.wzyx.app.WzyxApplication;
import com.allelink.wzyx.app.order.OrderHandler;
import com.allelink.wzyx.app.order.OrderListener;
import com.allelink.wzyx.ui.TitleBar;
import com.allelink.wzyx.ui.loader.WzyxLoader;
import com.allelink.wzyx.utils.activity.ActivityUtils;
import com.allelink.wzyx.utils.toast.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import qiu.niorgai.StatusBarCompat;

/**
 * 提交订单页
 * @author yangc
 * @date 2017/12/10
 * @version 1.0
 * @email yangchendev@qq.com
 */

public class SubmitOrderActivity extends BaseActivity {
    private static final String TAG = "SubmitOrderActivity";
    private static final String ACTIVITY_ID = "activityId";
    private static final String USER_ID = "userId";
    private static final String SELLER_ID = "sellerId";
    private static final String ACTIVITY_NAME = "activityName";
    private static final String ACTIVITY_COST = "cost";
    /**
    * UI
    */
    @BindView(R.id.tb_submit_order)
    TitleBar titleBar = null;
    @BindView(R.id.tv_submit_order_activity_name)
    AppCompatTextView tvActivityName = null;
    @BindView(R.id.tv_submit_total_cost)
    AppCompatTextView tvTotalCost = null;
    @BindView(R.id.btn_submit_order)
    Button submitOrder = null;
    /**
    * DATA
    */
    private String mActivityId = null;
    private String mUserId = null;
    private String mSellerId = null;
    private String mActivityName = null;
    private String mActivityCost = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_order);
        StatusBarCompat.setStatusBarColor(this,getResources().getColor(R.color.white));
        //隐藏actionBar
        android.app.ActionBar actionBar = getActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        //绑定ButterKnife
        ButterKnife.bind(this);
        initTitleBar();
        initData(getIntent());
        initView();
    }
    /**
    * 初始化titleBar点击事件
    */
    private void initTitleBar() {
        titleBar.setOnTitleBarButtonClickListener(new TitleBar.onTitleBarButtonClickListener() {
            @Override
            public void onLeftClick() {
                ((WzyxApplication)getApplication()).finishSingleActivity(SubmitOrderActivity.this);
            }

            @Override
            public void onRightClick() {

            }
        });
    }

    /**
    * 初始化数据到界面上
    */
    private void initView() {
        tvActivityName.setText(mActivityName);
        tvTotalCost.setText(getResources().getString(R.string.activity_cost,mActivityCost));
    }

    /**
    * 初始化数据
    */
    private void initData(Intent intent) {
        Bundle bundle = intent.getExtras();
        mActivityId = bundle.getString(ACTIVITY_ID);
        mUserId = bundle.getString(USER_ID);
        mSellerId = bundle.getString(SELLER_ID);
        mActivityName = bundle.getString(ACTIVITY_NAME);
        mActivityCost = bundle.getString(ACTIVITY_COST);
    }
    /**
    * 提交订单事件
    */
    @OnClick(R.id.btn_submit_order)
    void submitOrder(){
        params.clear();
        WzyxLoader.showLoading(this);
        params.put(USER_ID, "1");
        params.put(SELLER_ID, mSellerId);
        params.put(ACTIVITY_ID, mActivityId);
        OrderHandler.apply(params, new OrderListener() {
            @Override
            public void onSuccess() {
                WzyxLoader.stopLoading();
                ToastUtil.toastShort(SubmitOrderActivity.this,getResources().getString(R.string.submit_order_success));
                ActivityUtils.startActivity(SubmitOrderActivity.this,PayOrderActivity.class);
            }

            @Override
            public void onFailure(String errorMessage) {
                WzyxLoader.stopLoading();
                ToastUtil.toastShort(SubmitOrderActivity.this,getResources().getString(R.string.submit_order_failure));
            }
        });

    }
}
