package com.project.enic.basedemo.base_module.api;

import com.project.enic.basedemo.base_module.util.Config;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiEngine {
    private volatile static ApiEngine apiEngine;
    private static Retrofit retrofit;
    private static final int requestTime = 20;//请求超时、读写时间

    private ApiEngine() {
        String domain_url = Config.API_DOMAIN; //base_url
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(requestTime, TimeUnit.SECONDS)
                .writeTimeout(requestTime, TimeUnit.SECONDS)
                .readTimeout(requestTime, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new RequestInterceptor())
                .addInterceptor(new ResponseInterceptor())
                .build();
//                .addInterceptor(new RequestInterceptor())//请求拦截器，可进入该类查看使用信息
        retrofit = new Retrofit.Builder()
                .baseUrl(domain_url)//公共的请求地址
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static ApiEngine init() {
        if (apiEngine == null) {
            synchronized (ApiEngine.class) {
                if (apiEngine == null) {
                    apiEngine = new ApiEngine();
                }
            }
        }
        return apiEngine;
    }

    public static Request getApiService() {
        return retrofit.create(Request.class);
    }
}
