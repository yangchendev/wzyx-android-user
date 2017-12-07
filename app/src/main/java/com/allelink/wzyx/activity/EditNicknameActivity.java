package com.allelink.wzyx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.allelink.wzyx.R;
import com.allelink.wzyx.activity.base.BaseActivity;
import com.allelink.wzyx.app.WzyxApplication;
import com.allelink.wzyx.app.user.IUpdateUserInfoListener;
import com.allelink.wzyx.app.user.UpdateUserInfoHandler;
import com.allelink.wzyx.ui.TitleBar;
import com.allelink.wzyx.utils.storage.WzyxPreference;
import com.allelink.wzyx.utils.toast.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import qiu.niorgai.StatusBarCompat;

/**
 * 修改昵称
 * @author yangc
 * @date 2017/11/24
 * @version 1.0
 * @email 1048027353@qq.com
 */

public class EditNicknameActivity extends BaseActivity {
    private static final String TAG = "EditNicknameActivity";
    private String nickname = null;
    /**
    * UI
    */
    @BindView(R.id.tb_edit_nickname)
    TitleBar titleBar = null;
    @BindView(R.id.et_edit_nickname)
    EditText etNickname = null;
    @BindView(R.id.btn_edit_nickname_confirm)
    Button confirm = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nickname);
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
    * 初始化titleBar点击事件
    */
    private void initTitleBar() {
        titleBar.setOnTitleBarButtonClickListener(new TitleBar.onTitleBarButtonClickListener() {
            @Override
            public void onLeftClick() {
                EditNicknameActivity.this.finish();
            }

            @Override
            public void onRightClick() {

            }
        });
    }
    /**
    * 确定按钮点击事件
    */
    @OnClick(R.id.btn_edit_nickname_confirm)
    void confirm(){
        nickname = etNickname.getText().toString().trim();
        if(TextUtils.isEmpty(nickname)){
            ToastUtil.toastShort(EditNicknameActivity.this,getResources().getString(R.string.nickname_cannot_be_empty));
        }else{
            confirm.setText(getResources().getString(R.string.nickname_altering));
            confirm.setEnabled(false);
            //需要提交给服务器
            params.clear();
            params.put("nickname", nickname);
            params.put("phoneNumber", WzyxPreference.getCustomAppProfile("phoneNumber"));
            UpdateUserInfoHandler.updateUserInfoHandler(params, new IUpdateUserInfoListener() {
                @Override
                public void onUpdateUserInfoSuccess(String response) {
                    confirm.setText(getResources().getString(R.string.confirm));
                    confirm.setEnabled(true);
                    ToastUtil.toastShort(EditNicknameActivity.this,getResources().getString(R.string.alter_nickname_success));
                    Intent intent = new Intent();
                    intent.putExtra("nickname", nickname);
                    setResult(RESULT_OK,intent);
                    ((WzyxApplication)getApplication()).finishSingleActivity(EditNicknameActivity.this);
                }

                @Override
                public void onUpdateUserInfoFailure(String error) {
                    confirm.setText(getResources().getString(R.string.confirm));
                    confirm.setEnabled(true);
                    ToastUtil.toastShort(EditNicknameActivity.this,getResources().getString(R.string.alter_password_failure));
                }
            });
        }
    }
}
