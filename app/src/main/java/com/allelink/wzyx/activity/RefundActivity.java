package com.allelink.wzyx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.allelink.wzyx.R;
import com.allelink.wzyx.activity.base.BaseActivity;
import com.allelink.wzyx.app.WzyxApplication;
import com.allelink.wzyx.app.pay.IRefundListener;
import com.allelink.wzyx.app.pay.RefundHandler;
import com.allelink.wzyx.ui.TitleBar;
import com.allelink.wzyx.ui.loader.WzyxLoader;
import com.allelink.wzyx.utils.log.LogUtil;
import com.allelink.wzyx.utils.toast.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import qiu.niorgai.StatusBarCompat;

/**
 * 退款
 * @author yangc
 * @date 2017/12/22
 * @version 1.0
 * @email yangchendev@qq.com
 */

public class RefundActivity extends BaseActivity {
    private static final String TAG = RefundActivity.class.getSimpleName();
    private static final String ACTIVITY_COST = "cost";
    private static final String ORDER_ID = "orderId";
    /**
    * UI
    */
    @BindView(R.id.tb_activity_refund)
    TitleBar titleBar = null;
    @BindView(R.id.tv_activity_refund_money)
    TextView tvCost = null;
    @BindView(R.id.et_activity_refund_refund_desc)
    EditText etRefundDesc = null;
    @BindView(R.id.btn_activity_refund_submit_apply)
    Button btnApply = null;
    /**
    * DATA
    */
    private String mRefundCost = null;
    private String mOrderId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund);
        StatusBarCompat.setStatusBarColor(this,getResources().getColor(R.color.white));
        //隐藏actionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //绑定ButterKnife
        ButterKnife.bind(this);
        initView(getIntent());
        initTitleBar();
        initEditTextEvent();

    }
    /**
    * 初始化顶部导航栏
    */
    private void initTitleBar() {
        titleBar.setOnTitleBarButtonClickListener(new TitleBar.onTitleBarButtonClickListener() {
            @Override
            public void onLeftClick() {
                ((WzyxApplication)getApplication()).finishSingleActivity(RefundActivity.this);
            }
            @Override
            public void onRightClick() {

            }
        });
    }

    /**
    * 初始化editText输入监听事件
    */
    private void initEditTextEvent() {
        etRefundDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                LogUtil.d(TAG,"beforeTextChanged");
                disableApplyButton();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LogUtil.d(TAG,"onTextChanged");
                disableApplyButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
                LogUtil.d(TAG,"afterTextChanged");
                disableApplyButton();
            }
        });
    }

    /**
    * 初始化view
    */
    private void initView(Intent intent) {
        mRefundCost = intent.getStringExtra(ACTIVITY_COST);
        mOrderId = intent.getStringExtra(ORDER_ID);
        tvCost.setText(getResources().getString(R.string.activity_cost,mRefundCost));
        disableApplyButton();
    }
    /**
    * 提交申请按钮状态的切换
    */
    private void disableApplyButton(){
        if(etRefundDesc.getText().toString().isEmpty()){
            btnApply.setEnabled(false);
            btnApply.setBackground(getResources().getDrawable(R.drawable.btn_disable));
        }else{
            btnApply.setEnabled(true);
            btnApply.setBackground(getResources().getDrawable(R.drawable.btn_blue_selector));
        }
    }
    /**
    * 申请退款按钮点击事件
    */
    @OnClick(R.id.btn_activity_refund_submit_apply)
    void applyRefund(){
        String refundReason = etRefundDesc.getText().toString();
        params.clear();
        params.put("orderIdStr", mOrderId);
        params.put("refundReason",refundReason);
        WzyxLoader.showLoading(RefundActivity.this);
        RefundHandler.applyRefund(params, new IRefundListener() {
            @Override
            public void onSuccess() {
                WzyxLoader.stopLoading();
                ToastUtil.toastShort(RefundActivity.this,getResources().getString(R.string.apply_refund_success));
            }

            @Override
            public void onFailure(String errorMessage) {
                WzyxLoader.stopLoading();
                ToastUtil.toastShort(RefundActivity.this,getResources().getString(R.string.apply_refund_failure));
            }
        });
    }
}
