package com.example.zhanghongqiang.databindingsample.subscribers;

/**
 * Created by liukun on 16/3/10.
 * ToDo:
 */
public interface OnNextNotMatchOnError<T> {
    //返回结果正确:code==0
    void onNext(T t);

    //返回结果不匹配:code!=0
    void notMatch(T t);

    //网络出错或其他
    void onError(Throwable e);
}
