package com.example.zhanghongqiang.databindingsample.presenter;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhanghongqiang.databindingsample.databinding.ViewEmptyBinding;
import com.example.zhanghongqiang.databindingsample.databinding.ViewRecyclerviewBinding;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghongqiang on 16/7/20  上午11:18
 * ToDo:列表代理者
 */
public class XRecyclerViewPresenter<T> implements XRecyclerviewContract.XRDelegate {

    //暴露给外界的接口是实现者
    XRecyclerviewContract.IFListview F;

    //databinding的好处是可以减少自定义view,这是一个包含列表,空布局的xml
    ViewRecyclerviewBinding mBinding;

    //ViewRecyclerviewBinding里面的空布局
    ViewEmptyBinding mEmptyBinding ;

    //xml里面的列表
    private XRecyclerView mRecyclerView;

    //万用的适配器
    private MyAdapter myAdapter;

    //头布局
    private View headerView;

    //底部布局
    private View footerView;

    //分页,从0开始
    private int page = 0;

    //每一页的item个数,默认20条
    private int pageSize = 20;


    //下一页
    public int nextPage() {
        return ++page;
    }


    public int getPage() {
        return page;
    }

    //获取数据列表
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
    public void reLoadData() {
        if (F != null) {
            page = 0;
            F.loadData();
        }
    }

    //加载成功的结果显示
    public void success(List<T> list) {

        //下拉刷新,多次请求首页的话,清空数据
        if (page == 1 || page == 0) {
            //第一页就没有数据,显示空数据
            if (list.size() == 0) {
                showEmptyView();
                myAdapter.clearList();
                refreshComplete();
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
        refreshComplete();
    }


    //刷新完成,隐藏进度条...
    public void refreshComplete() {
        if (page <= 1) {
            mRecyclerView.refreshComplete();
        } else {
            mRecyclerView.loadMoreComplete();
        }

    }

    public void clearData() {
        if (myAdapter != null) {
            myAdapter.clearList();
        }
    }

    public XRecyclerViewPresenter(XRecyclerviewContract.IFListview listview){
        this.F=listview;
    }

    /**
     * @return
     */
    public static XRecyclerViewPresenter with(XRecyclerviewContract.IFListview listview) {

        return new XRecyclerViewPresenter(listview);
    }
    public XRecyclerViewPresenter recyclerView(@NonNull ViewRecyclerviewBinding binding) {
        initVariable(binding);
        linearLayoutManager();
        setRefreshLoadMore();
        setRefreshLoadingMoreProgressStyle();
        return this;
    }

    private void initVariable(@NonNull ViewRecyclerviewBinding binding) {
        mBinding = binding;
        mRecyclerView = mBinding.recyclerview;
        mEmptyBinding= mBinding.viewStub;
    }


    public ViewEmptyBinding getEmptyBinding() {
        return mEmptyBinding;
    }

    /**
     * @param spanCount 网格布局的格数
     */
    public XRecyclerViewPresenter<T> recyclerView(@NonNull ViewRecyclerviewBinding binding, int spanCount) {
        initVariable(binding);
        gridLayoutManager(spanCount);
        setRefreshLoadMore();
        setRefreshLoadingMoreProgressStyle();
        return this;
    }

    /**
     * @param spanCount   交错网格的格子数
     * @param orientation
     * @return
     */
    public XRecyclerViewPresenter<T> recyclerView(@NonNull ViewRecyclerviewBinding binding, int spanCount, int orientation) {
        initVariable(binding);
        staggeredGridLayoutManager(spanCount, orientation);
        setRefreshLoadMore();
        setRefreshLoadingMoreProgressStyle();
        return this;
    }


    //加载动画http://blog.csdn.net/developer_jiangqq/article/details/49612399
    private void setRefreshLoadingMoreProgressStyle() {
        //头部加载小圆点,主题黄
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        //底部小方块
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.LineScaleParty);
    }



    /**
     * @param recycledViewPool
     */
    public XRecyclerViewPresenter setRecycledViewPool(@NonNull RecyclerView.RecycledViewPool recycledViewPool) {
        mRecyclerView.setRecycledViewPool(recycledViewPool);
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
                    if (mEmptyBinding.getRoot() != null) {
                        mEmptyBinding.getRoot().setVisibility(View.GONE);
                    }
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

    /**
     * ToDo:交错网格
     *
     * @param spanCount
     */
    private void staggeredGridLayoutManager(int spanCount, int orientation) {
        // 交错网格布局管理器
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(spanCount, orientation);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
    }


    public XRecyclerViewPresenter headerView(@NonNull View headerView) {
        this.headerView = headerView;
        return this;
    }

    public XRecyclerViewPresenter footerView(@NonNull View footerView) {
        this.footerView = footerView;
        return this;
    }

    public XRecyclerViewPresenter emptyTip(@NonNull String tip) {
        if (mEmptyBinding!=null){
            mEmptyBinding.setTip(tip);
        }
        return this;
    }


    /**
     * @param pullRefresh 头部是否下拉
     * @param loadingMore 底部加载更多
     * @return
     */
    public XRecyclerViewPresenter<T> cancelRefresh(boolean pullRefresh, boolean loadingMore) {
        if (mRecyclerView != null) {
            mRecyclerView.setPullRefreshEnabled(pullRefresh);
            mRecyclerView.setLoadingMoreEnabled(loadingMore);
        }
        return this;
    }

    public XRecyclerViewPresenter<T> build() {
        //添加头部
        if (this.headerView != null) {
            mRecyclerView.addHeaderView(headerView);
        }
        //添加尾部
        if (this.footerView != null) {
            mRecyclerView.addFootView(footerView);
        }
        //新建适配器
        myAdapter = new MyAdapter();
        //设置适配器
        mRecyclerView.setAdapter(myAdapter);
        return this;
    }

    public void notifyDataSetChanged() {
        if (myAdapter != null) {
            //数据如果为空的话,现实占位图
            if (mEmptyBinding.getRoot() != null && getDataList().size() == 0) {
                showEmptyView();
            }
            myAdapter.notifyDataSetChanged();
        }
    }

    public void notifyItemRangeRemoved(int positionStart, int itemCount) {
        if (myAdapter != null) {
            //数据如果为空的话,现实占位图
            if (mEmptyBinding.getRoot() != null && getDataList().size() == 0) {
                showEmptyView();
            }
            myAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }
    }


    public void showEmptyView() {
        if (mEmptyBinding.getRoot() != null) {
            mEmptyBinding.getRoot().setVisibility(View.VISIBLE);
        }
    }

    private void hideEmptyView() {
        if (mEmptyBinding.getRoot() != null) {
            mEmptyBinding.getRoot().setVisibility(View.GONE);
        }
    }


    //适配器
    class MyAdapter extends XRecyclerView.Adapter<XRecyclerViewPresenter.MyAdapterViewHolder> {

        ArrayList<T> dataList = new ArrayList<>();

        @Override
        public XRecyclerViewPresenter.MyAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //调用借口的方法
            ViewDataBinding binding = F.createView(parent, viewType);
            XRecyclerViewPresenter.MyAdapterViewHolder viewHolder = new XRecyclerViewPresenter.MyAdapterViewHolder(binding.getRoot());
            viewHolder.binding = binding;

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(XRecyclerViewPresenter.MyAdapterViewHolder holder, int position) {
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
            //调用接口的方法
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

    /**
     * RecyclerView万用的适配器
     */
    class MyAdapterViewHolder extends XRecyclerView.ViewHolder {

        T data;

        ViewDataBinding binding;

        public MyAdapterViewHolder(View itemView) {
            super(itemView);
        }

        public void setData(T data) {
            this.data = data;
            //调用接口的方法
            F.updateView(data, binding, getAdapterPosition());
        }
    }
}
