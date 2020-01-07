package com.project.enic.basedemo.base_module.api;

import com.project.enic.basedemo.base_module.util.sharepreference.SharePreferenceUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import kotlin.Pair;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 响应拦截器，使用场景（当服务器检验用户token失效时，会统一进入登录页面,配合RxBus使用）
 */
public class ResponseInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        for (Pair<? extends String, ? extends String> pair : response.headers()) {
            if(pair.component2().startsWith("JSESSIONID") && pair.component1().equals("Set-Cookie")){
                SharePreferenceUtil.setStringMemory(SharePreferenceUtil.COOKIE,pair.component2());
            }
        }
        return response.newBuilder().build();
    }
}
