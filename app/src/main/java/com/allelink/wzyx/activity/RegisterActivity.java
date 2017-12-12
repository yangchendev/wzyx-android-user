package com.allelink.wzyx.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
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
import com.allelink.wzyx.utils.activity.ActivityUtils;
import com.allelink.wzyx.utils.encrypt.SHAUtil;
import com.allelink.wzyx.utils.log.LogUtil;
import com.allelink.wzyx.utils.regex.RegexUtils;
import com.allelink.wzyx.utils.storage.WzyxPreference;
import com.allelink.wzyx.utils.toast.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import qiu.niorgai.StatusBarCompat;

/**
 * @author yangc
 * @filename RegisterActivity.java
 * @date 2017/11/3
 * @version 1.0
 * @description 注册页
 * @email 1048027353@qq.com
 */

public class RegisterActivity extends BaseActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    /**
    * UI
    */
    @BindView(R.id.et_register_phone_number)
    TextInputEditText etPhoneNumber = null;
    @BindView(R.id.et_register_code)
    TextInputEditText etCode = null;
    @BindView(R.id.et_register_confirm_password)
    TextInputEditText etPassword = null;
    @BindView(R.id.btn_register_back)
    Button btnBack = null;
    @BindView(R.id.btn_register_register)
     Button btnRegister = null;
    @BindView(R.id.tv_register_send_code)
    AppCompatTextView tvSendCode = null;
    @BindView(R.id.til_register_phone_number_layout)
    TextInputLayout phoneNumberLayout = null;

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
            etPhoneNumber.setEnabled(false);
            etCode.setEnabled(true);
            tvSendCode.setText(millisUntilFinished/1000 + "秒后重发");
        }
        @Override
        public void onFinish() {
            tvSendCode.setEnabled(true);
            etCode.setEnabled(true);
            etPhoneNumber.setEnabled(true);
            tvSendCode.setText(getResources().getString(R.string.send_code));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        StatusBarCompat.setStatusBarColor(this,getResources().getColor(R.color.white));
        //隐藏actionBar
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        //绑定ButterKnife
        ButterKnife.bind(this);
    }
    /**
    * 返回
    */
    @OnClick(R.id.btn_register_back)
    void back(){
        LogUtil.d(TAG,"back");
        this.finish();
    }
    /**
    * 注册点击事件
    */
    @OnClick(R.id.btn_register_register)
    void register(){
        LogUtil.d(TAG,"register");
        mPassword = etPassword.getText().toString().trim();
        mAuthCode = etCode.getText().toString().trim();
        if(!checkAuthCode(mAuthCode)){
            ToastUtil.toastShort(RegisterActivity.this,getResources().getString(R.string.empty_auth_code));
            return;
        }
        if(!checkPassword(mPassword)){
            ToastUtil.toastShort(RegisterActivity.this,getResources().getString(R.string.wrong_password));
            return;
        }
        params.clear();
        params.put("phoneNumber", mPhoneNumber);
        params.put("password", SHAUtil.SHAEncode(mPassword));
        params.put("checkcode", mAuthCode);
        btnRegister.setText(getResources().getString(R.string.on_registering));
        btnRegister.setEnabled(false);
        etCode.setEnabled(false);
        etPassword.setEnabled(false);
        etPhoneNumber.setEnabled(false);
        RegisterHandler.register(params, new IRegisterListener() {
            @Override
            public void onRegisterSuccess(String response) {
                LogUtil.d(TAG,response);
                btnRegister.setEnabled(true);
                btnRegister.setText(getResources().getString(R.string.register));
                ToastUtil.toastShort(RegisterActivity.this,response);
                //注册成功后直接进入到主界面
                //设置已登录状态
                AccountManager.setSignState(true);
                //保存已注册的手机号
                WzyxPreference.addCustomAppProfile(WzyxPreference.KEY_PHONE_NUMBER,mPhoneNumber);
                ActivityUtils.startActivity(RegisterActivity.this,MainActivity.class);
                ((WzyxApplication)getApplication()).finishSingleActivity(RegisterActivity.this);
            }
            @Override
            public void onRegisterFailure(String error) {
                LogUtil.d(TAG,error);
                btnRegister.setEnabled(true);
                etCode.setEnabled(true);
                etPassword.setEnabled(true);
                etPhoneNumber.setEnabled(true);
                btnRegister.setText(getResources().getString(R.string.register));
                ToastUtil.toastShort(RegisterActivity.this,error);
                mCountDownTimer.cancel();
                tvSendCode.setEnabled(true);
                tvSendCode.setText(getResources().getString(R.string.send_code));
            }
        });
    }

    /**
    * 发送验证码
    */
    @OnClick(R.id.tv_register_send_code)
    void sendCode(){
        mPhoneNumber = etPhoneNumber.getText().toString().trim();
        if(!checkPhoneNumber(mPhoneNumber)){
            mCountDownTimer.cancel();
            tvSendCode.setEnabled(true);
            etPhoneNumber.setEnabled(true);
            etPhoneNumber.setEnabled(true);
            tvSendCode.setText(getResources().getString(R.string.send_code));
            return;
        }
        mCountDownTimer.start();
        params.clear();
        params.put("phoneNumber", mPhoneNumber);
        params.put("sign", RestConstants.CHECK_CODE_FOR_REGISTER);
        CheckCodeHandler.sendCheckCode(params, new ICheckCodeSendListener() {
            @Override
            public void onCheckCodeSendSuccess(String response) {
                etCode.setEnabled(true);
                ToastUtil.toastShort(RegisterActivity.this,getResources().getString(R.string.code_send_success));
            }

            @Override
            public void onCheckCodeSendFailure(String error) {
                ToastUtil.toastShort(RegisterActivity.this,error);
                //发送失败恢复按钮状态
                mCountDownTimer.cancel();
                tvSendCode.setEnabled(true);
                etCode.setEnabled(true);
                etPhoneNumber.setEnabled(true);
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
            ToastUtil.toastShort(RegisterActivity.this,getResources().getString(R.string.wrong_phone_number));
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
