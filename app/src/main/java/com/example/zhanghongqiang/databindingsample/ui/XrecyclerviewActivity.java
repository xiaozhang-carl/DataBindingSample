package com.example.zhanghongqiang.databindingsample.ui;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhanghongqiang.databindingsample.R;
import com.example.zhanghongqiang.databindingsample.api.Api;
import com.example.zhanghongqiang.databindingsample.databinding.ActivityXrecyclerviewBinding;
import com.example.zhanghongqiang.databindingsample.databinding.ItemMovieBinding;
import com.example.zhanghongqiang.databindingsample.databinding.ViewEmptyBinding;
import com.example.zhanghongqiang.databindingsample.model.HttpResult;
import com.example.zhanghongqiang.databindingsample.model.Movie;
import com.example.zhanghongqiang.databindingsample.presenter.RecyclerViewContract;
import com.example.zhanghongqiang.databindingsample.presenter.XRecyclerViewPresenter;
import com.example.zhanghongqiang.databindingsample.subscribers.OnNextOnErrorNoMatch;

import java.util.List;

/**
 * Created by zhanghongqiang on 16/4/5  下午2:35
 * ToDo:
 */
public class XRecyclerviewActivity extends BaseActivity implements RecyclerViewContract.IFLoadData, RecyclerViewContract.IFAdapter<Movie> {

    private ActivityXrecyclerviewBinding binding;
    //布局填充器
    private LayoutInflater inflater;
    //列表代理
    private XRecyclerViewPresenter recyclerViewPresenter;

    private ViewEmptyBinding mEmptyBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_xrecyclerview);
        mEmptyBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.view_empty, null, false);
        inflater = getLayoutInflater();

        recyclerViewPresenter = XRecyclerViewPresenter.with(this, this)
                .recyclerView(binding.recyclerview, binding.frameLayout, mEmptyBinding.getRoot())
                .build();
        //空布局
        initEmptyView();
        //加载数据
        loadData();
    }

    private void initEmptyView() {
        //获取空布局
        //空布局的实现
        if (mEmptyBinding != null) {
            mEmptyBinding.button.setVisibility(View.VISIBLE);
            mEmptyBinding.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewPresenter.reLoadData();
                }
            });
        }
    }

    @Override
    public int getViewType(int position) {
        return 0;
    }

    @Override
    public void loadData() {
        pendingSubscriptions.add(Api.toSubscribe(this, Api.getInstance().getRest().getTopMovie(
                recyclerViewPresenter.nextPage()
                , recyclerViewPresenter.getCount())
                , new OnNextOnErrorNoMatch<HttpResult<List<Movie>>>() {


                    @Override
                    public void onNext(HttpResult<List<Movie>> listHttpResult) {
                        recyclerViewPresenter.add(listHttpResult.getSubjects());
                    }

                    @Override
                    public void notMatch(HttpResult<List<Movie>> listHttpResult) {
                        Log.i("123", "result not match");

                    }


                    @Override
                    public void onError(Throwable e) {
                        recyclerViewPresenter.refreshComplete();
                    }
                }));
    }


    @Override
    public void setData(@NonNull final Movie data, @NonNull ViewDataBinding binding) {
        ItemMovieBinding b = (ItemMovieBinding) binding;
        b.setMovie(data);

        b.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int position = recyclerViewPresenter.indexOf(data);
//                recyclerViewPresenter.notifyItemRangeRemoved(position);
                recyclerViewPresenter.notifyItemRangeInserted(0, data);
            }
        });

    }

    @Override
    public ViewDataBinding createView(ViewGroup parent, int viewType) {
        return DataBindingUtil.inflate(inflater, R.layout.item_movie, parent, false);
    }
}
