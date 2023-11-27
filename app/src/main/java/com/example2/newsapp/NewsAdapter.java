package com.example2.newsapp;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example2.newsapp.helper.ItemTouchHelperAdapter;
import com.example2.newsapp.helper.ItemTouchHelperViewHolder;
import com.example2.newsapp.model.NewsData;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> implements ItemTouchHelperAdapter {
    private  Context context;
    private ArrayList<NewsData> newsList = new ArrayList<>() ;
    private  DatabaseHelper databaseHelper;
    public NewsAdapter(Context context, ArrayList<NewsData> allData) {
        this.context  = context;
        this.newsList = allData;
    }
    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_news,parent,false);
        return new NewsViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsData data = newsList.get(position);
        holder.tvNewsTitle.setText(data.getTitle());
        holder.tvNewsDesc.setText(data.getDesc());
        Glide
                .with(context).load(data.getImageUrl()).placeholder(R.drawable.icon_placeholder)
                .into(holder.ivNewsImage);
        }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        NewsData news = newsList.remove(fromPosition);
        newsList.add(toPosition > fromPosition ? toPosition - 1 : toPosition, news);
        notifyItemMoved(fromPosition, toPosition);

    }

    @Override
    public void onItemDismiss(int position) {
        NewsData news = newsList.remove(position);
        databaseHelper = new DatabaseHelper(context.getApplicationContext());
        databaseHelper.insertData(news);
        Log.d("SWIPED","TESTTTT");
    }
    public class NewsViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        ImageView ivNewsImage;
        TextView tvNewsTitle,tvNewsDesc;
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            ivNewsImage = itemView.findViewById(R.id.ivNewsIVN);
            tvNewsTitle = itemView.findViewById(R.id.tvNewsTitleIV);
            tvNewsDesc = itemView.findViewById(R.id.tvNewsDescIV);
        }

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }
    }
}
