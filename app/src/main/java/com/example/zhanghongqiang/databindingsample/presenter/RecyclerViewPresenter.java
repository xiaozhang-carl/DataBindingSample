package com.example.zhanghongqiang.databindingsample.presenter;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.example.zhanghongqiang.databindingsample.databinding.ViewEmptyBinding;
import com.example.zhanghongqiang.databindingsample.databinding.ViewRecyclerviewBinding;
import com.example.zhanghongqiang.databindingsample.view.FullyLinearLayoutManager;

import java.util.List;

/**
 * Created by zhanghongqiang on 16/7/20  上午11:18
 * ToDo:列表代理者
 */
public class RecyclerViewPresenter<T> extends RecyclerviewContract.XRDelegate {



    //databinding的好处是可以减少自定义view,这是一个包含列表,空布局的xml
    ViewRecyclerviewBinding mBinding;

    //ViewRecyclerviewBinding里面的空布局
    ViewEmptyBinding mEmptyBinding;

    //xml里面的列表
    private RecyclerView mRecyclerView;

    //万用的适配器
    private MyAdapter mAdapter;


    //获取数据列表
    public List<T> getDataList() {
        return mAdapter.mDatas;
    }

    public int indexOf(T t) {
        return getDataList().indexOf(t);
    }



    //加载成功的结果显示
    public void success(List<T> list) {

        //没有数据,显示空数据
        if (list.size() == 0) {
            showEmptyView();
            mAdapter.clearList();
            return;
        } else {
            //有数据的话,清空原来的数据,防止数据重复添加
            mAdapter.clearList();

        }
        //隐藏占位图
        hideEmptyView();
        //加入新的数据
        mAdapter.addNewList(list);

    }



    //重新加载数据
    @Override
    public void reLoadData() {
        if (L != null) {
            L.loadData();
        }
    }


    public void clearData() {
        if (mAdapter != null) {
            mAdapter.clearList();
        }
    }

    public RecyclerViewPresenter(RecyclerviewContract.IFLoadData L, RecyclerviewContract.IFListview F) {
        super(L,F);
    }

    /**
     * @return
     */
    public static RecyclerViewPresenter with(RecyclerviewContract.IFLoadData L, RecyclerviewContract.IFListview F) {
        return new RecyclerViewPresenter(L, F);
    }

    public RecyclerViewPresenter recyclerView(@NonNull ViewRecyclerviewBinding binding) {
        initVariable(binding);
        linearLayoutManager();
        return this;
    }


    public RecyclerViewPresenter fullRecyclerView(ViewRecyclerviewBinding binding) {
        initVariable(binding);
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
    public RecyclerViewPresenter<T> recyclerView(@NonNull ViewRecyclerviewBinding binding, int spanCount) {
        initVariable(binding);
        gridLayoutManager(spanCount);
        return this;
    }

    /**
     * @param spanCount   交错网格的格子数
     * @param orientation
     * @return
     */
    public RecyclerViewPresenter<T> recyclerView(@NonNull ViewRecyclerviewBinding binding, int spanCount, int orientation) {
        initVariable(binding);
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


    public RecyclerViewPresenter emptyTip(@NonNull String tip) {
        if (mEmptyBinding != null) {
            mEmptyBinding.setTip(tip);
        }
        return this;
    }


    public RecyclerViewPresenter<T> build() {

        //新建适配器
        mAdapter = new MyAdapter();
        //设置适配器
        mRecyclerView.setAdapter(mAdapter);
        return this;
    }

    @Override
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
    @Override
    public void notifyItemChanged(int position) {
        if (mAdapter != null) {
            //一定要调用这个方法,因为XRecyclerView添加了头部,所以这个position+1
            mAdapter.notifyItemRangeChanged(position + 1, 1);
        }
    }

    /**
     * @param position 当前t在列表dataList的位置
     */
    @Override
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

}
