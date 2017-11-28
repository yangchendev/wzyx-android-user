package com.allelink.wzyx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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

/**
 * 修改性别
 * @author yangc
 * @date 2017/11/24
 * @version 1.0
 * @email 1048027353@qq.com
 */

public class EditGenderActivity extends BaseActivity {
    private static final String TAG = "EditGenderActivity";
    private static final String MALE = "男";
    private static final String FEMALE = "女";
    private static final String SECRET = "保密";
    private static final int CODE_MALE = 0;
    private static final int CODE_FEMALE = 1;
    private static final int CODE_SECRET = 2;
    private int mGenderCode = 0;
    private String gender = null;
    /**
    * UI
    */
    @BindView(R.id.iv_edit_gender_male)
    ImageView ivMaleSelect = null;
    @BindView(R.id.iv_edit_gender_female)
    ImageView ivFemaleSelect = null;
    @BindView(R.id.iv_edit_gender_secret)
    ImageView ivSecretSelect = null;
    @BindView(R.id.tb_edit_gender)
    TitleBar titleBar = null;
    @BindView(R.id.btn_edit_gender_confirm)
    Button confirm = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gender);
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
    * 初始化titlebar点击事件
    */
    private void initTitleBar() {
        titleBar.setOnTitleBarButtonClickListener(new TitleBar.onTitleBarButtonClickListener() {
            @Override
            public void onLeftClick() {
                EditGenderActivity.this.finish();
            }

            @Override
            public void onRightClick() {

            }
        });
    }

    /**
    * 点击了男性
    */
    @OnClick(R.id.ll_edit_gender_male)
    void male(){
        ivMaleSelect.setVisibility(View.VISIBLE);
        ivFemaleSelect.setVisibility(View.INVISIBLE);
        ivSecretSelect.setVisibility(View.INVISIBLE);
    }
    /**
     * 点击了女性
     */
    @OnClick(R.id.ll_edit_gender_female)
    void female(){
        ivFemaleSelect.setVisibility(View.VISIBLE);
        ivMaleSelect.setVisibility(View.INVISIBLE);
        ivSecretSelect.setVisibility(View.INVISIBLE);
    }
    /**
     * 点击了保密
     */
    @OnClick(R.id.ll_edit_gender_secret)
    void secret(){
        ivSecretSelect.setVisibility(View.VISIBLE);
        ivMaleSelect.setVisibility(View.INVISIBLE);
        ivFemaleSelect.setVisibility(View.INVISIBLE);
    }
    /**
    * 确认按钮
    */
    @OnClick(R.id.btn_edit_gender_confirm)
    void confirm(){
        //需要提交到服务器
        if(ivMaleSelect.getVisibility() == View.VISIBLE){
            gender = MALE;
            mGenderCode = CODE_MALE;
        }else if(ivFemaleSelect.getVisibility() == View.VISIBLE){
            gender = FEMALE;
            mGenderCode = CODE_FEMALE;
        }else if(ivSecretSelect.getVisibility() == View.VISIBLE){
            gender = SECRET;
            mGenderCode = CODE_SECRET;
        }
        confirm.setEnabled(false);
        confirm.setText(getResources().getString(R.string.gender_altering));
        params.clear();
        params.put("gender", mGenderCode);
        params.put("phoneNumber", WzyxPreference.getCustomAppProfile("phoneNumber"));
        UpdateUserInfoHandler.updateUserInfoHandler(params, new IUpdateUserInfoListener() {
            @Override
            public void onUpdateUserInfoSuccess(String response) {
                confirm.setEnabled(true);
                confirm.setText(getResources().getString(R.string.confirm));
                ToastUtil.toastShort(EditGenderActivity.this,getResources().getString(R.string.alter_gender_success));
                Intent intent = new Intent();
                intent.putExtra("gender", gender);
                setResult(RESULT_OK,intent);
                ((WzyxApplication)getApplication()).finishSingleActivity(EditGenderActivity.this);
            }

            @Override
            public void onUpdateUserInfoFailure(String error) {
                confirm.setEnabled(true);
                confirm.setText(getResources().getString(R.string.confirm));
                ToastUtil.toastShort(EditGenderActivity.this,getResources().getString(R.string.alter_gender_failure));
            }
        });

    }
}
