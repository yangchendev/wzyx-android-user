package com.allelink.wzyx.app.activityinfo;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
 * @version TODO
 * @date 2017/12/8
 * @email 1048027353@qq.com
 */
public class ActivityInfoHandler {
    private static final String TAG = ActivityInfoHandler.class.getSimpleName();
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";
    /**
    * 活动列表
    */
    private List<ActivityItem> activityItems = new ArrayList<>();

    /**
     * 获取默认活动列表信息
     * @param params 请求参数 经纬度
     * @param listener 接口
     */
    public void getDefaultActivityList(HashMap<String,Object> params,final GetActivityInfoListener listener){
        //将用户信息转换成json格式
        String jsonString = JSON.toJSONString(params);
        LogUtil.json(TAG,jsonString);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonString);
        //post 提交json格式的数据
        Call<String> call = RestCreator.getRestService().postRaw(RestConstants.GET_DEFAULT_ACTIVITY_INFO_LIST_URL, body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //网络请求成功的回调
                String responseString = response.body();
                String result;
                String message;
                if(!TextUtils.isEmpty(responseString) && response.isSuccessful()){
                    LogUtil.json(TAG,responseString);
                    JSONObject jsonObject = JSON.parseObject(responseString);
                    result = jsonObject.getString("result");
                    message = jsonObject.getString("message");
                    if(SUCCESS.equals(result)){
                        activityItems = jsonObject.getJSONArray("data").toJavaList(ActivityItem.class);
                        listener.onSuccess(activityItems);
                    }else if(ERROR.equals(result)){
                        listener.onFailure(message);
                    }
                }else{
                    try {
                        listener.onFailure(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });


    }

}
