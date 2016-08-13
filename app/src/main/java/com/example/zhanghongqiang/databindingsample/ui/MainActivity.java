package com.example.zhanghongqiang.databindingsample.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.example.zhanghongqiang.databindingsample.R;
import com.example.zhanghongqiang.databindingsample.databinding.ActivityMainBinding;

/**
 * Created by zhanghongqiang on 16/8/13  下午2:06
 * ToDo:
 */
public class MainActivity extends BaseActivity {
    ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setClick(this);
    }

    public void test1(View view) {
        startActivity(RecyclerviewActivity.class);
    }

    public void test2(View view) {
        startActivity(XRecyclerviewActivity.class);
    }
    public void test3(View view) {
        startActivity(NestRecyclerviewActivity.class);
    }
}
