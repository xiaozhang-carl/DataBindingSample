package com.example.zhanghongqiang.databindingsample.subscribers;

import com.example.zhanghongqiang.databindingsample.model.HttpResult;

import rx.Subscriber;

/**
 * Created by liukun on 16/3/10.
 * ToDo:没有进度条的订阅者,后台加载,并在页面显示
 */
public class SilentlyNextSubscriber<T> extends Subscriber<T> {

    private OnNext mOnNext;

    public SilentlyNextSubscriber(OnNext mOnNext) {
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