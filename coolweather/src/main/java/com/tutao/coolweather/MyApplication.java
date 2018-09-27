package com.tutao.coolweather;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

/**
 * Created by jingting on 2018/2/1.
 */

public class MyApplication extends Application {


    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;

        Stetho.initializeWithDefaults(MyApplication.this);
    }

    public static Context getContext() {
        return sContext;
    }
}
