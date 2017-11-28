package com.allelink.wzyx.activity.base;

import android.os.Bundle;

import com.allelink.wzyx.app.WzyxApplication;

import java.util.HashMap;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * @author yangc
 * @filename BaseActivity.java
 * @date 2017/11/3
 * @version 1.0
 * @description 基类activity
 * @email 1048027353@qq.com
 */

public class BaseActivity extends SupportActivity {
    protected HashMap<String, Object> params = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((WzyxApplication)getApplication()).addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((WzyxApplication)getApplication()).finishSingleActivity(this);
    }
}
