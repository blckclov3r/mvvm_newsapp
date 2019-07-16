package com.example.newsapp_mvvm.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import com.example.newsapp_mvvm.R;
import com.example.newsapp_mvvm.adapter.NewsRecyclerAdapter;
import com.example.newsapp_mvvm.models.Articles;
import com.example.newsapp_mvvm.viewmodel.NewsViewModel;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity implements NewsRecyclerAdapter.OnItemClickInterface, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainActivity";
    private static final String COMMON_TAG = "mAppLog";
    private String mQuery = "";

    private RecyclerView mRecyclerView;
    private NewsRecyclerAdapter mRecyclerAdapter;
    private NewsViewModel mNewsViewModel;

    private SwipeRefreshLayout mSwipeRefreshLayout;
//    private RelativeLayout mErrorLayout;
//    private Button mRetry_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
//        mErrorLayout = findViewById(R.id.errorLayout);
//        mRetry_button = findViewById(R.id.btnRetry);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mNewsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);

        mNewsViewModel.loadJson("");
        initRecycler();


        mNewsViewModel.getNewsList().observe(this, new Observer<List<Articles>>() {
            @Override
            public void onChanged(List<Articles> articles) {
                mRecyclerAdapter.setArticlesList(articles);
            }
        });

        mNewsViewModel.getSwipeCondition().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.d(COMMON_TAG, TAG + " aBoolean: " + aBoolean);
                mSwipeRefreshLayout.setRefreshing(!aBoolean);
                if (!aBoolean) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }, 3000);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initRecycler() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerAdapter = new NewsRecyclerAdapter();
        mRecyclerAdapter.setOnItemClickInterface(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mRecyclerAdapter);

    }

    @Override
    public void onItemClick(View view, int position) {
        ImageView imageView = view.findViewById(R.id.news_imageView);
        Log.d(COMMON_TAG, TAG + " position: " + position);
        Articles articles = mRecyclerAdapter.getArticlesList().get(position);


        Intent intent = new Intent(MainActivity.this, NewsDetailActivity.class);
        intent.putExtra("articles", articles);
        intent.putExtra("source", articles.getSource().getName());
        Pair<View, String> pair = Pair.create((View) imageView, ViewCompat.getTransitionName(imageView));
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                MainActivity.this,
                pair
        );
        startActivity(intent, optionsCompat.toBundle());
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        mNewsViewModel.loadJson("");
    }

    public void onLoadingSwipeRefresh(String keyword) {
        mNewsViewModel.loadJson(keyword);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search Latest News");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mQuery = query;
                onLoadingSwipeRefresh(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mQuery = newText;
                return false;
            }
        });

        searchMenuItem.getIcon().setVisible(false, false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onRefresh() {
        mNewsViewModel.loadJson(mQuery);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.exit) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
