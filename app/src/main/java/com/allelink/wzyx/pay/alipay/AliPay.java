package com.allelink.wzyx.pay.alipay;

import android.app.Activity;
import android.os.AsyncTask;

import com.allelink.wzyx.R;
import com.allelink.wzyx.app.order.IAliPayOrderInfoListener;
import com.allelink.wzyx.app.order.OrderHandler;
import com.allelink.wzyx.ui.loader.WzyxLoader;
import com.allelink.wzyx.utils.log.LogUtil;
import com.allelink.wzyx.utils.toast.ToastUtil;

import java.util.HashMap;

/**
 * 支付宝支付
 * @author yangc
 * @version 1.0
 * @date 2017/12/13
 * @email yangchendev@qq.com
 */
public class AliPay {
    private IAliPayResultListener mIAliPayResultListener = null;
    private Activity mActivity = null;
    /**
    * 订单ID
    */
    private String mOrderId = null;
    /**
    * 商品的标题/交易标题/订单标题/订单关键字
    */
    private String mSubject = null;
    /**
    * 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
    */
    private String mTotalAmount = null;

    private AliPay(Activity activity){
        this.mActivity = activity;
    }
    /**
    * 简单工厂模式
    */
    public static AliPay create(Activity activity){
        return new AliPay(activity);
    }
    /**
    * 设置支付结果监听器
    */
    public AliPay setPayResultListener(IAliPayResultListener listener){
        this.mIAliPayResultListener = listener;
        return this;
    }
    /**
    * 设置订单号
    */
    public AliPay setOrderId(String orderId){
        this.mOrderId = orderId;
        return this;
    }
    /**
    * 设置商品的标题/交易标题/订单标题/订单关键字
    */
    public AliPay setSubject(String subject){
        this.mSubject = subject;
        return this;
    }
    /**
    * 设置订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
    */
    public AliPay setTotalAmount(String totalAmount){
        this.mTotalAmount = totalAmount;
        return this;
    }
    /**
    * 调用支付宝支付
    */
    public void aliPay(){
        //1.通过服务器获取签名后的订单信息
        WzyxLoader.showLoading(mActivity);
        HashMap<String, Object> params = new HashMap<>();
        params.put("orderIdStr", mOrderId);
        params.put("subject", mSubject);
        params.put("totalAmount", mTotalAmount);
        OrderHandler.getAliPayOrderInfo(params, new IAliPayOrderInfoListener() {
            @Override
            public void onSuccess(String orderInfo) {
                WzyxLoader.stopLoading();
                //2.获取成功后发起支付宝支付
                LogUtil.json("orderInfo",orderInfo);
                final AliPayAsyncTask aliPayAsyncTask = new AliPayAsyncTask(mActivity, mIAliPayResultListener);
                aliPayAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, orderInfo);
            }
            @Override
            public void onFailure(String errorMessage) {
                WzyxLoader.stopLoading();
                ToastUtil.toastShort(mActivity,mActivity.getResources().getString(R.string.get_order_info_failure));
            }
        });

    }

}
