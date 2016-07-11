package com.example.zhanghongqiang.databindingsample.subscribers;

/**
 * Created by zhanghongqiang on 16/7/12.
 * ToDo:返回结果匹配,错误,不匹配
 */
public interface OnNextOnErrorNoMatch<T> extends OnNextOnError<T>{

    //返回结果不匹配:code!=0
    void notMatch(T t);

}
