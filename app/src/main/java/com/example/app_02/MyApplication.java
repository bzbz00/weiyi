package com.example.app_02;

import android.app.Application;

import com.android.volley.BuildConfig;

import okhttp3.logging.HttpLoggingInterceptor;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化网络相关配置
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
    }
} 