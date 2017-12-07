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
import qiu.niorgai.StatusBarCompat;

/**
 * 设置
 * @author yangc
 * @date 2017/12/5
 * @version 1.0
 * @email 1048027353@qq.com
 */

public class SettingActivity extends BaseActivity {
    private static final String TAG = "SettingActivity";
    private static final int REQUEST_CODE_LOGOUT = 2002;
    /**
    * titleBar
    */
    @BindView(R.id.tb_setting_title_bar)
    TitleBar titleBar = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        StatusBarCompat.setStatusBarColor(this,getResources().getColor(R.color.brands_color));
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
        ActivityUtils.startActivity(SettingActivity.this,SecuritySettingActivity.class);
    }
    /**
     * 清除缓存点击事件
     */
    @OnClick(R.id.rl_setting_clear_cache)
    void clearCache(){
        LogUtil.d(TAG,"clearCache");
        //1.获取缓存大小，并显示在textview上
        //2.弹出对话框提示用户是否要清除缓存
        //3.清除缓存
    }
    /**
     * 检查更新点击事件
     */
    @OnClick(R.id.rl_setting_check_update)
    void checkUpdate(){
        LogUtil.d(TAG,"checkUpdate");
        //1.从服务器获取apk版本号
        //2.与本地版本号进行比较，如果小于服务器端的则进行下载安装

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
        //退出当前activity到登录activity并退出其他activity
        Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
        setResult(RESULT_OK);
        startActivityForResult(intent,REQUEST_CODE_LOGOUT);
        ((WzyxApplication)getApplication()).finishSingleActivity(SettingActivity.this);
    }
}
