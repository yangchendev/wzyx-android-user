package com.allelink.wzyx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.allelink.wzyx.R;
import com.allelink.wzyx.activity.base.BaseActivity;
import com.allelink.wzyx.adapter.EvaluateActivityAdapter;
import com.allelink.wzyx.app.WzyxApplication;
import com.allelink.wzyx.app.comment.ICommentListener;
import com.allelink.wzyx.app.comment.commentHandler;
import com.allelink.wzyx.model.EvaluateListItem;
import com.allelink.wzyx.ui.TitleBar;
import com.allelink.wzyx.ui.loader.WzyxLoader;
import com.allelink.wzyx.utils.toast.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import qiu.niorgai.StatusBarCompat;

public class ActivityEvaluateActivity extends BaseActivity {

    private static final String ACTIVITY_ID = "activityId";
    String activityId;
    List<EvaluateListItem> evaluateList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_list);
        StatusBarCompat.setStatusBarColor(this,getResources().getColor(R.color.white));
        //隐藏actionBar
        android.app.ActionBar actionBar = getActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        Intent intent = getIntent();
        activityId = intent.getStringExtra(ACTIVITY_ID);
        initData();
        initTitleBar();
    }

    private void initTitleBar() {
        TitleBar titleBar = findViewById(R.id.tb_evaluate_activity);
        titleBar.setOnTitleBarButtonClickListener(new TitleBar.onTitleBarButtonClickListener() {
            @Override
            public void onLeftClick() {
                ((WzyxApplication)getApplication()).finishSingleActivity(ActivityEvaluateActivity.this);
            }

            @Override
            public void onRightClick() {

            }
        });
    }

    private void initData() {
        params.clear();
        WzyxLoader.showLoading(this);
        params.put(ACTIVITY_ID, activityId);
        commentHandler.activityEvaluate(params, new ICommentListener() {
            @Override
            public void onSuccess(String evaluateListStr) {
                WzyxLoader.stopLoading();
                ArrayList<EvaluateListItem> evaluates = JSON.parseObject(evaluateListStr,
                        new TypeReference<ArrayList<EvaluateListItem>>() {
                        });
                evaluateList = evaluates;
                bindAdapter();
            }


            @Override
            public void onFailure(String errorMessage) {
                WzyxLoader.stopLoading();
                ToastUtil.toastShort(ActivityEvaluateActivity.this, errorMessage);
            }
        });
    }


    private void bindAdapter() {
        RecyclerView recyclerView = findViewById(R.id.rv_activity_evaluate_list);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        EvaluateActivityAdapter adapter = new EvaluateActivityAdapter(evaluateList);
        recyclerView.setAdapter(adapter);
    }


}
