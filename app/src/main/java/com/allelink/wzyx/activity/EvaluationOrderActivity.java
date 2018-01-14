package com.allelink.wzyx.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.widget.RatingBar;

import com.allelink.wzyx.R;
import com.allelink.wzyx.app.WzyxApplication;
import com.allelink.wzyx.app.order.IOrderListener;
import com.allelink.wzyx.app.order.OrderHandler;
import com.allelink.wzyx.ui.TitleBar;
import com.allelink.wzyx.ui.loader.WzyxLoader;
import com.allelink.wzyx.utils.storage.WzyxPreference;
import com.allelink.wzyx.utils.toast.ToastUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import qiu.niorgai.StatusBarCompat;

public class EvaluationOrderActivity extends AppCompatActivity {

    private String orderId = null;
    private String activityId = null;
    private int evaluateLevel = 5;
    private static final String ORDER_ID = "orderId";
    private static final String ACTIVITY_ID = "activityId";
    private static final int REQUEST_CODE_PAY_SUCCESS = 9000;

    @BindView(R.id.et_order_evaluate)
    AppCompatEditText editEvaluateOrder;
    @BindView(R.id.btn_order_evaluate)
    AppCompatButton btnEvaluateOrder;
    @BindView(R.id.rb_evaluateLevel)
    RatingBar rbEvaluateLevel;
    @BindView(R.id.tv_evaluate_level_hint)
    AppCompatTextView textLevelHint;
    @BindView(R.id.tb_evaluate_order_activity)
    TitleBar titleBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderevaluate);
        StatusBarCompat.setStatusBarColor(this,getResources().getColor(R.color.white));
        android.app.ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        Intent intent = getIntent();
        orderId = intent.getStringExtra(ORDER_ID);
        activityId = intent.getStringExtra(ACTIVITY_ID);
        ButterKnife.bind(this);
        initTitleBar();
        rbEvaluateLevel.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
           @Override
           public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
               switch((int) rating){
                   case 1:textLevelHint.setText("很差");break;
                   case 2:textLevelHint.setText("差");break;
                   case 3:textLevelHint.setText("好");break;
                   case 4:textLevelHint.setText("很好");break;
                   case 5:textLevelHint.setText("非常好");break;
                   default:textLevelHint.setText("请选择评价等级");break;
               }
           }
       });
    }

    private void initTitleBar() {
        titleBar.setOnTitleBarButtonClickListener(new TitleBar.onTitleBarButtonClickListener() {
            @Override
            public void onLeftClick() {
                ((WzyxApplication)getApplication()).finishSingleActivity(EvaluationOrderActivity.this);
            }

            @Override
            public void onRightClick() {

            }
        });
    }

    @OnClick(R.id.btn_order_evaluate)
    void onEvaluateOrder() {
        String text = editEvaluateOrder.getText().toString();
        HashMap<String, Object> params = new HashMap<>();
        if (!text.equals("")&&!text.isEmpty()) {
            evaluateLevel = (int) rbEvaluateLevel.getRating();
            //将评价内容提交给服务器
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
                    editEvaluateOrder.setText("");
                    rbEvaluateLevel.setRating(0);
                    //切换到订单fragment
                    Intent intent = new Intent(EvaluationOrderActivity.this, MainActivity.class);
                    intent.putExtra("pay_success", REQUEST_CODE_PAY_SUCCESS);
                    startActivity(intent);
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
