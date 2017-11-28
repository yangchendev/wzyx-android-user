package com.allelink.wzyx.net.service;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


/**
 * @author yangc
 * @version 1.0
 * @filename RestService
 * @date 2017/11/2
 * @description 为retrofit构建的网络请求注解服务
 * @email 1048027353@qq.com
 */

public interface RestService {

    /**
     * @param url 请求地址，params 参数
     * @return Observable<String>
     * @description 用于一般的GET请求
     */
    @GET
    Call<String> get(@Url String url, @QueryMap HashMap<String, Object> params);

    /**
     * @param url 请求地址，params 参数
     * @return Observable<String>
     * @description 用于表单的POST请求
     */
    @FormUrlEncoded
    @POST
    Call<String> post(@Url String url, @FieldMap HashMap<String, Object> params);

    /**
     * 上传原始数据如 json
     * @param body 请求体
     * @return Observable<String>
     */
    @Headers("User-Agent:Android")
    @POST
    Call<String> postRaw(@Url String url, @Body RequestBody body);
    /**
     * @param url 请求地址，params 参数
     * @return Observable<String>
     * @description 用于表单的PUT请求
     */
    @FormUrlEncoded
    @PUT
    Call<String> put(@Url String url,@FieldMap HashMap<String, Object> params);
    /**
     * @param url 请求地址，params 参数
     * @return Observable<String>
     * @description DELETE请求
     */
    @DELETE
    Call<String> delete(@Url String url,@QueryMap HashMap<String, Object> params);

    /**
     * @param url 请求地址，params 参数
     * @return Observable<ResponseBody> 是一个输入流
     * @description 用于文件的下载请求
     */
    @Streaming
    @GET
    Call<ResponseBody> download(@Url String url, @QueryMap HashMap<String, Object> params);

    /**
     * @param url 请求地址，file 文件
     * @return Observable<String>
     * @description 用于文件的上传请求
     */
    @Headers("User-Agent:Android")
    @Multipart
    @POST
    Call<String> upload(@Url String url, @Part MultipartBody.Part file);

}
