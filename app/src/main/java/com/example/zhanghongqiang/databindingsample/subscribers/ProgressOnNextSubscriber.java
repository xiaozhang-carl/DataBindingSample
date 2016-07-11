package com.example.zhanghongqiang.databindingsample.subscribers;

import android.content.Context;
import android.util.Log;

import com.example.zhanghongqiang.databindingsample.R;
import com.example.zhanghongqiang.databindingsample.model.HttpResult;
import com.example.zhanghongqiang.databindingsample.utils.ToastUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * Created by liukun on 16/3/10.
 * ToDo:带进度条的提供给列表使用的订阅者,需要返回结果的使用
 */
public class ProgressOnNextSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    //观察者的下一步监听
    private OnNext nextListener;
    //显示进度条对话
    private ProgressDialogHandler mProgressDialogHandler;

    private Context context;

    public ProgressOnNextSubscriber(Context context, OnNext nextListener) {

        this.nextListener = nextListener;

        this.context = context;

        mProgressDialogHandler = new ProgressDialogHandler(this.context, this, true);
    }

    public ProgressOnNextSubscriber(Context context, OnNextOnErrorNoMatch nextListener) {

        this.nextListener = nextListener;

        this.context = context;

        mProgressDialogHandler = new ProgressDialogHandler(this.context, this, true);
    }

    public ProgressOnNextSubscriber(Context context, OnNextOnError nextListener) {

        this.nextListener = nextListener;

        this.context = context;

        mProgressDialogHandler = new ProgressDialogHandler(this.context, this, true);
    }


    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        showProgressDialog();
    }

    /**
     * 完成，隐藏ProgressDialog,检查数据类型是否正确
     */
    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        Log.i("123", e.toString());
        dismissProgressDialog();
        if (nextListener != null && nextListener instanceof OnNextOnError) {
            ((OnNextOnError) nextListener).onError(e);
        }
        if (e instanceof ConnectException) {
            ToastUtil.show(context, "网络中断，请检查您的网络状态");
        } else if (e instanceof SocketTimeoutException) {
            ToastUtil.show(context, "网络中断，请检查您的网络状态");
        } else {
            ToastUtil.show(context, context.getString(R.string.network_error));
        }

    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     */
    @Override
    public void onNext(T t) {
        //这里还需要判断发回的数据是否合法
        if (nextListener != null) {
            HttpResult<T> result = (HttpResult<T>) t;
//            if (result.code==0){
//                nextListener.onNext(result);
//            }else if (nextListener!=null  && nextListener instanceof NoMatch){
//                ((NoMatch)nextListener).notMatch(result);
//            }
            nextListener.onNext(result);

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