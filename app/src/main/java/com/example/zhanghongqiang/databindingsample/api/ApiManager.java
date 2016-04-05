package com.example.zhanghongqiang.databindingsample.api;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhanghongqiang on 16/3/31.
 */
public class ApiManager {

    private static ApiManager apiInstance;
    //地址
    public static final String BASE_URL = "https://api.douban.com/v2/movie/";
    //链接时长
    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;

    private Rest rest;

    public static ApiManager getInstance() {
        if (apiInstance == null) {
            synchronized (ApiManager.class) {
                apiInstance = new ApiManager();
            }
        }
        return apiInstance;
    }

    private ApiManager() {
        //https://drakeet.me/retrofit-2-0-okhttp-3-0-config
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain)
                    throws IOException {
                Response response = chain.proceed(chain.request());

                // Do anything with response here

                return response;
            }
        });
        retrofit = new Retrofit.Builder()
                //绑定地址
                .baseUrl(BASE_URL)
                //okhttp
                .client(builder.build())
                //gson转换工厂
                .addConverterFactory(GsonConverterFactory.create())
                //解析rxjava的适配器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        rest = retrofit.create(Rest.class);
    }


    public Rest getRest() {
        return rest;
    }
}
