package com.allelink.wzyx.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;

import com.allelink.wzyx.R;
import com.allelink.wzyx.activity.base.BaseActivity;
import com.allelink.wzyx.app.AccountManager;
import com.allelink.wzyx.app.IUserChecker;
import com.allelink.wzyx.utils.activity.ActivityUtils;

/**
 * @author yangc
 * @filename SplashActivity.java
 * @date 2017/11/3
 * @version 1.0
 * @description 启动页,用于检查更新
 * @email 1048027353@qq.com
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //隐藏actionBar
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        //2秒后跳转到登陆界面
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //检查用户是否已登录
                AccountManager.checkAccount(new IUserChecker() {
                    @Override
                    public void onSignIn() {
                        //已经登录过
                        ActivityUtils.startActivity(SplashActivity.this, MainActivity.class);
                        SplashActivity.this.finish();
                    }
                    @Override
                    public void onNotSignIn() {
                        //还未登录过
                        ActivityUtils.startActivity(SplashActivity.this,LoginActivity.class);
                        SplashActivity.this.finish();
                    }
                });
            }
        },2000);
    }
}
