package com.allelink.wzyx.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.allelink.wzyx.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayOrderActivity extends AppCompatActivity {

    //微信支付
    @BindView(R.id.rb_wechat_pay)
    RadioButton rbWechatPay = null;
    //支付宝支付
    @BindView(R.id.rb_alipay)
    RadioButton rbAliPay = null;
    //确认支付按钮
    @BindView(R.id.btn_pay_order)
    Button btnPayOrder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payorder);
        //隐藏actionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //绑定ButterKnife
        ButterKnife.bind(this);
        //设置自定义button图标
        rbWechatPay.setButtonDrawable(R.drawable.btn_payorder_selector);
        rbAliPay.setButtonDrawable(R.drawable.btn_payorder_selector);
    }

    @OnClick(R.id.rb_wechat_pay)
    void selectWechatPay() {
        rbWechatPay.setChecked(true);
        rbAliPay.setChecked(false);
    }

    @OnClick(R.id.rb_alipay)
    void selectAliPay() {
        rbWechatPay.setChecked(false);
        rbAliPay.setChecked(true);
    }
    //TODO  请杨大佬协助一下
    @OnClick(R.id.btn_pay_order)
    void payOrder()
    {
        //微信支付
        if(rbWechatPay.isChecked()){
            Toast.makeText(this,"微信支付",Toast.LENGTH_SHORT).show();
        }
        //支付宝支付
        if(rbAliPay.isChecked())
        {
            Toast.makeText(this,"支付包支付",Toast.LENGTH_SHORT).show();
        }
    }

}
