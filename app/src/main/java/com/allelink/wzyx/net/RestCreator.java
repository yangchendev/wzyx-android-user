package com.allelink.wzyx.net;

import com.allelink.wzyx.net.service.RestService;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author yangc
 * @version 1.0
 * @filename RestCreator
 * @date 2017/11/2
 * @description retrofit 全局构建类
 * @email 1048027353@qq.com
 */

public final class RestCreator {
    /**
    * 构建参数
    */
    private static final class ParamsHolder{
        private static final HashMap<String, Object> PARAMS = new HashMap<>();
    }

    /**
     * 获取参数
     * @return 返回参数
     */
    private static HashMap<String,Object> getParams(){
        return ParamsHolder.PARAMS;
    }
    /**
    * 构建OkHttp
    */
    private static final class OkHttpHolder{
        /**
        * 超时时间10s
        */
        private static final int TIME_OUT = 10;
        private static final OkHttpClient.Builder BUILDER = new OkHttpClient.Builder();
        private static final OkHttpClient OK_HTTP_CLIENT = BUILDER.connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();
    }
    /**
    * 构建Retrofit
    */
    private static final class RetrofitHolder{
        private static final Retrofit RETROFIT_CLIENT = new Retrofit.Builder()
                .baseUrl(RestConstants.BASE_URL)
                .client(OkHttpHolder.OK_HTTP_CLIENT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }
    /**
    * 构建service
    */
    private static final class RestServiceHolder{
        private static final RestService REST_SERVICE =
                RetrofitHolder.RETROFIT_CLIENT.create(RestService.class);
    }

    public static RestService getRestService(){
        return RestServiceHolder.REST_SERVICE;
    }

}
