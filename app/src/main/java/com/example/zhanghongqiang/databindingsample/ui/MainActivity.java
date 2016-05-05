package com.example.zhanghongqiang.databindingsample.ui;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.zhanghongqiang.databindingsample.R;
import com.example.zhanghongqiang.databindingsample.api.ApiManager;
import com.example.zhanghongqiang.databindingsample.databinding.ActivityMainBinding;
import com.example.zhanghongqiang.databindingsample.databinding.ItemMovieBinding;
import com.example.zhanghongqiang.databindingsample.model.HttpResult;
import com.example.zhanghongqiang.databindingsample.model.Movie;
import com.example.zhanghongqiang.databindingsample.presenter.RecyclerViewPresenter;
import com.example.zhanghongqiang.databindingsample.subscribers.OnNextOnError;
import com.example.zhanghongqiang.databindingsample.view.IFListview;

import java.util.List;

/**
 * Created by zhanghongqiang on 16/4/5  下午2:35
 * ToDo:
 */
public class MainActivity extends BaseActivity implements IFListview<Movie> {

    ActivityMainBinding binding;
    //布局填充器
    LayoutInflater inflater;
    //列表代理
    RecyclerViewPresenter recyclerViewPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        inflater = getLayoutInflater();
        recyclerViewPresenter = RecyclerViewPresenter.with(this, this)
                .recyclerView(binding.listview)
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
        //获取数据
        subscriptionArrayList.add(
                ApiManager.toSubscribe(this
                        , ApiManager.getInstance().getRest().getTopMovie(recyclerViewPresenter.nextPage(), recyclerViewPresenter.getPageSize())
                        , new OnNextOnError<HttpResult<List<Movie>>>() {
                            @Override
                            public void onNext(HttpResult<List<Movie>> listHttpResult) {
                                recyclerViewPresenter.success(listHttpResult.getSubjects());
                            }

                            @Override
                            public void onError() {
                                recyclerViewPresenter.refreshComplete();
                            }
                        }));
    }

    @Override
    public void updateView(@NonNull Movie data, @NonNull ViewDataBinding binding, int position) {
        ItemMovieBinding b = (ItemMovieBinding) binding;
        b.setMovie(data);

    }

    @Override
    public ViewDataBinding createView(ViewGroup parent, int viewType) {
        return DataBindingUtil.inflate(inflater, R.layout.item_movie, parent, false);
    }
}
