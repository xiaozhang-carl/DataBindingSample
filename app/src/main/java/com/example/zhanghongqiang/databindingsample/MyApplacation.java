package com.example.zhanghongqiang.databindingsample;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by zhanghongqiang on 16/5/5  上午9:37
 * ToDo:
 */
public class MyApplacation extends Application {


    private static MyApplacation application;


    public static MyApplacation getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        Fresco.initialize(this);
    }
}
