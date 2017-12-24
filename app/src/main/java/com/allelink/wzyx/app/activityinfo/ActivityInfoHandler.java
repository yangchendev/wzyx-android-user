package com.allelink.wzyx.app.activityinfo;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.allelink.wzyx.model.ActivityDetailItem;
import com.allelink.wzyx.model.ActivityItem;
import com.allelink.wzyx.net.RestConstants;
import com.allelink.wzyx.net.RestCreator;
import com.allelink.wzyx.utils.log.LogUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 默认活动获取处理类
 * @author yangc
 * @version 1.0
 * @date 2017/12/8
 * @email 1048027353@qq.com
 */
public class ActivityInfoHandler {
    private static final String TAG = ActivityInfoHandler.class.getSimpleName();
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";

    /**
     * 获取默认活动列表信息
     * @param params 请求参数 经纬度
     * @param listener 接口
     * @param url 请求地址
     */
    public static void getActivityList(HashMap<String,Object> params,String url,final GetActivityInfoListener listener){
        //将请求参数转换成json格式
        String jsonString = JSON.toJSONString(params);
        LogUtil.json(TAG,jsonString);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonString);
        //post 提交json格式的数据
        Call<String> call = RestCreator.getRestService().postRaw(url, body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //网络请求成功的回调
                String responseString = response.body();
                String result;
                String message;
                List<ActivityItem> activityItems = new ArrayList<>();
                LogUtil.json(TAG,responseString);
                if(!TextUtils.isEmpty(responseString) && response.isSuccessful()){
                    JSONObject jsonObject = JSON.parseObject(responseString);
                    result = jsonObject.getString("result");
                    message = jsonObject.getString("message");
                    if(SUCCESS.equals(result)){
                        if(jsonObject.getJSONArray("data") != null){
                            activityItems = jsonObject.getJSONArray("data").toJavaList(ActivityItem.class);
                        }
                        if(listener != null && activityItems != null){
                            listener.onSuccess(activityItems);
                        }
                    }else if(ERROR.equals(result)){
                        if(listener != null){
                            listener.onFailure(message);
                            LogUtil.d(TAG,message);
                        }
                    }
                }else{
                    try {
                        if (listener != null) {
                            listener.onFailure(response.errorBody().string());
                            LogUtil.d(TAG,response.errorBody().string());
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
                    LogUtil.d(TAG,t.getMessage());
                }
            }
        });
    }


    /**
     * 获取单个活动的详细信息
     * @param params 请求参数
     * @param listener 回调接口
     */
    public static void getOneActivityDetailInfo(HashMap<String,Object> params,final GetOneActivityDetailInfoListener listener){
        //将请求参数转换成json格式
        String jsonString = JSON.toJSONString(params);
        LogUtil.json(TAG,jsonString);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonString);
        //post 提交json格式的数据
        Call<String> call = RestCreator.getRestService().postRaw(RestConstants.GET_ACTIVITY_DETAIL_INFO_URL, body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //网络请求成功的回调
                String responseString = response.body();
                String result;
                String message;
                ActivityDetailItem activityDetailItem = null;
                if(!TextUtils.isEmpty(responseString) && response.isSuccessful()){
                    LogUtil.json(TAG,responseString);
                    JSONObject jsonObject = JSON.parseObject(responseString);
                    result = jsonObject.getString("result");
                    message = jsonObject.getString("message");
                    if(SUCCESS.equals(result)){
                        //获取data
                        activityDetailItem = jsonObject.getObject("data", ActivityDetailItem.class);
                        if(listener != null){
                            listener.onSuccess(activityDetailItem);
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
