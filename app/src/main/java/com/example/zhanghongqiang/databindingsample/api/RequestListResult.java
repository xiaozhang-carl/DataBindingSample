package com.example.zhanghongqiang.databindingsample.api;

import java.io.Serializable;
import java.util.ArrayList;


public class RequestListResult <T> implements Serializable,IResult {
    public static final int CODE_NO_LOGIN = 1;

    public int code;
    public String message;
    public ArrayList<T> data;


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
}
