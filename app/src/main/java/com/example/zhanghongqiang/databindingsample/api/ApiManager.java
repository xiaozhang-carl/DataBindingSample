package com.example.zhanghongqiang.databindingsample.api;

import android.content.Context;

import com.example.zhanghongqiang.databindingsample.subscribers.OnNext;
import com.example.zhanghongqiang.databindingsample.subscribers.OnNextNotMatch;
import com.example.zhanghongqiang.databindingsample.subscribers.OnNextNotMatchOnError;
import com.example.zhanghongqiang.databindingsample.subscribers.OnNextOnError;
import com.example.zhanghongqiang.databindingsample.subscribers.ProgressNextErrorSubscriber;
import com.example.zhanghongqiang.databindingsample.subscribers.ProgressNextMatchErrorSubscriber;
import com.example.zhanghongqiang.databindingsample.subscribers.ProgressNextMatchSubscriber;
import com.example.zhanghongqiang.databindingsample.subscribers.ProgressNextSubscriber;
import com.example.zhanghongqiang.databindingsample.subscribers.SilentlyNextErrorSubscriber;
import com.example.zhanghongqiang.databindingsample.subscribers.SilentlyNextSubscriber;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


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

    /**
     * 显示加载进度条的网络处理,返回成功
     *
     * @param context      上下文
     * @param o            rest返回的Observabler
     * @param nextListener 回调结果的接口
     * @param <T>
     */
    public static <T> Subscription toSubscribe(Context context, rx.Observable<T> o, OnNext<T> nextListener) {
        return o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressNextSubscriber<T>(context, nextListener));
    }

    /**
     * 显示加载进度条的网络处理,返回返回成功,结果不匹配
     *
     * @param context      上下文
     * @param o            rest返回的Observabler
     * @param nextListener 回调结果的接口
     * @param <T>
     */
    public static <T> Subscription toSubscribe(Context context, rx.Observable<T> o, OnNextNotMatch<T> nextListener) {
        return o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressNextMatchSubscriber<T>(context, nextListener));
    }

    /**
     * 显示加载进度条的网络处理,刷新列表示用,需要处理返回结果的使用,返回成功和失败
     *
     * @param context      上下文
     * @param o            rest返回的Observabler
     * @param nextListener 回调结果的接口
     * @param <T>
     */
    public static <T> Subscription toSubscribe(Context context, rx.Observable<T> o, OnNextOnError<T> nextListener) {
        return o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressNextErrorSubscriber<T>(context, nextListener));

    }


    /**
     * 显示加载进度条的网络处理,刷新列表示用,需要处理返回结果的使用,返回成功,结果不匹配和失败
     *
     * @param context      上下文
     * @param o            rest返回的Observabler
     * @param nextListener 回调结果的接口
     * @param <T>
     */
    public static <T> Subscription toSubscribe(Context context, rx.Observable<T> o, OnNextNotMatchOnError<T> nextListener) {
        return o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressNextMatchErrorSubscriber<T>(context, nextListener));

    }

    /**
     * 后台默认加载数据,返回成功
     *
     * @param o
     * @param nextListener
     * @param <T>
     */
    public static <T> Subscription toSubscribe(rx.Observable<T> o, OnNext<T> nextListener) {
        return o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SilentlyNextSubscriber<T>(nextListener));
    }

    /**
     * 后台默认加载数据,返回成功和失败
     *
     * @param o
     * @param nextListener
     * @param <T>
     */
    public static <T> Subscription toSubscribe(rx.Observable<T> o, OnNextOnError<T> nextListener) {
        return o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SilentlyNextErrorSubscriber<T>(nextListener));
    }


}
