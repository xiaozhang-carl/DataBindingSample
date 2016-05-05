package com.example.zhanghongqiang.databindingsample.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.zhanghongqiang.databindingsample.R;

import java.util.ArrayList;

import rx.Subscription;

public abstract class BaseActivity extends AppCompatActivity {

    //标题返回按钮
    View view;

    //用于存放网络请求等的Subscription,便于activity在onDestroy的时候销毁
    public ArrayList<Subscription> subscriptionArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        view = findViewById(R.id.toolbar_back);
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickToolbarBack();
                }
            });
        }
    }

    public void clickToolbarBack() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscriptionArrayList.size() > 0) {
            for (Subscription subscription : subscriptionArrayList) {
                subscription.unsubscribe();
            }
        }
    }
}
