package com.allelink.wzyx.app.sign.checkcode;

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
 * @filename CheckCodeHandler
 * @date 2017/11/10
 * @description 验证码发送请求处理
 * @email 1048027353@qq.com
 */

public class CheckCodeHandler {
    private static final String TAG = CheckCodeHandler.class.getSimpleName();
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";
    /**
     *
     * @param params 请求的参数对
     * @param checkCodeSendListener  回调接口
     */
    public static void sendCheckCode(HashMap<String,Object> params, final ICheckCodeSendListener checkCodeSendListener){
        //将用户信息转换成json格式
        String jsonString = JSON.toJSONString(params);
        LogUtil.json(TAG,jsonString);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonString);
        //post 提交json格式的数据
        Call<String> call = RestCreator.getRestService().postRaw(RestConstants.SMS_URL, body);
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
                        //成功的回调
                        checkCodeSendListener.onCheckCodeSendSuccess(commonResponse.getMessage());
                    }else if(ERROR.equals(commonResponse.getResult())){
                        //失败的回调
                        checkCodeSendListener.onCheckCodeSendFailure(commonResponse.getMessage());
                    }
                }else{
                    try {
                        checkCodeSendListener.onCheckCodeSendFailure(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //注册失败回调
                checkCodeSendListener.onCheckCodeSendFailure(t.getMessage());
            }
        });
    }
}
