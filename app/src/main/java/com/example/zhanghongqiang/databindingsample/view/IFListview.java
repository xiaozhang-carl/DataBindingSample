package com.example.zhanghongqiang.databindingsample.view;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

/**
 * Created by zhanghongqiang on 16/3/1  下午5:16
 * ToDo:由用到RecyclerView的BaseListActivity实现,在RecyclerViewPresenter里面使用
 */
public interface IFListview<T> extends IFBaseView {


    int getViewType(int position);
    //加载数据
    void loadData();
    //显示数据
    void updateView(@NonNull T data, @NonNull ViewDataBinding binding, int position);


    ViewDataBinding createView(ViewGroup parent, int viewType);
}
