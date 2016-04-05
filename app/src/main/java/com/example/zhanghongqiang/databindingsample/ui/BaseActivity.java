package com.example.zhanghongqiang.databindingsample.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.zhanghongqiang.databindingsample.R;

public abstract class BaseActivity extends AppCompatActivity {
    //标题返回按钮
    View view;

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
}
