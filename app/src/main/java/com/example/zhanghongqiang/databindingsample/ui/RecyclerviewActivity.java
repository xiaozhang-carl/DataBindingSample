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
import com.example.zhanghongqiang.databindingsample.databinding.ActivityRecyclerviewBinding;
import com.example.zhanghongqiang.databindingsample.databinding.ItemMovieBinding;
import com.example.zhanghongqiang.databindingsample.model.HttpResult;
import com.example.zhanghongqiang.databindingsample.model.Movie;
import com.example.zhanghongqiang.databindingsample.presenter.RecyclerViewContract;
import com.example.zhanghongqiang.databindingsample.presenter.RecyclerViewPresenter;
import com.example.zhanghongqiang.databindingsample.subscribers.OnNextOnErrorNoMatch;

import java.util.List;

import rx.Observable;

/**
 * Created by zhanghongqiang on 16/8/13  下午1:55
 * ToDo:
 */
public class RecyclerviewActivity extends BaseActivity implements RecyclerViewContract.IFLoadData, RecyclerViewContract.IFAdapter<Movie> {

    private ActivityRecyclerviewBinding mBinding;

    //布局填充器
    LayoutInflater inflater;
    //列表代理
    RecyclerViewPresenter recyclerViewPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_recyclerview);
        inflater = getLayoutInflater();

        recyclerViewPresenter = RecyclerViewPresenter.with(this, this)
                .recyclerView(mBinding.recyclerview)
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
        Observable<HttpResult<List<Movie>>> observable = Api.getInstance().getRest().getTopMovie(1, 5);
//        observable.
        pendingSubscriptions.add(Api.toSubscribe(this, Api.getInstance().getRest().getTopMovie(
                1
                , 5)
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
                data.setTitle("xiaozhanghehe");
                int position = recyclerViewPresenter.indexOf(data);
                recyclerViewPresenter.notifyItemRangeRemoved(position);
            }
        });

    }

    @Override
    public ViewDataBinding createView(ViewGroup parent, int viewType) {
        return DataBindingUtil.inflate(inflater, R.layout.item_movie, parent, false);
    }
}