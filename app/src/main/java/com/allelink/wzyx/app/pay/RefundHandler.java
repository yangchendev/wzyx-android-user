package com.allelink.wzyx.app.pay;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.allelink.wzyx.net.RestConstants;
import com.allelink.wzyx.net.RestCreator;
import com.allelink.wzyx.utils.log.LogUtil;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 退款处理类
 * @author yangc
 * @version 1.0
 * @date 2017/12/22
 * @email yangchendev@qq.com
 */
public class RefundHandler {

    private static final String TAG = RefundHandler.class.getSimpleName();
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";

    /**
     * 申请退款
     * @param params 请求参数 （订单号，退款理由）
     * @param listener 回调接口
     */
    public static void applyRefund(HashMap<String,Object> params, final IRefundListener listener){
        //将请求参数转换成json格式
        String jsonString = JSON.toJSONString(params);
        LogUtil.json(TAG,jsonString);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonString);
        //post 提交json格式的数据
        Call<String> call = RestCreator.getRestService().postRaw(RestConstants.APPLY_REFUND_URL, body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //网络请求成功的回调
                String responseString = response.body();
                String result;
                String message;
                LogUtil.json(TAG,responseString);
                if(!TextUtils.isEmpty(responseString) && response.isSuccessful()){
                    JSONObject jsonObject = JSON.parseObject(responseString);
                    result = jsonObject.getString("result");
                    message = jsonObject.getString("message");
                    if(SUCCESS.equals(result)){
                        if(listener != null){
                            listener.onSuccess();
                        }
                    }else if(ERROR.equals(result)){
                        if(listener != null){
                            listener.onFailure(message);
                        }
                    }
                }else{
                    try {
                        if(listener != null){
                            listener.onFailure(response.errorBody().string());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if(listener != null){
                    listener.onFailure(t.getMessage());
                }
            }
        });
    }
}
