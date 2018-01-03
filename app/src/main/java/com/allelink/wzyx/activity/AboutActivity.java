package com.allelink.wzyx.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.allelink.wzyx.R;
import com.allelink.wzyx.activity.base.BaseActivity;
import com.allelink.wzyx.app.WzyxApplication;
import com.allelink.wzyx.ui.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import qiu.niorgai.StatusBarCompat;

/**
 * 关于万众艺兴页面
 * @author yangc
 * @date 2018/1/3
 * @version 1.0
 * @email yangchendev@qq.com
 */

public class AboutActivity extends BaseActivity {
    private static final String TAG = AboutActivity.class.getSimpleName();
    @BindView(R.id.tb_activity_about)
    TitleBar titleBar = null;
    @BindView(R.id.tv_activity_about_version)
    TextView tvVersion = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        StatusBarCompat.setStatusBarColor(this,getResources().getColor(R.color.white));
        //隐藏actionBar
        android.app.ActionBar actionBar = getActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        //绑定ButterKnife
        ButterKnife.bind(this);
        //初始化titleBar
        initTitleBar();
        //初始化版本号
        initData();
    }
    /**
    * 初始化版本号
    */
    private void initData() {
        String versionName = "";
        versionName = getVersionName();
        tvVersion.setText(versionName);
    }

    /**
     * 获取应用版本名称
     * @return 版本名称
     */
    private String getVersionName() {
        String versionName = "";
        try {
            versionName = this.getPackageManager().getPackageInfo(getPackageName(),0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
    * 初始化titleBar
    */
    private void initTitleBar() {
        titleBar.setOnTitleBarButtonClickListener(new TitleBar.onTitleBarButtonClickListener() {
            @Override
            public void onLeftClick() {
                ((WzyxApplication)getApplication()).finishSingleActivity(AboutActivity.this);
            }

            @Override
            public void onRightClick() {
            }
        });
    }
}
