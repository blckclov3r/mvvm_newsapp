package com.example.newsapp_mvvm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.newsapp_mvvm.adapter.NewsRecyclerAdapter;
import com.example.newsapp_mvvm.models.Articles;
import com.example.newsapp_mvvm.viewmodel.NewsViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String COMMON_TAG= "mAppLog";

    private RecyclerView mRecyclerView;
    private NewsRecyclerAdapter mRecyclerAdapter;

    private NewsViewModel mNewsViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNewsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        mNewsViewModel.loadJson("");
        mNewsViewModel.getNewsList().observe(this, new Observer<List<Articles>>() {
            @Override
            public void onChanged(List<Articles> articles) {
                Log.d(COMMON_TAG,TAG+" onChanged");
                mRecyclerAdapter.setArticlesList(articles);
            }
        });

        initRecycler();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(COMMON_TAG,TAG+" onStart");
    }

    private void initRecycler(){
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerAdapter = new NewsRecyclerAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mRecyclerAdapter);

    }
}
