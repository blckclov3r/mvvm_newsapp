package com.example.newsapp_mvvm.viewmodel;

import android.app.Application;

import com.example.newsapp_mvvm.api.NewsListCallback;
import com.example.newsapp_mvvm.models.Articles;
import com.example.newsapp_mvvm.repository.NewsRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class NewsViewModel extends AndroidViewModel implements NewsListCallback {
    private NewsRepository mNewsRepository;
    private MutableLiveData<List<Articles>> mNewsList = new MutableLiveData<>();

    public NewsViewModel(@NonNull Application application) {
        super(application);
        mNewsRepository = NewsRepository.getInstance();
        mNewsRepository.setNewsListCallback(this);
    }


    @Override
    public void setNews(List<Articles> newsList) {
        mNewsList.setValue(newsList);
    }

    public LiveData<List<Articles>> getNewsList(){
        return mNewsList;
    }

    public void loadJson(String keyword){
        mNewsRepository.loadJson(keyword);
    }

    public LiveData<Boolean> getSwipeCondition(){
        return mNewsRepository.getSwipeCondition();
    }


}
