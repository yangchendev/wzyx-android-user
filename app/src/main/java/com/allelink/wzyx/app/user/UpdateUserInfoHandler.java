package com.allelink.wzyx.app.user;

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
 * @filename UpdateUserInfoHandler
 * @date 2017/11/25
 * @description 更新用户信息处理类
 * @email 1048027353@qq.com
 */

public class UpdateUserInfoHandler {
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";
    private static final String TAG = "UpdateUserInfoHandler";
    public static void updateUserInfoHandler(HashMap<String,Object> params,final IUpdateUserInfoListener listener){
        String jsonString = JSON.toJSONString(params);
        LogUtil.json(TAG,jsonString);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonString);
        //post 提交json格式的数据
        Call<String> call = RestCreator.getRestService().postRaw(RestConstants.UPDATE_USER_INFO_URL, body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                CommonResponse commonResponse = null;
                String responseString  = response.body();
                LogUtil.json(TAG,responseString);
                if(!TextUtils.isEmpty(responseString) && response.isSuccessful()){

                    commonResponse = JSON.parseObject(responseString, CommonResponse.class);
                    if(SUCCESS.equals(commonResponse.getResult())){
                        //成功的回调
                        listener.onUpdateUserInfoSuccess(commonResponse.getMessage());
                    }else if(ERROR.equals(commonResponse.getResult())){
                        //失败的回调
                        listener.onUpdateUserInfoFailure(commonResponse.getMessage());
                    }
                }else{
                    try {
                        listener.onUpdateUserInfoFailure(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                listener.onUpdateUserInfoFailure(t.getMessage());
            }
        });

    }
}
