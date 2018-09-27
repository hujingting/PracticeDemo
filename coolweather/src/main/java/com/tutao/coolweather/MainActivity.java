package com.tutao.coolweather;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tutao.common.BaseActivity;
import com.tutao.common.config.AppConfig;
import com.tutao.common.dto.TestWorkData;
import com.tutao.common.network.BasicParamsInterceptor;
import com.tutao.common.utils.GsonHelper;
import com.tutao.coolweather.api.TestApi;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends BaseActivity {

    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = findViewById(R.id.button);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.addInterceptor(new BasicParamsInterceptor());
                OkHttpClient okHttpClient = builder.build();

                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .baseUrl(AppConfig.testBaseUrl)
                        .client(okHttpClient)
                        .build();
                TestApi testApi = retrofit.create(TestApi.class);
                TestWorkData testWorkData = new TestWorkData();

                HashMap hashMap = new HashMap();
                hashMap.put("token", "19071a7887dc18925bea6f98edd824ab");
                hashMap.put("page", "1");
                hashMap.put("pageSize", "10");

                Call<ResponseBody> call = testApi.getWorkList("tool.work.history", GsonHelper.getInstance().getGson().toJson(hashMap));
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String body = response.body().toString();
                        showToast("success");
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        showToast("failure");
                    }
                });
//                testApi.getWorkList("tool.work.history", hashMap)
//                        .subscribeOn(Schedulers.newThread())
//                        .observeOn(Schedulers.io())
//                        .doOnNext(new Action1<TestWorkData>() {
//                            @Override
//                            public void call(TestWorkData materialDto) {
//                                showToast("call");
//                            }
//                        })
//
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Subscriber<TestWorkData>() {
//                            @Override
//                            public void onCompleted() {
//                                showToast("complete");
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                showToast("error");
//                            }
//
//                            @Override
//                            public void onNext(TestWorkData materialDto) {
//
//                                showToast("success");
//                            }
//                        });
            }
        });
    }
}
