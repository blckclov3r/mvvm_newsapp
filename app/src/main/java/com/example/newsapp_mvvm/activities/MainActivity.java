package com.example.newsapp_mvvm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.newsapp_mvvm.R;
import com.example.newsapp_mvvm.adapter.NewsRecyclerAdapter;
import com.example.newsapp_mvvm.models.Articles;
import com.example.newsapp_mvvm.viewmodel.NewsViewModel;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements NewsRecyclerAdapter.OnItemClickInterface {

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
        mRecyclerAdapter.setOnItemClickInterface(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mRecyclerAdapter);

    }

    @Override
    public void onItemClick(View view, int position) {
        ImageView imageView = view.findViewById(R.id.news_imageView);
        Log.d(COMMON_TAG,TAG+" position: "+position);
        Articles articles = mRecyclerAdapter.getArticlesList().get(position);


        Intent intent = new Intent(MainActivity.this,NewsDetailActivity.class);
        intent.putExtra("articles",articles);
        intent.putExtra("source",articles.getSource().getName());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
