package com.example.zhanghongqiang.databindingsample.subscribers;


import rx.Subscriber;

/**
 * Created by liukun on 16/3/10.
 * ToDo:没有进度条的订阅者,后台加载,并在页面显示
 */
public class BackgroudNextSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private OnNext mOnNext;

    public BackgroudNextSubscriber(OnNext mOnNext) {
        this.mOnNext = mOnNext;
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

    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     */
    @Override
    public void onNext(T t) {
        //这里还需要判断发回的数据是否合法
        if (mOnNext != null) {
            //返回数据为非空的判断没做
            mOnNext.onNext(t);
        }
    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}