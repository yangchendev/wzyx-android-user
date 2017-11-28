package com.allelink.wzyx.app.sign.register;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.allelink.wzyx.model.CommonResponse;
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
 * @author yangc
 * @version 1.0
 * @filename RegisterHandler
 * @date 2017/11/9
 * @description 注册处理类
 * @email 1048027353@qq.com
 */

public class RegisterHandler {
    private static final String TAG = RegisterHandler.class.getSimpleName();
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";
    /**
     * 注册
     * @param params 请求的参数对
     * @param registerListener  注册回调接口
     */
    public static void register(HashMap<String,Object> params, final IRegisterListener registerListener){
        //将用户信息转换成json格式
        String jsonString = JSON.toJSONString(params);
        LogUtil.json(TAG,jsonString);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonString);
        //post 提交json格式的数据
        Call<String> call = RestCreator.getRestService().postRaw(RestConstants.REGISTER_URL, body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //网络请求成功的回调
                CommonResponse commonResponse = null;
                String responseString = response.body();
                if(!TextUtils.isEmpty(responseString) && response.isSuccessful()){
                    LogUtil.json(TAG,responseString);
                    commonResponse = JSON.parseObject(responseString, CommonResponse.class);
                    if(SUCCESS.equals(commonResponse.getResult())){
                        //注册成功的回调
                        registerListener.onRegisterSuccess(commonResponse.getMessage());
                    }else if(ERROR.equals(commonResponse.getResult())){
                        //注册失败的回调
                        registerListener.onRegisterFailure(commonResponse.getMessage());
                    }
                }else{
                    try {
                        registerListener.onRegisterFailure(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //注册失败回调
                registerListener.onRegisterFailure(t.getMessage());
            }
        });
    }
    /**
     * 注册
     * @param params 请求的参数对
     * @param registerListener  注册回调接口
     */
    public static void resetPassword(HashMap<String,Object> params, final IRegisterListener registerListener){
        //将用户信息转换成json格式
        String jsonString = JSON.toJSONString(params);
        LogUtil.json(TAG,jsonString);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonString);
        //post 提交json格式的数据
        Call<String> call = RestCreator.getRestService().postRaw(RestConstants.RESET_PASSWORD_URL, body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //网络请求成功的回调
                CommonResponse commonResponse = null;
                String responseString = response.body();
                if(!TextUtils.isEmpty(responseString) && response.isSuccessful()){
                    LogUtil.json(TAG,responseString);
                    commonResponse = JSON.parseObject(responseString, CommonResponse.class);
                    if(SUCCESS.equals(commonResponse.getResult())){
                        //注册成功的回调
                        registerListener.onRegisterSuccess(commonResponse.getMessage());
                    }else if(ERROR.equals(commonResponse.getResult())){
                        //注册失败的回调
                        registerListener.onRegisterFailure(commonResponse.getMessage());
                    }
                }else{
                    try {
                        registerListener.onRegisterFailure(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //注册失败回调
                registerListener.onRegisterFailure(t.getMessage());
            }
        });
    }
}
