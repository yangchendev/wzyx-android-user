package com.allelink.wzyx.pay.alipay;

import android.app.Activity;
import android.os.AsyncTask;

import com.alipay.sdk.app.PayTask;
import com.allelink.wzyx.ui.loader.WzyxLoader;
import com.allelink.wzyx.utils.log.LogUtil;

/**
 * 支付异步请求（支付宝支付需要在新线程中执行）
 * @author yangc
 * @version 1.0
 * @date 2017/12/13
 * @email yangchendev@qq.com
 */
public class AliPayAsyncTask extends AsyncTask<String,Void,String> {
    private final Activity ACTIVITY;
    private final IAliPayResultListener LISTENER;

    public AliPayAsyncTask(Activity activity, IAliPayResultListener listener) {
        this.ACTIVITY = activity;
        this.LISTENER = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        //在子线程中进行支付
        final String aliPaySign = params[0];
        final PayTask payTask = new PayTask(ACTIVITY);
        return payTask.pay(aliPaySign,true);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        WzyxLoader.stopLoading();
        final PayResult payResult = new PayResult(result);
        final String resultInfo = payResult.getResult();
        final String resultStatus = payResult.getResultStatus();
        LogUtil.d("alipay_result",resultInfo);
        LogUtil.d("alipay_result_status",resultStatus);
        switch (resultStatus){
            case AliPayConstants.AL_PAY_STATUS_SUCCESS:
                if(LISTENER != null){
                    LISTENER.onPaySuccess();
                }
                break;
            case AliPayConstants.AL_PAY_STATUS_FAIL:
                if (LISTENER != null) {
                    LISTENER.onPayFail();
                }
                break;
            case AliPayConstants.AL_PAY_STATUS_PAYING:
                if (LISTENER != null) {
                    LISTENER.onPaying();
                }
                break;
            case AliPayConstants.AL_PAY_STATUS_CANCEL:
                if (LISTENER != null) {
                    LISTENER.onPayCancel();
                }
                break;
            case AliPayConstants.AL_PAY_STATUS_CONNECT_ERROR:
                if (LISTENER != null) {
                    LISTENER.onPayConnectError();
                }
                break;
            default:
                break;
        }
    }
}

