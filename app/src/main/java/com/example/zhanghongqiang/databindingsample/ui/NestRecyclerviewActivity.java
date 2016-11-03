package com.example.zhanghongqiang.databindingsample.ui;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhanghongqiang.databindingsample.R;
import com.example.zhanghongqiang.databindingsample.api.Api;
import com.example.zhanghongqiang.databindingsample.databinding.ActivityNestRecyclerviewBinding;
import com.example.zhanghongqiang.databindingsample.databinding.ItemMovieCircleBinding;
import com.example.zhanghongqiang.databindingsample.model.HttpResult;
import com.example.zhanghongqiang.databindingsample.model.Movie;
import com.example.zhanghongqiang.databindingsample.presenter.RecyclerViewContract;
import com.example.zhanghongqiang.databindingsample.presenter.RecyclerViewPresenter;
import com.example.zhanghongqiang.databindingsample.subscribers.OnNextOnError;

import java.util.List;

/**
 * Created by zhanghongqiang on 16/8/13  下午1:55
 * ToDo:
 */
public class NestRecyclerviewActivity extends BaseActivity implements RecyclerViewContract.IFLoadData, RecyclerViewContract.IFAdapter<Movie> {

    private ActivityNestRecyclerviewBinding mBinding;

    //布局填充器
    private LayoutInflater inflater;
    //列表代理
    private RecyclerViewPresenter recyclerViewPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_nest_recyclerview);
        inflater = getLayoutInflater();

        recyclerViewPresenter = RecyclerViewPresenter.with(this, this)
                .fullRecyclerView(mBinding.recyclerview)
                .build();
        //加载数据
        loadData();
    }


    @Override
    public int getViewType(int position) {
        return 0;
    }

    @Override
    public void loadData() {
        pendingSubscriptions.add(Api.toSubscribe( Api.getInstance().getRest().getTopMovie(
                1
                , 8)
                , new OnNextOnError<HttpResult<List<Movie>>>() {


                    @Override
                    public void onNext(HttpResult<List<Movie>> listHttpResult) {
                        recyclerViewPresenter.add(listHttpResult.getSubjects());
                    }


                    @Override
                    public void onError(Throwable e) {

                    }
                }));
    }


    @Override
    public void setData(@NonNull final Movie data, @NonNull ViewDataBinding binding) {
        ItemMovieCircleBinding b = (ItemMovieCircleBinding) binding;
        b.setMovie(data);

        b.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setTitle("xiaozhanghehe");
                int position = recyclerViewPresenter.indexOf(data);
                recyclerViewPresenter.notifyItemRangeRemoved(position);
            }
        });

    }

    @Override
    public ViewDataBinding createView(ViewGroup parent, int viewType) {
        return DataBindingUtil.inflate(inflater, R.layout.item_movie_circle, parent, false);
    }
}