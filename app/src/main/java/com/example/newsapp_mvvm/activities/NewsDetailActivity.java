package com.example.newsapp_mvvm.activities;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.newsapp_mvvm.R;
import com.example.newsapp_mvvm.Utils;
import com.example.newsapp_mvvm.models.Articles;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class NewsDetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private static final String TAG = "NewsDetailActivity";
    private static final String COMMON_TAG = "mAppLog";

    private ImageView mImageView;
    private TextView mAppBar_title_tv, mAppBar_subtitle_tv, mDate_tv, mTime_tv, mTitle_tv;
    private RelativeLayout mDate_behaviour;
    private Toolbar mToolbar;
    private AppBarLayout mAppbar;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        mAppBar_title_tv = findViewById(R.id.title_on_appbar);
        mAppBar_subtitle_tv = findViewById(R.id.subtitle_on_appbar);
        mDate_tv = findViewById(R.id.date);
        mTime_tv = findViewById(R.id.time);
        mTitle_tv = findViewById(R.id.title);
        mDate_behaviour = findViewById(R.id.date_behavior);
        mImageView = findViewById(R.id.imageView);
        mAppbar = findViewById(R.id.appbar);
        mWebView = findViewById(R.id.webView);
        mAppbar.addOnOffsetChangedListener(this);

        if (getIntent().getParcelableExtra("articles") != null) {

            Articles articles = getIntent().getParcelableExtra("articles");
            String source_name = getIntent().getStringExtra("source");

            initWebView(articles.getUrl());
            RequestOptions options = new RequestOptions()
                    .error(Utils.getRandomDrawbleColor())
                    .centerCrop();
            Glide.with(this)
                    .asBitmap()
                    .load(articles.getUrlToImage())
                    .dontAnimate()
                    .apply(options)
                    .into(mImageView);

            mAppBar_title_tv.setText(source_name);
            mAppBar_subtitle_tv.setText(articles.getUrl());
            mDate_tv.setText(Utils.DateFormat(articles.getPublishedAt()));
            mTitle_tv.setText(articles.getTitle());


            String author;
            if (articles.getAuthor() != null) {
                author = " \u2022 " + articles.getAuthor();
            } else {
                author = "";
            }
            mTime_tv.setText(source_name + author + " \u2022 " + Utils.DateToTimeFormat(articles.getPublishedAt()));
        }
    }


    private void initWebView(String url){
        mWebView.loadUrl(url);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);

        mWebView.setWebViewClient(new WebViewClient());

        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                DownloadManager.Request mRequest = new DownloadManager.Request(Uri.parse(url));
                mRequest.allowScanningByMediaScanner();
                mRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                manager.enqueue(mRequest);

                Toast.makeText(getApplicationContext(), "Your file is donwloading...", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        if(offset == 0){
            //fully expand
            mDate_behaviour.setVisibility(View.VISIBLE);
        }else{
            //not full expand
            mDate_behaviour.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }
}
