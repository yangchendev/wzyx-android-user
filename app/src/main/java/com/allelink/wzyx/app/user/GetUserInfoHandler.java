package com.allelink.wzyx.app.user;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.allelink.wzyx.model.User;
import com.allelink.wzyx.model.UserInfoResponse;
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
 * @filename GetUserInfoHandler
 * @date 2017/11/25
 * @description 获取用户信息处理类
 * @email 1048027353@qq.com
 */

public class GetUserInfoHandler {
    private static final String TAG = "GetUserInfoHandler";
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";
    private static final String MALE = "男";
    private static final String FEMALE = "女";
    private static final String SECRET = "保密";
    private static final String CODE_MALE = "0";
    private static final String CODE_FEMALE = "1";
    private static final String CODE_SECRET = "2";


    public static void getUserInfo(HashMap<String,Object> params,final IGetUserInfoListener listener){
        final String jsonString = JSON.toJSONString(params);
        LogUtil.json(TAG,jsonString);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonString);
        //post 提交json格式的数据
        Call<String> call = RestCreator.getRestService().postRaw(RestConstants.GET_USER_INFO_URL, body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                User user = new User();
                UserInfoResponse userInfoResponse = new UserInfoResponse();
                String responseString  = response.body();
                LogUtil.json(TAG,responseString);
                if(!TextUtils.isEmpty(responseString) && response.isSuccessful()){

                    JSONObject jsonObject = JSONObject.parseObject(responseString);
                    JSONObject data = jsonObject.getJSONObject("data");
                    userInfoResponse.setMessage(jsonObject.getString("message"));
                    userInfoResponse.setResult(jsonObject.getString("result"));
                    if(SUCCESS.equals(userInfoResponse.getResult())){
                        //成功的回调
                        if(data.getString("phoneNumber") == null){
                            user.setPhoneNumber("");
                        }else{
                            user.setPhoneNumber(data.getString("phoneNumber"));
                        }
                        if(data.getString("photo") == null){
                            user.setAvatar("");
                        }else{
                            user.setAvatar(data.getString("photo"));
                        }
                        if(data.getString("gender") == null){
                            user.setGender("");
                        }else{
                            if(CODE_MALE.equals(data.getString("gender"))){
                                user.setGender(MALE);
                            }else if(CODE_FEMALE.equals(data.getString("gender"))){
                                user.setGender(FEMALE);
                            }else {
                                user.setGender(SECRET);
                            }
                        }
                        if(data.getString("nickname") == null){
                            user.setNickname("");
                        }else{
                            user.setNickname(data.getString("nickname"));
                        }
                        if(data.getString("userId") == null){
                            user.setUserId("");
                        }else{
                            user.setUserId(data.getString("userId"));
                        }
                        listener.onGetUserInfoSuccess(user);
                    }else if(ERROR.equals(userInfoResponse.getResult())){
                        //失败的回调
                        listener.onGetUserInfoFailure(userInfoResponse.getMessage());
                    }
                }else{
                    try {
                        listener.onGetUserInfoFailure(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                listener.onGetUserInfoFailure(t.getMessage());
            }
        });
    }
}
