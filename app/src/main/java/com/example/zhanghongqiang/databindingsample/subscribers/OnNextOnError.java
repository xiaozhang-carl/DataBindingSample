package com.example.zhanghongqiang.databindingsample.subscribers;

/**
 * Created by liukun on 16/3/10.
 * ToDo:  列表使用
 */
public interface OnNextOnError<T>{
    void onNext(T t);
    void onError();
}
