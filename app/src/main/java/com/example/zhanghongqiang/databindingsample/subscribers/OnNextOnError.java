package com.example.zhanghongqiang.databindingsample.subscribers;

/**
 * Created by zhanghongqiang on 16/7/12.
 * ToDo:返回结果匹配,错误
 */
public interface OnNextOnError<T> extends OnNext<T> {

    //网络出错或其他
    void onError(Throwable e);
}
