package com.tutao.common.network;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jingting on 2018/2/1.
 */

public class BasicParamsInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        String method = request.method();
        Headers headers = request.headers();
        HttpUrl httpUrl = request.url().newBuilder()
                .addQueryParameter("language", "1")
                .addQueryParameter("ver", "1")
                .addQueryParameter("os", "2")
                .addQueryParameter("channel", "tutao_test")
                .build();
        Request request1 = request.newBuilder().url(httpUrl).build();
        return chain.proceed(request1);
    }
}
