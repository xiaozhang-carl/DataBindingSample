package com.example.zhanghongqiang.databindingsample.presenter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.example.zhanghongqiang.databindingsample.view.FullyLinearLayoutManager;

import java.util.List;

/**
 * Created by zhanghongqiang on 16/7/20  上午11:18
 * ToDo:列表代理者
 */
public class RecyclerViewPresenter<T> extends RecyclerViewContract.XRDelegate {


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
            mAdapter.clearList();
            return;
        } else {
            //有数据的话,清空原来的数据,防止数据重复添加
            mAdapter.clearList();

        }
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

    public RecyclerViewPresenter(RecyclerViewContract.IFLoadData L, RecyclerViewContract.IFAdapter F) {
        super(L, F);
    }

    /**
     * @return
     */
    public static RecyclerViewPresenter with(RecyclerViewContract.IFLoadData L, RecyclerViewContract.IFAdapter F) {
        return new RecyclerViewPresenter(L, F);
    }

    public RecyclerViewPresenter recyclerView(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
        linearLayoutManager();
        return this;
    }


    public RecyclerViewPresenter fullRecyclerView(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
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
     * @param spanCount 网格布局的格数
     */
    public RecyclerViewPresenter<T> recyclerView(RecyclerView recyclerView, int spanCount) {
        this.mRecyclerView = recyclerView;
        gridLayoutManager(spanCount);
        return this;
    }

    /**
     * @param spanCount   交错网格的格子数
     * @param orientation
     * @return
     */
    public RecyclerViewPresenter<T> recyclerView(RecyclerView recyclerView, int spanCount, int orientation) {
        this.mRecyclerView = recyclerView;
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
            //数据如果为空的话
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * @param position 当前t在列表dataList的位置
     */
    @Override
    public void notifyItemChanged(int position) {
        if (mAdapter != null) {
            mAdapter.notifyItemRangeChanged(position, 1);
        }
    }

    /**
     * @param position 当前t在列表dataList的位置
     */
    @Override
    public void notifyItemRangeRemoved(int position) {
        if (mAdapter != null) {
            getDataList().remove(position);
            mAdapter.notifyItemRangeRemoved(position, 1);
        }
    }

    @Override
    public void notifyItemRangeInserted(int position, Object o) {
        T t= (T) o;
        if (mAdapter != null) {
            getDataList().add(position,t);
            mAdapter.notifyItemRangeInserted(position, 1);
        }
    }


}
