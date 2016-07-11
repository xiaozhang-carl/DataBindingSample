package com.example.zhanghongqiang.databindingsample.presenter;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;


import com.example.zhanghongqiang.databindingsample.ui.BaseActivity;
import com.example.zhanghongqiang.databindingsample.view.IFListview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghongqiang on 16/3/1  下午4:58
 * ToDo:listView的代理者
 */
public class ListViewPresenter<T> extends BasePresenter<IFListview> {

    //含有listView的activity
    private BaseActivity mActivity;

    //ml里面的列表
    private ListView listView;

    //没有数据将显示的提示
    private View emptyView;

    //头布局
    private View headerView;

    //底部布局
    private View footerView;


    //万用的适配器
    private MyAdapter myAdapter;

    public ListViewPresenter(BaseActivity activity, IFListview F) {
        super(activity, F);
    }


    public void reLoadData() {
        if (F != null) {
            F.loadData();
        }
    }

    /**
     * @param activity
     * @param F
     * @return
     */
    public static ListViewPresenter with(BaseActivity activity, IFListview F) {
        return new ListViewPresenter(activity, F);
    }


    /**
     * @param listView
     */
    public ListViewPresenter listView(@NonNull ListView listView) {
        this.listView = listView;
        return this;
    }


    public ListViewPresenter emptyView(@NonNull View emptyView) {
        this.emptyView = emptyView;
        return this;
    }

    public ListViewPresenter headerView(@NonNull View headerView) {
        this.headerView = headerView;
        return this;
    }

    public ListViewPresenter footerView(@NonNull View footerView) {
        this.footerView = footerView;
        return this;
    }


    public ListViewPresenter build() {

        if (this.headerView != null) {
            this.listView.addHeaderView(headerView, null, false);
        }
        if (this.footerView != null) {
            this.listView.addFooterView(footerView, null, false);
        }
        myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);
        return this;
    }


    public void success(List<T> list) {
        myAdapter.clearList();
        myAdapter.addNewList(list);
    }

    public List<T> getDataList() {
        return myAdapter.dataList;
    }

    public void setNotifyDataSetChanged() {
        myAdapter.notifyDataSetChanged();
    }

    /**
     * listView万用的适配器,
     */
    class MyAdapter extends BaseAdapter {

        final ArrayList<T> dataList = new ArrayList<>();

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public T getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewDataBinding binding = null;
            if (view == null) {
                binding = F.createView(parent, 0);
                view = binding.getRoot();
                view.setTag(binding);
            }
            if (binding == null) {
                binding = (ViewDataBinding) view.getTag();
            }
            F.updateView(getItem(position), binding, position);
            return view;
        }


        public BaseAdapter getAdapter() {
            return this;
        }


        public void clearList() {
            dataList.clear();
            notifyDataSetChanged();
        }

        public void addNewList(List<T> list) {
            if (list != null && list.size() > 0) {
                dataList.addAll(list);
                notifyDataSetChanged();
            }
        }
    }
}
