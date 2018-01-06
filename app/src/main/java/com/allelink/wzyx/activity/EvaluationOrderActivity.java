package com.allelink.wzyx.activity;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.widget.RatingBar;

import com.allelink.wzyx.R;
import com.allelink.wzyx.app.order.IOrderListener;
import com.allelink.wzyx.app.order.OrderHandler;
import com.allelink.wzyx.fragment.main.order.OrderFragment;
import com.allelink.wzyx.ui.loader.WzyxLoader;
import com.allelink.wzyx.utils.storage.WzyxPreference;
import com.allelink.wzyx.utils.toast.ToastUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EvaluationOrderActivity extends AppCompatActivity {

    private String orderId = null;
    private String activityId = null;
    private int evaluateLevel = 5;
    private static final String ORDER_ID = "orderId";
    private static final String ACTIVITY_ID = "activityId";

    @BindView(R.id.et_order_evaluate)
    AppCompatEditText editEvaluateOrder;
    @BindView(R.id.btn_order_evaluate)
    AppCompatButton btnEvaluateOrder;
    @BindView(R.id.rb_evaluateLevel)
    RatingBar rbEvaluateLevel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderevaluate);
        android.app.ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        Intent intent = getIntent();
        orderId = intent.getStringExtra(ORDER_ID);
        activityId = intent.getStringExtra(ACTIVITY_ID);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_order_evaluate)
    void onEvaluateOrder() {
        String text = editEvaluateOrder.getText().toString();
        HashMap<String, Object> params = new HashMap<>();
        if (text != null) {
            evaluateLevel = (int) rbEvaluateLevel.getRating();
            //TODO 将评价内容提交给服务器
            btnEvaluateOrder.setEnabled(true);
            params.clear();
            WzyxLoader.showLoading(this);
            params.put(ACTIVITY_ID, activityId);
            //添加订单号
            params.put("orderId", orderId);
            //添加评价的星级
            params.put("evaluateLevel", evaluateLevel);
            params.put("evaluateContent", text);
            params.put("userId", WzyxPreference.getCustomAppProfile("userId"));
            OrderHandler.evaluateOrderInfo(params, new IOrderListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onSuccess(String orderId) {
                    WzyxLoader.stopLoading();
                    ToastUtil.toastShort(EvaluationOrderActivity.this, "评论提交成功");
                   // Intent intent=new Intent(EvaluationOrderActivity.this,OrderFragment.class);
                  //  startActivity(intent);
                    editEvaluateOrder.setTag("");
                    rbEvaluateLevel.setRating(0);
                    //切换到订单fragment
                    android.support.v4.app.FragmentManager manager=getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction transaction=manager.beginTransaction();
                    transaction.replace(R.layout.activity_orderevaluate,new OrderFragment());
                    transaction.commit();
                }

                @Override
                public void onFailure(String errorMessage) {
                    WzyxLoader.stopLoading();
                    ToastUtil.toastShort(EvaluationOrderActivity.this, errorMessage);
                }
            });
        } else {
            ToastUtil.toastShort(EvaluationOrderActivity.this, "评论内容不能为空");
        }
    }
}
