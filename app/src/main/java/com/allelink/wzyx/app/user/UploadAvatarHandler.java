package com.allelink.wzyx.app.user;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.allelink.wzyx.net.RestConstants;
import com.allelink.wzyx.net.RestCreator;
import com.allelink.wzyx.utils.log.LogUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author yangc
 * @version 1.0
 * @filename UploadAvatarHandler
 * @date 2017/11/27
 * @description 上传头像处理类
 * @email 1048027353@qq.com
 */

public class UploadAvatarHandler {
    private static final String TAG = "UploadAvatarHandler";
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";


    /**
     * 上传头像
     * @param phoneNumber 用户手机号
     * @param listener 接口
     */
    public static void uploadAvatar(String phoneNumber,String filePath, final IUploadAvatarListener listener){
        //获取头像
        File file = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("avatarFile", file.getName(), requestBody);
        LogUtil.d(TAG,body.headers().get("avatarFile"));
        Call<String> call = RestCreator.getRestService().upload(RestConstants.UPLOAD_AVATAR_PIC_URL
                + "?phoneNumber=" + phoneNumber, body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String responseString  = response.body();
                String result = null;
                LogUtil.json(TAG,responseString);
                if(!TextUtils.isEmpty(responseString) && response.isSuccessful()){
                    result = JSONObject.parseObject(responseString).getString("result");
                    if(SUCCESS.equals(result)){
                        String avatarUrl = JSONObject.parseObject(responseString).getString("imageUrl");
                        listener.onUploadAvatarSuccess(avatarUrl);
                        LogUtil.d(TAG,avatarUrl);
                    }else if(ERROR.equals(result)){
                        String message = JSONObject.parseObject(responseString).getString("message");
                        listener.onUploadAvatarFailure(message);
                    }
                }else{
                    try {
                        listener.onUploadAvatarFailure(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        LogUtil.d(TAG,e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                listener.onUploadAvatarFailure(t.getMessage());
            }
        });
    }
}
