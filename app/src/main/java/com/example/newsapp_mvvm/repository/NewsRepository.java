package com.example.newsapp_mvvm.repository;

import android.util.Log;

import com.example.newsapp_mvvm.Utils;
import com.example.newsapp_mvvm.api.ApiClient;
import com.example.newsapp_mvvm.api.ApiInterface;
import com.example.newsapp_mvvm.api.NewsListCallback;
import com.example.newsapp_mvvm.models.Articles;
import com.example.newsapp_mvvm.models.News;
import com.example.newsapp_mvvm.models.Source;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepository {
    private static final String TAG = "NewsRepository";
    private static final String COMMON_TAG = "mAppLog";
    private static final String API_KEY = "3b174566b9a547a5bfb58c46d927b14f";

    private static NewsRepository instance = null;
    private NewsListCallback mNewsListCallback = null;

    public synchronized static NewsRepository getInstance(){
        if(instance == null){
            instance = new NewsRepository();
        }
        return instance;
    }

    public void setNewsListCallback(NewsListCallback callback){
        mNewsListCallback = callback;
    }

    public void loadJson(String keyword){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        String country = Utils.getCountry();
        String language = Utils.getLanguage();

        Call<News> call;
        if(keyword.length() > 0){
            call = apiInterface.getNewsSearch(keyword,language,"publishedAt",API_KEY);
        }else{
            call = apiInterface.getNews(country,API_KEY);
        }
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<Articles> newsList = response.body().getArticles();

                    mNewsListCallback.setNews(newsList);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.d(COMMON_TAG,TAG+" loadJson onFailure: "+t.getMessage());
            }
        });
    }



}
