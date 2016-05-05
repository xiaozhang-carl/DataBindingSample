package com.example.zhanghongqiang.databindingsample.subscribers;

import android.content.Context;

import com.example.zhanghongqiang.databindingsample.MyApplacation;
import com.example.zhanghongqiang.databindingsample.utils.ToastUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * Created by liukun on 16/3/10.
 * ToDo:没有进度条的订阅者,后台加载,并在页面显示,返回成功和失败
 */
public class BackgroundNextErrorSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private OnNextOnError mOnNextOnError;

    public BackgroundNextErrorSubscriber(OnNextOnError mOnNextOnError) {
        this.mOnNextOnError = mOnNextOnError;
    }


    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {

    }

    /**
     * 完成，隐藏ProgressDialog,检查数据类型是否正确
     */
    @Override
    public void onCompleted() {

    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        if (mOnNextOnError != null) {
            mOnNextOnError.onError();
        }
        Context context = MyApplacation.getInstance();
        if (e instanceof SocketTimeoutException) {
            ToastUtil.show(context, "网络中断，请检查您的网络状态");
        } else if (e instanceof ConnectException) {
            ToastUtil.show(context, "网络中断，请检查您的网络状态");
        } else {
            ToastUtil.show(context, "网络连接失败");
        }

    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     */
    @Override
    public void onNext(T t) {
        //这里还需要判断发回的数据是否合法
        if (mOnNextOnError != null) {
            mOnNextOnError.onNext(t);
        }
    }

    /**
     * 取消ProgressDialog的时候，取消对observble的订阅，同时也取消了http请求
     */
    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}