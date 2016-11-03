package com.example.zhanghongqiang.databindingsample.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.RefreshTrigger;
import com.example.zhanghongqiang.databindingsample.view.FullyLinearLayoutManager;
import com.jcodecraeer.xrecyclerview.ArrowRefreshHeader;
import com.jcodecraeer.xrecyclerview.LoadingMoreFooter;

import java.util.List;

/**
 * Created by zhanghongqiang on 16/7/20  上午11:18
 * ToDo:列表代理者
 */
public class IRecyclerViewPresenter<T> extends RecyclerViewContract.XRDelegate {

    //列表的父布局
    FrameLayout mFrameLayout;

    //列表空布局的现实
    View mEmptyView;

    //xml里面的列表
    private IRecyclerView mRecyclerView;

    //万用的适配器
    private MyAdapter mAdapter;

    //头布局
    private View mHeaderView;

    //底部布局
    private View mFooterView;

    //分页,从0开始
    private int mPage = 0;

    //每一页的item个数,默认20条
    private int mCount = 20;


    public IRecyclerViewPresenter(RecyclerViewContract.IFLoadData L, RecyclerViewContract.IFAdapter F) {
        super(L, F);
    }

    /**
     * @return
     */
    public static IRecyclerViewPresenter with(RecyclerViewContract.IFLoadData L, RecyclerViewContract.IFAdapter F) {
        return new IRecyclerViewPresenter(L, F);
    }

    public IRecyclerViewPresenter recyclerView(@NonNull IRecyclerView recyclerView) {
        initVariable(recyclerView, null, null);
        linearLayoutManager();
        return this;
    }

    public IRecyclerViewPresenter recyclerView(@NonNull IRecyclerView recyclerView, FrameLayout frameLayout, View emptyView) {
        initVariable(recyclerView, frameLayout, emptyView);
        linearLayoutManager();
        return this;
    }


    public IRecyclerViewPresenter fullRecyclerView(@NonNull IRecyclerView recyclerView, FrameLayout frameLayout, View emptyView) {
        initVariable(recyclerView, frameLayout, emptyView);
        fullLinearLayoutManager();
        return this;
    }

    private void fullLinearLayoutManager() {
        //默认的layoutManager
        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(mRecyclerView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //设置layoutManager
        mRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * 初始化列表,列表父布局,需要显示的空布局
     *
     * @param recyclerView
     * @param frameLayout
     * @param emptyView
     */
    private void initVariable(@NonNull IRecyclerView recyclerView, FrameLayout frameLayout, View emptyView) {
        mRecyclerView = recyclerView;
        if (frameLayout != null && emptyView != null) {
            mFrameLayout = frameLayout;
            mEmptyView = emptyView;
            //空布局添加到父布局
            mFrameLayout.addView(mEmptyView);
        }
    }


    public View getEmptyView() {
        return mEmptyView;
    }

    /**
     * @param spanCount 网格布局的格数
     */
    public IRecyclerViewPresenter<T> recyclerView(IRecyclerView recyclerView, FrameLayout frameLayout, View emptyView, int spanCount) {
        initVariable(recyclerView, frameLayout, emptyView);
        gridLayoutManager(spanCount);
        return this;
    }

    /**
     * @param spanCount 网格布局的格数
     */
    public IRecyclerViewPresenter<T> recyclerView(IRecyclerView recyclerView, int spanCount) {
        initVariable(recyclerView, null, null);
        gridLayoutManager(spanCount);
        return this;
    }

    /**
     * @param spanCount   交错网格的格子数
     * @param orientation
     * @return
     */
    public IRecyclerViewPresenter<T> recyclerView(IRecyclerView recyclerView, int spanCount, int orientation) {
        initVariable(recyclerView, null, null);
        staggeredGridLayoutManager(spanCount, orientation);
        return this;
    }

    /**
     * @param spanCount   交错网格的格子数
     * @param orientation
     * @return
     */
    public IRecyclerViewPresenter<T> recyclerView(IRecyclerView recyclerView, FrameLayout frameLayout, View emptyView, int spanCount, int orientation) {
        initVariable(recyclerView, frameLayout, emptyView);
        staggeredGridLayoutManager(spanCount, orientation);
        return this;
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


    public IRecyclerViewPresenter headerView(@NonNull View headerView) {
        this.mHeaderView = headerView;
        return this;
    }

    public IRecyclerViewPresenter footerView(@NonNull View footerView) {
        this.mFooterView = footerView;
        return this;
    }

    /**
     * @param pullRefresh 头部是否下拉
     * @param loadingMore 底部加载更多
     * @return
     */
    public IRecyclerViewPresenter<T> cancelRefresh(boolean pullRefresh, boolean loadingMore) {
        if (mRecyclerView != null) {
            if (pullRefresh) {
//                MyRefreshHeader refreshHeader = new MyRefreshHeader();
////                refreshHeader.setProgressStyle(ProgressStyle.LineScaleParty);
////                refreshHeader.
//                mRecyclerView.setRefreshHeaderView(refreshHeader);
                mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        //下拉刷新,刷新成功后,清空原有数据
                        //页面设置为第一页
                        if (L != null) {
                            mPage = 0;
                            if (mEmptyView != null) {
                                mEmptyView.setVisibility(View.GONE);
                            }
                            L.loadData();
                            mRecyclerView.setRefreshing(true);
                        }
                    }
                });
            }
            if (loadingMore) {
                LoadingMoreFooter loadingMoreFooter = new LoadingMoreFooter(mRecyclerView.getContext());
//                loadingMoreFooter.setProgressStyle(ProgressStyle.LineScaleParty);
                mRecyclerView.setLoadMoreFooterView(loadingMoreFooter);
                mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {
                        if (L != null) {
                            //加载更多
                            L.loadData();
                            mRecyclerView.setRefreshing(true);
                        }
                    }

                });
            }
        }
        return this;
    }

    public IRecyclerViewPresenter<T> build() {
        //添加头部
        if (this.mHeaderView != null) {
            mRecyclerView.addHeaderView(mHeaderView);
        }
        //添加尾部
        if (this.mFooterView != null) {
            mRecyclerView.addFooterView(mFooterView);
        }
        //新建适配器
        mAdapter = new MyAdapter();
        //设置适配器
        mRecyclerView.setIAdapter(mAdapter);
        return this;
    }

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
    @Override
    public void reLoadData() {
        if (L != null) {
            mPage = 0;
            L.loadData();
        }
    }

    @Override
    public void add(List list) {
        //下拉刷新,多次请求首页的话,清空数据
        if (mPage == 1 || mPage == 0) {
            //第一页就没有数据,显示空数据
            if (list.size() == 0) {
                showEmptyView();
                clearData();
                //刷新完成,隐藏进度条...
                refreshComplete();
                return;
            } else {
                //有数据的话,清空原来的数据,防止数据重复添加
                clearData();
            }
        }
        //隐藏占位图
        hideEmptyView();
        //加入新的数据
        //一定要调用这个方法,因为XRecyclerView添加了头部,所以这个position+1
        int position = getDataList().size();
        if (mHeaderView == null) {
            mAdapter.addNewList(position + 1, list);
        } else {
            mAdapter.addNewList(position + 2, list);
        }
        //刷新完成,隐藏进度条...
        refreshComplete();
    }

    @Override
    public void clearData() {
        if (mAdapter != null) {
            if (mHeaderView == null) {
                mAdapter.clearList(1);
            } else {
                mAdapter.clearList(2);
            }
        }
    }


    //刷新完成,隐藏进度条...
    public void refreshComplete() {
        mRecyclerView.setRefreshing(false);
    }


    @Override
    public void notifyDataSetChanged() {
        if (mAdapter != null) {
            //数据如果为空的话,现实占位图
            if (mEmptyView != null && getDataList().size() == 0) {
                showEmptyView();
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * @param position 当前t在列表dataList的位置
     */
    @Override
    public void notifyItemChanged(int position) {
        if (position < 0) {
            return;
        }
        if (mAdapter != null) {
            //一定要调用这个方法,因为XRecyclerView添加了头部,所以这个position+1
            if (mHeaderView == null) {
                mAdapter.notifyItemRangeChanged(position + 1, 1);
            } else {
                mAdapter.notifyItemRangeChanged(position + 2, 1);
            }
        }
    }

    /**
     * @param position 当前t在列表dataList的位置
     */
    @Override
    public void notifyItemRangeRemoved(int position) {
        if (position < 0) {
            return;
        }
        if (mAdapter != null) {
            //一定要调用这个方法,因为XRecyclerView添加了头部,所以这个position+1
            getDataList().remove(position);
            if (mHeaderView == null) {
                mAdapter.notifyItemRangeRemoved(position + 1, 1);
            } else {
                mAdapter.notifyItemRangeRemoved(position + 2, 1);
            }
            if (getDataList().size() == 0) {
                showEmptyView();
            }
        }
    }

    /**
     * @param position 需要加入到列表的位置
     * @param o        列表的item数据
     */

    @Override
    public void notifyItemRangeInserted(int position, Object o) {
        if (position < 0) {
            return;
        }
        T t = (T) o;
        if (mAdapter != null) {
            //一定要调用这个方法,因为XRecyclerView添加了头部,所以这个position+1
            getDataList().add(position, t);
            if (mHeaderView == null) {
                mAdapter.notifyItemRangeInserted(position + 1, 1);
            } else {
                mAdapter.notifyItemRangeInserted(position + 2, 1);
            }
        }
    }

    public void showEmptyView() {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    private void hideEmptyView() {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.GONE);
        }
    }


    public class MyRefreshHeader extends ArrowRefreshHeader implements RefreshTrigger {


        public MyRefreshHeader(Context context, boolean scrollWhenFirst, boolean showArrow) {
            super(context, scrollWhenFirst, showArrow);
        }

        public MyRefreshHeader(Context context, AttributeSet attrs, boolean scrollWhenFirst) {
            super(context, attrs, scrollWhenFirst);
        }

        @Override
        public void onStart(boolean b, int i, int i1) {
            setState(STATE_DONE);
        }

        @Override
        public void onMove(boolean b, boolean b1, int i) {

        }

        @Override
        public void onRefresh() {
            setState(STATE_REFRESHING);
        }

        @Override
        public void onRelease() {
            setState(STATE_RELEASE_TO_REFRESH);
        }

        @Override
        public void onComplete() {
            setState(STATE_DONE);
        }

        @Override
        public void onReset() {
            setState(STATE_NORMAL);
        }
    }

}
