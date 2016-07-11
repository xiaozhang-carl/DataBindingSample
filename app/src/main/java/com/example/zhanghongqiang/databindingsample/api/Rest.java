package com.example.zhanghongqiang.databindingsample.api;


import com.example.zhanghongqiang.databindingsample.model.HttpResult;
import com.example.zhanghongqiang.databindingsample.model.Movie;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


/**
 * Created by zhanghongqiang on 16/3/31  下午3:58
 * ToDo:网络访问
 */
public interface Rest {

    @GET("top250")
    Observable<HttpResult<List<Movie>>> getTopMovie(
            @Query("start") int start,
            @Query("count") int count
    );
}
