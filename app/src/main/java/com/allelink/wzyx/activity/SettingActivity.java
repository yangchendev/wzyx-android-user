package com.allelink.wzyx.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;

import com.allelink.wzyx.R;
import com.allelink.wzyx.activity.base.BaseActivity;
import com.allelink.wzyx.app.AccountManager;
import com.allelink.wzyx.app.WzyxApplication;
import com.allelink.wzyx.ui.TitleBar;
import com.allelink.wzyx.utils.activity.ActivityUtils;
import com.allelink.wzyx.utils.log.LogUtil;
import com.allelink.wzyx.utils.storage.WzyxPreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置
 * @author yangc
 * @date 2017/12/5
 * @version 1.0
 * @email 1048027353@qq.com
 */

public class SettingActivity extends BaseActivity {
    private static final String TAG = "SettingActivity";
    private static final int REQUEST_LOGOUT_PASSWORD = 2002;
    /**
    * titleBar
    */
    @BindView(R.id.tb_setting_title_bar)
    TitleBar titleBar = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //隐藏actionBar
        ActionBar actionBar = getActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        //绑定ButterKnife
        ButterKnife.bind(this);
        initTitleBar();
    }
    /**
    * 初始化titleBar点击事件
    */
    private void initTitleBar() {
        titleBar.setOnTitleBarButtonClickListener(new TitleBar.onTitleBarButtonClickListener() {
            @Override
            public void onLeftClick() {
                ((WzyxApplication)getApplication()).finishSingleActivity(SettingActivity.this);
            }

            @Override
            public void onRightClick() {

            }
        });
    }

    /**
    * 账户设置点击事件
    */
    @OnClick(R.id.rl_setting_account_setting)
    void accountSetting(){
        LogUtil.d(TAG,"accountSetting");
        ActivityUtils.startActivity(SettingActivity.this,AccountSettingActivity.class);
    }
    /**
     * 安全设置点击事件
     */
    @OnClick(R.id.rl_setting_security_setting)
    void securitySetting(){
        LogUtil.d(TAG,"securitySetting");
    }
    /**
     * 通用设置点击事件
     */
    @OnClick(R.id.rl_setting_general)
    void generalSetting(){
        LogUtil.d(TAG,"generalSetting");
    }
    /**
     * 退出登录点击事件
     */
    @OnClick(R.id.btn_setting_logout)
    void logout(){
        LogUtil.d(TAG,"logout");
        //退出登录清除用户数据
        WzyxPreference.clearAppPreferences();
        AccountManager.setSignState(false);
        Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
        setResult(RESULT_OK);
        startActivityForResult(intent,REQUEST_LOGOUT_PASSWORD);
        ((WzyxApplication)getApplication()).finishSingleActivity(SettingActivity.this);
    }

}
