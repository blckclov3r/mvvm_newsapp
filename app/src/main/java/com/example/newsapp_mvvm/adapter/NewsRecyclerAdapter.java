package com.example.newsapp_mvvm.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.example.newsapp_mvvm.R;
import com.example.newsapp_mvvm.Utils;
import com.example.newsapp_mvvm.models.Articles;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder>{

    private List<Articles> articlesList = new ArrayList<>();


    public interface OnItemClickInterface{
        void onItemClick(View view,int position);
    }
    private OnItemClickInterface mListener;
    public void setOnItemClickInterface(OnItemClickInterface listener){
        mListener = listener;
    }

    public void setArticlesList(List<Articles> articlesList){
        this.articlesList = articlesList;
        notifyDataSetChanged();
    }

    public List<Articles> getArticlesList(){
        return articlesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item,parent,false);
        return new ViewHolder(view,mListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Articles articles = articlesList.get(position);


        RequestOptions options = new RequestOptions()
                .placeholder(Utils.getRandomDrawbleColor())
                .error(Utils.getRandomDrawbleColor())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop();

        Glide.with(holder.itemView)
                .setDefaultRequestOptions(options)
                .asBitmap()
                .load(articles.getUrlToImage())
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        holder.progress_load_photo.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progress_load_photo.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.news_imageView);


        holder.author_tv.setText(articles.getAuthor());
        holder.publishedAt_tv.setText(Utils.DateToTimeFormat(articles.getPublishedAt()));
        holder.title_tv.setText(articles.getTitle());
        holder.desc_tv.setText(articles.getDescription());
        holder.source_tv.setText(articles.getSource().getName());
        holder.time_tv.setText(" \u2022 "+Utils.DateToTimeFormat(articles.getPublishedAt()));
    }

    @Override
    public int getItemCount() {
        if(articlesList.size() > 0) {
            return articlesList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView news_imageView;
        private TextView author_tv,publishedAt_tv,title_tv,desc_tv,source_tv,time_tv;
        private ProgressBar progress_load_photo;
        private OnItemClickInterface listener;
        public ViewHolder(@NonNull View itemView,OnItemClickInterface listener) {
            super(itemView);
            this.listener = listener;
            news_imageView = itemView.findViewById(R.id.news_imageView);
            author_tv = itemView.findViewById(R.id.author);
            publishedAt_tv = itemView.findViewById(R.id.publishedAt);
            title_tv = itemView.findViewById(R.id.title);
            desc_tv = itemView.findViewById(R.id.desc);
            source_tv = itemView.findViewById(R.id.source);
            time_tv = itemView.findViewById(R.id.time);
            progress_load_photo = itemView.findViewById(R.id.progress_load_photo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(listener!=null && position != RecyclerView.NO_POSITION) {
                listener.onItemClick(v, position);
            }
        }
    }


}
