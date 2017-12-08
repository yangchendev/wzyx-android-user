package com.allelink.wzyx.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;

import com.allelink.wzyx.R;
import com.allelink.wzyx.activity.base.BaseActivity;
import com.allelink.wzyx.app.AccountManager;
import com.allelink.wzyx.app.IUserChecker;
import com.allelink.wzyx.utils.activity.ActivityUtils;
import com.allelink.wzyx.utils.toast.ToastUtil;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * @author yangc
 * @filename SplashActivity.java
 * @date 2017/11/3
 * @version 1.0
 * @description 启动页,用于检查更新
 * @email 1048027353@qq.com
 */
@RuntimePermissions
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
        //检查定位权限
        startLocateWithCheck();
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

    /**
    * 申请定位权限
    */
    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    void startLocate(){

    }
    /**
    * 检查定位权限
    */
    private void startLocateWithCheck(){
        SplashActivityPermissionsDispatcher.startLocateWithPermissionCheck(this);
    }
    /**
    * 拒绝定位权限
    */
    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    void onLocateDenied(){
        ToastUtil.toastShort(this,getResources().getString(R.string.locate_permission_denied));
    }
    /**
    * 永久拒绝定位权限
    */
    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    void neverAskLocate(){
        ToastUtil.toastShort(this,getResources().getString(R.string.locate_permission_never_ask));
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
    }
}
