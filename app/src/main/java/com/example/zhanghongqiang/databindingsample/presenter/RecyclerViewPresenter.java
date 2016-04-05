package com.example.zhanghongqiang.databindingsample.presenter;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhanghongqiang.databindingsample.ui.BaseActivity;
import com.example.zhanghongqiang.databindingsample.view.IFListview;
import com.jcodecraeer.xrecyclerview.XRecyclerView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghongqiang on 16/3/1  下午4:58
 * ToDo:XRecyclerView的代理者
 */
public class RecyclerViewPresenter<T> extends ListPresenter<IFListview> {


    //xml里面的列表
    private XRecyclerView mRecyclerView;

    //没有数据将显示的提示
    private View emptyView;

    //头布局
    private View headerView;

    //底部布局
    private View footerView;

    //分页
    private int page = 0;

    //页的个数
    private int pageSize = 20;

    //下一页
    public int nextPage() {
        return ++page;
    }

    //获取列表
    public List<T> getDataList() {
        return myAdapter.dataList;
    }

    //返回每页的条目
    public int getPageSize() {
        return pageSize;
    }

    //设置每页的条目
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    //设置页码,配合分页使用
    public void setPage(int page) {
        this.page = page;
    }

    //这是为了加loadingDialog
    public void loadData() {
        if (F != null) {
            F.loadData();
        }
    }

    //加载新的数据
    @Override
    public void reLoadData() {
        if (F != null) {
            page = 0;
            F.loadData();
        }
    }

    // loadingDialog
    public void success(List<T> list) {
        refreshComplete();
        //下拉刷新,多次请求首页的话,清空数据
        if (page == 1) {
            //第一页就没有数据,显示空数据
            if (list.size() == 0) {
                showEmptyView();
                myAdapter.clearList();
                return;
            } else {
                //有数据的话,清空原来的数据,防止数据重复添加
                myAdapter.clearList();
            }
        }
        //隐藏占位图
        hideEmptyView();
        //加入新的数据
        myAdapter.addNewList(list);

    }


    //刷新完成,隐藏进度条...
    @Override
    public void refreshComplete() {
        mRecyclerView.loadMoreComplete();
        mRecyclerView.refreshComplete();
    }

    public void clearData() {
        if (myAdapter != null) {
            myAdapter.clearList();
        }
    }

    //万用的适配器
    private MyAdapter myAdapter;

    /**
     * @param activity
     * @param F
     */
    private RecyclerViewPresenter(BaseActivity activity, IFListview F) {
        super(activity, F);
    }


    /**
     * @param activity
     * @param F
     * @return
     */
    public static RecyclerViewPresenter with(BaseActivity activity, IFListview F) {
        return new RecyclerViewPresenter(activity, F);
    }

    /**
     * @param recyclerView
     * @param spanCount    网格布局的格数
     */
    public RecyclerViewPresenter recyclerView(@NonNull XRecyclerView recyclerView, int spanCount) {
        mRecyclerView = recyclerView;
        gridLayoutManager(spanCount);
        setRefreshLoadMore();
        return this;
    }

    /**
     * @param recyclerView
     */
    public RecyclerViewPresenter recyclerView(@NonNull XRecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        linearLayoutManager();
        setRefreshLoadMore();
        return this;
    }

    private void setRefreshLoadMore() {
        //设置下拉和加在更多的事件
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //下拉刷新,刷新成功后,清空原有数据
                //页面设置为第一页
                if (F != null) {
                    page = 0;
                    F.loadData();
                }
            }

            @Override
            public void onLoadMore() {
                if (F != null) {
                    //加载更多
                    F.loadData();
                }
            }
        });
    }

    private void linearLayoutManager() {
        //默认的layoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(mRecyclerView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //设置layoutManager
        mRecyclerView.setLayoutManager(layoutManager);
    }


    /**
     * ToDo:网格列表
     *
     * @param spanCount
     */
    private void gridLayoutManager(int spanCount) {
        //GridLayoutManager
        GridLayoutManager layoutManager = new GridLayoutManager(mRecyclerView.getContext(), spanCount);
        //设置layoutManager
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public RecyclerViewPresenter emptyView(@NonNull View emptyView) {
        this.emptyView = emptyView;
        return this;
    }

    public RecyclerViewPresenter headerView(@NonNull View headerView) {
        this.headerView = headerView;
        return this;
    }

    public RecyclerViewPresenter footerView(@NonNull View footerView) {
        this.footerView = footerView;
        return this;
    }

    /**
     * @param pullRefresh 头部是否下拉
     * @param loadingMore 底部加载更多
     * @return
     */
    public RecyclerViewPresenter cancelRefresh(boolean pullRefresh, boolean loadingMore) {
        if (mRecyclerView != null) {
            mRecyclerView.setPullRefreshEnabled(pullRefresh);
            mRecyclerView.setLoadingMoreEnabled(loadingMore);
        }
        return this;
    }

    public RecyclerViewPresenter build() {

        if (this.headerView != null) {
            mRecyclerView.addHeaderView(headerView);
        }
        if (this.footerView != null) {
            mRecyclerView.addFootView(footerView);
        }
        myAdapter = new MyAdapter();
        mRecyclerView.setAdapter(myAdapter);
        return this;
    }

    public void notifyDataSetChanged() {
        if (myAdapter != null) {
            //数据如果为空的话,现实占位图
            if (emptyView != null && getDataList().size() == 0) {
                showEmptyView();
            }
            myAdapter.notifyDataSetChanged();
        }
    }

    public void notifyItemRangeRemoved(int positionStart, int itemCount) {
        if (myAdapter != null) {
            //数据如果为空的话,现实占位图
            if (emptyView != null && getDataList().size() == 0) {
                showEmptyView();
            }
            myAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }
    }

    public void onError(Throwable e) {
        mRecyclerView.refreshComplete();
        mRecyclerView.loadMoreComplete();

    }

    public int getPage() {
        return page;
    }


    private void showEmptyView() {
        if (emptyView != null) {
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    private void hideEmptyView() {
        if (emptyView != null) {
            emptyView.setVisibility(View.GONE);
        }
    }

    /**
     * RecyclerView万用的适配器,
     */
    class MyAdapterViewHolder extends XRecyclerView.ViewHolder {

        T data;
        ViewDataBinding binding;

        public MyAdapterViewHolder(View itemView) {
            super(itemView);
        }

        public void setData(T data) {
            this.data = data;
            //调用借口的方法
            F.updateView(data, binding, getAdapterPosition());
        }
    }


    class MyAdapter extends XRecyclerView.Adapter<MyAdapterViewHolder> {

        ArrayList<T> dataList = new ArrayList<>();

        @Override
        public MyAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //调用借口的方法
            ViewDataBinding binding = F.createView(parent, viewType);
            MyAdapterViewHolder viewHolder = new MyAdapterViewHolder(binding.getRoot());
            viewHolder.binding = binding;

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapterViewHolder holder, int position) {
            holder.setData(getItem(position));
        }

        private T getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        @Override
        public int getItemViewType(int position) {
            //调用借口的方法
            return F.getViewType(position);
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
