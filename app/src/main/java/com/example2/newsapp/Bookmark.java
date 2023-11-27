package com.example2.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example2.newsapp.model.NewsData;

import java.util.ArrayList;

public class Bookmark extends AppCompatActivity {
    TextView tvHeadingAB;
    SwipeRefreshLayout swipeRefreshAB;
    RecyclerView rvBookmark;
    ProgressBar progressBarAB;
    private ArrayList<NewsData> newsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e ) {

        }
        InitViews();
        loadNews();

    }

    private void InitViews() {
        swipeRefreshAB = findViewById(R.id.swipeRefreshAB);
        rvBookmark = findViewById(R.id.rvBookmark);
        progressBarAB = findViewById(R.id.progressBarAB);
        tvHeadingAB = findViewById(R.id.tvHeadingAB);

        swipeRefreshAB.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNews();
                swipeRefreshAB.setRefreshing(false);
            }
        });
        rvBookmark.setLayoutManager(new LinearLayoutManager(this));
        rvBookmark.setItemAnimator(new DefaultItemAnimator());
        rvBookmark.setAdapter(new NewsAdapter(this,newsList));

    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

//    private void checkSignedIn() {
//        User user = auth.currentUser;
//        if (user == null)
//            startActivity(new Intent(this, MainActivity.class));
//    }
    private void loadNews() {
        progressBarAB.setVisibility( View.VISIBLE);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        newsList = databaseHelper.getAllData();
        rvBookmark.setAdapter(new NewsAdapter(this,newsList));
        progressBarAB.setVisibility( View.GONE);
    }

}