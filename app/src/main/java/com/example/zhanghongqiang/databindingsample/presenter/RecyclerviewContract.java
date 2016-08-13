package com.example.zhanghongqiang.databindingsample.presenter;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

/**
 * Created by zhanghongqiang on 16/7/20  上午10:49
 * ToDo:列表的契约者,用来管理mvvp里的v
 */
public interface RecyclerviewContract {

    //加载数据
    interface IFLoadData {
        void loadData();
    }

    //适配器使用
    interface IFListview<T> {

        //可以根据数据类型来显示不同的item
        int getViewType(int position);

        //显示数据
        void updateView(@NonNull T data, @NonNull ViewDataBinding binding);

        //这里的使用一定要注意,用第二个参数来判断
        ViewDataBinding createView(ViewGroup parent, int position);
    }

    //代理者
    interface XRDelegate<T>{


        void reLoadData();

        void notifyDataSetChanged();

        void notifyItemChanged(int position);

        void notifyItemRangeRemoved(int position);

    }
}
