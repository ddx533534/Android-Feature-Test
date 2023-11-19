package com.example.androidfeature.network;

import android.app.Application;

import com.google.gson.GsonBuilder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DemoRetrofit {
    private static DemoRetrofit instance;
    private Retrofit retrofit;

    static {
        instance = new DemoRetrofit();
    }

    private DemoRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.baidu.com")
                .addConverterFactory(createConverterFactory())
                .client(createOkHttpClient())
                .build();
    }

    private OkHttpClient createOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .build();
    }

    private Converter.Factory createConverterFactory() {
        return GsonConverterFactory.create(
                new GsonBuilder().setLenient().create()
        );
    }

    public static RequestService getRetrofit(){
        return instance.retrofit.create(RequestService.class);
    };


}
