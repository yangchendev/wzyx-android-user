package com.allelink.wzyx.app.sign.signin;

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
 * @filename SignInHandler
 * @date 2017/11/9
 * @description 登录处理类
 * @email 1048027353@qq.com
 */

public class SignInHandler {
    private static final String TAG = SignInHandler.class.getSimpleName();
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";
    /**
     * 登录
     * @param params 请求的参数对
     * @param signInListener  登录回调接口
     */
    public static void signIn(HashMap<String,Object> params, final ISignInListener signInListener){
        //将用户信息转换成json格式
        String jsonString = JSON.toJSONString(params);
        LogUtil.json(TAG,jsonString);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonString);
        //post 提交json格式的数据
        Call<String> call = RestCreator.getRestService().postRaw(RestConstants.SIGN_IN_URL, body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //网络请求成功的回调
                CommonResponse commonResponse = null;
                String responseString = response.body();
                if(!TextUtils.isEmpty(responseString) && response.isSuccessful()){
                    LogUtil.json(TAG,responseString);
                    //将服务器返回的信息转换成类
                    commonResponse = JSON.parseObject(responseString, CommonResponse.class);
                    //登录成功
                    if(SUCCESS.equals(commonResponse.getResult())){
                        signInListener.onSignInSuccess(commonResponse.getMessage());
                    }else if(ERROR.equals(commonResponse.getResult())){
                        //登录失败
                        signInListener.onSignInFailure(commonResponse.getMessage());
                    }
                }else {
                    try {
                        signInListener.onSignInFailure(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //网络请求失败的回调
                signInListener.onSignInFailure(t.getMessage());
            }
        });

    }
}
