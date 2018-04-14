package com.tutao.coolweather.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by jingting on 2018/1/29.
 */

public interface TestApi {
    @FormUrlEncoded
    @POST("api")
    Call<ResponseBody> getWorkList(@Field("method") String method, @Field("args")String params);
}
