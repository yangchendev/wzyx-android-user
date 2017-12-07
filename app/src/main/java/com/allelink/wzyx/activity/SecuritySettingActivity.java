package com.allelink.wzyx.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;

import com.allelink.wzyx.R;
import com.allelink.wzyx.activity.base.BaseActivity;
import com.allelink.wzyx.app.WzyxApplication;
import com.allelink.wzyx.ui.TitleBar;
import com.allelink.wzyx.utils.activity.ActivityUtils;
import com.allelink.wzyx.utils.log.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import qiu.niorgai.StatusBarCompat;

/**
 * 安全设置
 * @author yangc
 * @date 2017/12/5
 * @version 1.0
 * @email 1048027353@qq.com
 */

public class SecuritySettingActivity extends BaseActivity {
    private static final String TAG = "SecuritySettingActivity";
    /**
    * UI
    */
    @BindView(R.id.tb_security_setting_title_bar)
    TitleBar titleBar = null;
    @BindView(R.id.tv_security_setting_real_name_certification)
    AppCompatTextView tvCertification = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_setting);
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
                ((WzyxApplication)getApplication()).finishSingleActivity(SecuritySettingActivity.this);
            }

            @Override
            public void onRightClick() {

            }
        });
    }

    /**
     * 实名认证点击事件
     */
    @OnClick(R.id.rl_security_setting_real_name_certification)
    void certification(){
        LogUtil.d(TAG,"certification");
    }
    /**
     * 修改密码点击事件
     */
    @OnClick(R.id.rl_security_setting_login_password)
    void alterPassword(){
        LogUtil.d(TAG,"alterPassword");
        ActivityUtils.startActivity(SecuritySettingActivity.this,EditPasswordActivity.class);
    }

}
