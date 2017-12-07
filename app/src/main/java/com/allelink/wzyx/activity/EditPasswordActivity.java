package com.allelink.wzyx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.widget.Button;

import com.allelink.wzyx.R;
import com.allelink.wzyx.activity.base.BaseActivity;
import com.allelink.wzyx.app.AccountManager;
import com.allelink.wzyx.app.WzyxApplication;
import com.allelink.wzyx.app.sign.checkcode.CheckCodeHandler;
import com.allelink.wzyx.app.sign.checkcode.ICheckCodeSendListener;
import com.allelink.wzyx.app.sign.register.IRegisterListener;
import com.allelink.wzyx.app.sign.register.RegisterHandler;
import com.allelink.wzyx.net.RestConstants;
import com.allelink.wzyx.ui.TitleBar;
import com.allelink.wzyx.utils.log.LogUtil;
import com.allelink.wzyx.utils.regex.RegexUtils;
import com.allelink.wzyx.utils.toast.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import qiu.niorgai.StatusBarCompat;

/**
 * 修改密码
 * @author yangc
 * @date 2017/11/24
 * @version 1.0
 * @email 1048027353@qq.com
 */

public class EditPasswordActivity extends BaseActivity {
    private static final String TAG = "EditPasswordActivity";
    private static final int REQUEST_CODE_EDIT_PASSWORD = 2001;
    /**
    * UI
    */
    @BindView(R.id.tb_edit_password)
    TitleBar titleBar = null;
    @BindView(R.id.et_edit_password_phone_number)
    TextInputEditText etPhoneNumber = null;
    @BindView(R.id.et_edit_password_code)
    TextInputEditText etCode = null;
    @BindView(R.id.et_edit_password_confirm_password)
    TextInputEditText etPassword = null;
    @BindView(R.id.tv_edit_password_send_code)
    AppCompatTextView tvSendCode = null;
    @BindView(R.id.btn_edit_password_confirm)
    Button btnConfirm = null;
    /**
     * 手机号码
     */
    private String mPhoneNumber = null;
    /**
     * 密码
     */
    private String mPassword = null;
    /**
     * 验证码
     */
    private String mAuthCode = null;
    /**
     * 验证码倒计时
     */
    private CountDownTimer mCountDownTimer = new CountDownTimer(60000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            tvSendCode.setEnabled(false);
            tvSendCode.setText(millisUntilFinished/1000 + "秒后重发");
        }
        @Override
        public void onFinish() {
            tvSendCode.setEnabled(true);
            tvSendCode.setText(getResources().getString(R.string.send_code));
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        StatusBarCompat.setStatusBarColor(this,getResources().getColor(R.color.white));
        //隐藏actionBar
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        //绑定ButterKnife
        ButterKnife.bind(this);
        initTitleBar();
    }

    /**
    * 初始化titlebar
    */
    private void initTitleBar() {
        titleBar.setOnTitleBarButtonClickListener(new TitleBar.onTitleBarButtonClickListener() {
            @Override
            public void onLeftClick() {
                EditPasswordActivity.this.finish();
            }

            @Override
            public void onRightClick() {

            }
        });
    }

    /**
     * 发送验证码
     */
    @OnClick(R.id.tv_edit_password_send_code)
    void sendCode(){
        mPhoneNumber = etPhoneNumber.getText().toString().trim();
        //手机号不合法
        if(!checkPhoneNumber(mPhoneNumber)){
            mCountDownTimer.cancel();
            tvSendCode.setEnabled(true);
            tvSendCode.setText(getResources().getString(R.string.send_code));
            return;
        }
        mCountDownTimer.start();
        params.clear();
        params.put("phoneNumber", mPhoneNumber);
        params.put("sign", RestConstants.CHECK_CODE_FOR_RESET_PASSWORD);
        CheckCodeHandler.sendCheckCode(params, new ICheckCodeSendListener() {
            @Override
            public void onCheckCodeSendSuccess(String response) {
                ToastUtil.toastShort(EditPasswordActivity.this,getResources().getString(R.string.code_send_success));
            }

            @Override
            public void onCheckCodeSendFailure(String error) {
                ToastUtil.toastShort(EditPasswordActivity.this,error);
                //发送失败恢复按钮状态
                mCountDownTimer.cancel();
                tvSendCode.setEnabled(true);
                tvSendCode.setText(getResources().getString(R.string.send_code));
            }
        });
    }
    /**
    * 确认修改密码
    */
    @OnClick(R.id.btn_edit_password_confirm)
    void confirm(){
        mPassword = etPassword.getText().toString().trim();
        mAuthCode = etCode.getText().toString().trim();
        if(!checkAuthCode(mAuthCode)){
            ToastUtil.toastShort(EditPasswordActivity.this,getResources().getString(R.string.empty_auth_code));
            return;
        }
        if(!checkPassword(mPassword)){
            ToastUtil.toastShort(EditPasswordActivity.this,getResources().getString(R.string.wrong_password));
            return;
        }
        params.clear();
        params.put("phoneNumber", mPhoneNumber);
        params.put("password", mPassword);
        params.put("checkcode", mAuthCode);
        btnConfirm.setText(getResources().getString(R.string.password_altering));
        btnConfirm.setEnabled(false);
        RegisterHandler.resetPassword(params, new IRegisterListener() {
            //成功回调
            @Override
            public void onRegisterSuccess(String response) {
                LogUtil.d(TAG,response);
                btnConfirm.setEnabled(true);
                btnConfirm.setText(getResources().getString(R.string.confirm));
                ToastUtil.toastShort(EditPasswordActivity.this,getResources().getString(R.string.alter_password_success));
                //修改密码成功后需要重新登录
                //设置未登录状态
                AccountManager.setSignState(false);
                Intent intent = new Intent(EditPasswordActivity.this,LoginActivity.class);
                setResult(RESULT_OK);
                startActivityForResult(intent,REQUEST_CODE_EDIT_PASSWORD);
                ((WzyxApplication)getApplication()).finishSingleActivity(EditPasswordActivity.this);
            }
            //失败回调
            @Override
            public void onRegisterFailure(String error) {
                LogUtil.d(TAG,error);
                btnConfirm.setEnabled(true);
                btnConfirm.setText(getResources().getString(R.string.confirm));
                ToastUtil.toastShort(EditPasswordActivity.this,getResources().getString(R.string.alter_password_failure));
                mCountDownTimer.cancel();
                tvSendCode.setEnabled(true);
                tvSendCode.setText(getResources().getString(R.string.send_code));
            }
        });
    }
    /**
     * 检查手机是否合法
     * @return TODO
     */
    private boolean checkPhoneNumber(String phoneNumber) {

        if(TextUtils.isEmpty(phoneNumber) || !RegexUtils.isMobileExact(phoneNumber)){
            ToastUtil.toastShort(EditPasswordActivity.this,getResources().getString(R.string.wrong_phone_number));
            return false;
        }
        return true;
    }
    /**
     * 检查密码是否合法
     * @return TODO
     */
    private boolean checkPassword(String password) {
        if(TextUtils.isEmpty(password) || !RegexUtils.isPassword(password)){
            return false;
        }
        return true;
    }
    /**
     * 检查验证码是否合法
     * @return TODO
     */
    private boolean checkAuthCode(String authCode) {
        if(TextUtils.isEmpty(authCode) || !RegexUtils.isAuthCode(authCode)){
            return false;
        }
        return true;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //应用销毁时取消定时器，以免发生内存泄露
        mCountDownTimer.cancel();
    }
}
