package com.allelink.wzyx.app.order;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.allelink.wzyx.model.OrderItem;
import com.allelink.wzyx.net.RestConstants;
import com.allelink.wzyx.net.RestCreator;
import com.allelink.wzyx.utils.log.LogUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 活动报名处理类
 * @author yangc
 * @version 1.0
 * @date 2017/12/9
 * @email yangchendev@qq.com
 */
public class OrderHandler {
    private static final String TAG = OrderHandler.class.getSimpleName();
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";

    /**
     * 报名活动
     * @param params 请求参数 （用户Id,商家Id,活动Id）
     * @param listener 回调接口
     */
    public static void apply(HashMap<String,Object> params,final IOrderListener listener){
        //将请求参数转换成json格式
        String jsonString = JSON.toJSONString(params);
        LogUtil.json(TAG,jsonString);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonString);
        //post 提交json格式的数据
        Call<String> call = RestCreator.getRestService().postRaw(RestConstants.APPLY_ACTIVITY_URL, body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //网络请求成功的回调
                String responseString = response.body();
                String result;
                String message;
                String orderId;
                if(!TextUtils.isEmpty(responseString) && response.isSuccessful()){
                    LogUtil.json(TAG,responseString);
                    JSONObject jsonObject = JSON.parseObject(responseString);
                    result = jsonObject.getString("result");
                    message = jsonObject.getString("message");
                    if(SUCCESS.equals(result)){
                        if(listener != null){
                            orderId = jsonObject.getJSONObject("data").getString("orderId");
                            listener.onSuccess(orderId);
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

    /**
     * 获取未付款订单集合
     * @param params 请求参数 （用户Id,订单状态）
     * @param listener 回调接口
     */
    public static void getOrderList(HashMap<String,Object> params,final IGetOrderListListener listener){
        //将请求参数转换成json格式
        String jsonString = JSON.toJSONString(params);
        LogUtil.json(TAG,jsonString);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonString);
        //post 提交json格式的数据
        Call<String> call = RestCreator.getRestService().postRaw(RestConstants.GET_ORDER_LIST_URL, body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //网络请求成功的回调
                String responseString = response.body();
                String result;
                String message;
                List<OrderItem> orderItems = null;
                if(!TextUtils.isEmpty(responseString) && response.isSuccessful()){
                    LogUtil.json(TAG,responseString);
                    JSONObject jsonObject = JSON.parseObject(responseString);
                    result = jsonObject.getString("result");
                    message = jsonObject.getString("message");
                    if(SUCCESS.equals(result)){
                        //得到未付款订单集合
                        orderItems = jsonObject.getJSONArray("data").toJavaList(OrderItem.class);
                        if(listener != null){
                            listener.onSuccess(orderItems);
                        }
                    }else if(ERROR.equals(result)){
                        if(listener != null){
                            listener.onFailure(message);
                        }
                    }
                }else{
                    try {
                        if (listener != null) {
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


    /**
     * 删除订单
     * @param params 请求参数 （用户Id,订单Id）
     * @param listener 回调接口
     */
    public static void delete(HashMap<String,Object> params,final IOrderListener listener){
        //将请求参数转换成json格式
        String jsonString = JSON.toJSONString(params);
        LogUtil.json(TAG,jsonString);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonString);
        //post 提交json格式的数据
        Call<String> call = RestCreator.getRestService().postRaw(RestConstants.DELETE_ORDER_URL, body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //网络请求成功的回调
                String responseString = response.body();
                String result;
                String message;
                String orderId;
                LogUtil.json(TAG,responseString);
                if(!TextUtils.isEmpty(responseString) && response.isSuccessful()){
                    JSONObject jsonObject = JSON.parseObject(responseString);
                    result = jsonObject.getString("result");
                    message = jsonObject.getString("message");
                    if(SUCCESS.equals(result)){
                        if(listener != null){
                            orderId = jsonObject.getString("data");
                            listener.onSuccess(orderId);
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
    /**
     * 获取支付宝订单信息
     * @param params 请求参数 （订单Id，商品名，总金额）
     * @param listener 回调接口
     */
    public static void getAliPayOrderInfo(HashMap<String,Object> params,final IAliPayOrderInfoListener listener){
        //将请求参数转换成json格式
        String jsonString = JSON.toJSONString(params);
        LogUtil.json(TAG,jsonString);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonString);
        //post 提交json格式的数据
        Call<String> call = RestCreator.getRestService().postRaw(RestConstants.ALI_PAY_GET_ORDER_STRING_URL, body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //网络请求成功的回调
                String responseString = response.body();
                String result;
                String message;
                String orderInfo;
                LogUtil.json(TAG,responseString);
                if(!TextUtils.isEmpty(responseString) && response.isSuccessful()){
                    JSONObject jsonObject = JSON.parseObject(responseString);
                    result = jsonObject.getString("result");
                    message = jsonObject.getString("message");
                    if(SUCCESS.equals(result)){
                        if(listener != null){
                            orderInfo = jsonObject.getString("data");
                            listener.onSuccess(orderInfo);
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
