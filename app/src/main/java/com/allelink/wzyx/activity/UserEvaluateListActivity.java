package com.allelink.wzyx.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.allelink.wzyx.R;
import com.allelink.wzyx.activity.base.BaseActivity;
import com.allelink.wzyx.adapter.EvaluateActivityAdapter;
import com.allelink.wzyx.adapter.UserEvaluateAdapter;
import com.allelink.wzyx.app.comment.ICommentListener;
import com.allelink.wzyx.app.comment.commentHandler;
import com.allelink.wzyx.model.EvaluateListItem;
import com.allelink.wzyx.model.UserEvaluateItem;
import com.allelink.wzyx.ui.loader.WzyxLoader;
import com.allelink.wzyx.utils.storage.WzyxPreference;
import com.allelink.wzyx.utils.toast.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class UserEvaluateListActivity extends BaseActivity {

    private List<UserEvaluateItem> evaluateList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_evaluate_list);
        initData();
    }

    private void initData() {
        params.clear();
        WzyxLoader.showLoading(this);
        params.put("userId", WzyxPreference.getCustomAppProfile("userId") );
        commentHandler.userEvaluate(params, new ICommentListener() {
            @Override
            public void onSuccess(String evaluateListStr) {
                WzyxLoader.stopLoading();
                ArrayList<UserEvaluateItem> evaluates = JSON.parseObject(evaluateListStr,
                        new TypeReference<ArrayList<UserEvaluateItem>>() {
                        });
                evaluateList = evaluates;
                bindAdapter();
            }


            @Override
            public void onFailure(String errorMessage) {
                WzyxLoader.stopLoading();
                ToastUtil.toastShort(UserEvaluateListActivity.this, errorMessage);
            }
        });
    }


    private void bindAdapter() {
        RecyclerView recyclerView = findViewById(R.id.rv_user_evaluate_list);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        UserEvaluateAdapter adapter = new UserEvaluateAdapter(evaluateList);
        recyclerView.setAdapter(adapter);
    }

}
