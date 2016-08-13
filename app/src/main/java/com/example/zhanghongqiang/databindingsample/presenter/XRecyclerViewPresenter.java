package com.example.zhanghongqiang.databindingsample.presenter;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhanghongqiang.databindingsample.databinding.ViewEmptyBinding;
import com.example.zhanghongqiang.databindingsample.databinding.ViewRecyclerviewBinding;
import com.example.zhanghongqiang.databindingsample.view.FullyLinearLayoutManager;
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

    XRecyclerviewContract.IFLoadData L;

    //databinding的好处是可以减少自定义view,这是一个包含列表,空布局的xml
    ViewRecyclerviewBinding mBinding;

    //ViewRecyclerviewBinding里面的空布局
    ViewEmptyBinding mEmptyBinding;

    //xml里面的列表
    private XRecyclerView mRecyclerView;

    //万用的适配器
    private MyAdapter mAdapter;

    //头布局
    private View mHeaderView;

    //底部布局
    private View mFooterView;

    //分页,从0开始
    private int mPage = 0;

    //每一页的item个数,默认20条
    private int mCount = 5;


    //下一页,获取数据的时候调用
    public int nextPage() {
        return ++mPage;
    }


    public int getPage() {
        return mPage;
    }



    //获取数据列表
    public List<T> getDataList() {
        return mAdapter.mDatas;
    }

    public int indexOf(T t) {
        return getDataList().indexOf(t);
    }

    //返回每页的条数
    public int getCount() {
        return mCount;
    }

    //设置每页的条数
    public void setCount(int count) {
        this.mCount = count;
    }

    //设置页码,配合分页使用
    public void setPage(int mPage) {
        this.mPage = mPage;
    }


    //重新加载数据
    public void reLoadData() {
        if (L != null) {
            mPage = 0;
            L.loadData();
        }
    }

    //加载成功的结果显示
    public void success(List<T> list) {

        //下拉刷新,多次请求首页的话,清空数据
        if (mPage == 1 || mPage == 0) {
            //第一页就没有数据,显示空数据
            if (list.size() == 0) {
                showEmptyView();
                mAdapter.clearList();
                //刷新完成,隐藏进度条...
                refreshComplete();
                return;
            } else {
                //有数据的话,清空原来的数据,防止数据重复添加
                mAdapter.clearList();
            }
        }
        //隐藏占位图
        hideEmptyView();
        //加入新的数据
        mAdapter.addNewList(list);
        //刷新完成,隐藏进度条...
        refreshComplete();
    }


    //刷新完成,隐藏进度条...
    public void refreshComplete() {
        if (mPage <= 1) {
            mRecyclerView.refreshComplete();
        } else {
            mRecyclerView.loadMoreComplete();
        }

    }

    public void clearData() {
        if (mAdapter != null) {
            mAdapter.clearList();
        }
    }

    public XRecyclerViewPresenter(XRecyclerviewContract.IFLoadData L, XRecyclerviewContract.IFListview F) {
        this.F = F;
        this.L = L;
    }

    /**
     * @return
     */
    public static XRecyclerViewPresenter with(XRecyclerviewContract.IFLoadData L, XRecyclerviewContract.IFListview F) {
        return new XRecyclerViewPresenter(L, F);
    }

    public XRecyclerViewPresenter recyclerView(@NonNull ViewRecyclerviewBinding binding) {
        initVariable(binding);
        linearLayoutManager();
        setRefreshLoadMore();
        setRefreshLoadingMoreProgressStyle();
        return this;
    }


    public XRecyclerViewPresenter fullRecyclerView(ViewRecyclerviewBinding binding) {
        initVariable(binding);
        fullLinearLayoutManager();
        setRefreshLoadMore();
        setRefreshLoadingMoreProgressStyle();
        return this;
    }

    private void fullLinearLayoutManager() {
        //默认的layoutManager
        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(mRecyclerView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //设置layoutManager
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void initVariable(@NonNull ViewRecyclerviewBinding binding) {
        mBinding = binding;
        mRecyclerView = mBinding.recyclerview;
        mEmptyBinding = mBinding.viewStub;
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

    


    private void setRefreshLoadMore() {
        //设置下拉和加在更多的事件
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //下拉刷新,刷新成功后,清空原有数据
                //页面设置为第一页
                if (L != null) {
                    mPage = 0;
                    if (mEmptyBinding.getRoot() != null) {
                        mEmptyBinding.getRoot().setVisibility(View.GONE);
                    }
                    L.loadData();
                }
            }

            @Override
            public void onLoadMore() {
                if (L != null) {
                    //加载更多
                    L.loadData();
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
        this.mHeaderView = headerView;
        return this;
    }

    public XRecyclerViewPresenter footerView(@NonNull View footerView) {
        this.mFooterView = footerView;
        return this;
    }

    public XRecyclerViewPresenter emptyTip(@NonNull String tip) {
        if (mEmptyBinding != null) {
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
        if (this.mHeaderView != null) {
            mRecyclerView.addHeaderView(mHeaderView);
        }
        //添加尾部
        if (this.mFooterView != null) {
            mRecyclerView.addFootView(mFooterView);
        }
        //新建适配器
        mAdapter = new MyAdapter();
        //设置适配器
        mRecyclerView.setAdapter(mAdapter);
        return this;
    }

    public void notifyDataSetChanged() {
        if (mAdapter != null) {
            //数据如果为空的话,现实占位图
            if (mEmptyBinding.getRoot() != null && getDataList().size() == 0) {
                showEmptyView();
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * @param position 当前t在列表dataList的位置
     */
    public void notifyItemChanged(int position) {
        if (mAdapter != null) {
            //一定要调用这个方法,因为XRecyclerView添加了头部,所以这个position+1
            mAdapter.notifyItemRangeChanged(position + 1, 1);
        }
    }

    /**
     * @param position 当前t在列表dataList的位置
     */
    public void notifyItemRangeRemoved(int position) {
        if (mAdapter != null) {
            //一定要调用这个方法,因为XRecyclerView添加了头部,所以这个position+1
            mAdapter.notifyItemRangeRemoved(position + 1, 1);
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

        ArrayList<T> mDatas = new ArrayList<>();

        @Override
        public XRecyclerViewPresenter.MyAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //调用借口的方法
            ViewDataBinding binding = F.createView(parent, viewType);
            XRecyclerViewPresenter.MyAdapterViewHolder viewHolder = new XRecyclerViewPresenter.MyAdapterViewHolder(binding.getRoot());
            viewHolder.mViewDataBinding = binding;

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(XRecyclerViewPresenter.MyAdapterViewHolder holder, int position) {
            holder.setData(getItem(position));
        }

        private T getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public int getItemCount() {
            return mDatas!=null?mDatas.size():0;
        }

        @Override
        public int getItemViewType(int position) {
            //调用接口的方法
            return F.getViewType(position);
        }

        public void clearList() {
            mDatas.clear();
            notifyDataSetChanged();
        }

        public void addNewList(List<T> list) {
            if (list != null && list.size() > 0) {
                mDatas.addAll(list);
                notifyDataSetChanged();
            }
        }

    }

    /**
     * RecyclerView万用的适配器
     */
    class MyAdapterViewHolder extends XRecyclerView.ViewHolder {


        ViewDataBinding mViewDataBinding;

        public MyAdapterViewHolder(View itemView) {
            super(itemView);
        }

        public void setData(T data) {
            //调用接口的方法
            F.updateView(data, mViewDataBinding);
        }
    }
}
