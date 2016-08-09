package com.example.zhanghongqiang.databindingsample.subscribers;

import com.example.zhanghongqiang.databindingsample.MyApplacation;
import com.example.zhanghongqiang.databindingsample.R;
import com.example.zhanghongqiang.databindingsample.model.HttpResult;
import com.example.zhanghongqiang.databindingsample.utils.ToastUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * Created by liukun on 16/3/10.
 * ToDo:没有进度条的订阅者,后台加载,并在页面显示
 */
public class SilentlySubscriber<T> extends Subscriber<T> {

    private OnNext mOnNext;

    public SilentlySubscriber(OnNext mOnNext) {
        this.mOnNext = mOnNext;
    }


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
        
        if (e instanceof ConnectException) {
            ToastUtil.show(MyApplacation.getInstance(), "网络中断，请检查您的网络状态");
        } else if (e instanceof SocketTimeoutException) {
            ToastUtil.show(MyApplacation.getInstance(), "网络中断，请检查您的网络状态");
        } else {
            ToastUtil.show(MyApplacation.getInstance(), MyApplacation.getInstance().getString(R.string.network_error));
        }
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     */
    @Override
    public void onNext(T t) {
        //这里还需要判断发回的数据是否合法
        if (mOnNext != null) {
            HttpResult<T> result = (HttpResult<T>) t;
            mOnNext.onNext(result);
        }
    }

}