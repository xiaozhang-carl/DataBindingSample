package com.example.zhanghongqiang.databindingsample.api;

import java.io.Serializable;

public class RequestResult<T> implements Serializable, IResult {
    public static final int CODE_NO_LOGIN = 1;
    public static final int RESULT_SUCESS = 0;

    /**
     * 0成功，1未登录，2其他错误
     */
    public int code;
    public String message;
    public T data;

    @Override
    public int code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.message;
    }

    @Override
    public Object data() {
        return this.data;
    }

    @Override
    public String toString() {
        return "RequestResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

}
