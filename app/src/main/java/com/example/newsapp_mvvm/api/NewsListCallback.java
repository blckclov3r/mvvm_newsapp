package com.example.newsapp_mvvm.api;

import com.example.newsapp_mvvm.models.Articles;

import java.util.List;

public interface NewsListCallback {
    void setNews(List<Articles> newsList);
}
