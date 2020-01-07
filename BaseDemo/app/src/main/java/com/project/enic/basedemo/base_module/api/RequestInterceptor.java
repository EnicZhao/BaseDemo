package com.project.enic.basedemo.base_module.api;

import android.text.TextUtils;

import com.project.enic.basedemo.base_module.util.sharepreference.SharePreferenceUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 使用说明:网络请求拦截器，当需要添加公共参数、对请求参数再处理、添加数据变化的请求头信息，可添加此拦截器进行请求处理
 */

public class RequestInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();
        String cookie = SharePreferenceUtil.getStringMemory(SharePreferenceUtil.COOKIE);
        if(!TextUtils.isEmpty(cookie)){
            requestBuilder.addHeader("cookie",cookie);
        }
        return chain.proceed(requestBuilder.build());
    }
}
