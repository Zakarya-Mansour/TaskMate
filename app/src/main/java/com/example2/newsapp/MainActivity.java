package com.example2.newsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.example2.newsapp.helper.SimpleItemTouchHelperCallback;
import com.example2.newsapp.model.NewsData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<NewsData> newsData = new ArrayList<>();
    private ProgressBar progressBarAM ;
    private  String newsUrl = Constants.NEWS_API;
    private  ArrayList<Button> buttonCategory= new ArrayList<>();
    private  String newsCategory;
    private  String country;
    private  NewsAdapter newsAdapter = new NewsAdapter(this,null) ;
    private  SwipeRefreshLayout swipeRefreshAM;
    private Button btnBreakingNews,btnBusiness,btnTechnology,btnSports,btnScience,btnHealth;
    private ImageView ivLanguage,ivBookmark;
    private RecyclerView rvNewsAM;
    private  DatabaseHelper databaseHelper;
    private  NewsData news;
    private  int last=0;
    private ItemTouchHelper mItemTouchHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e ) {

        }
        InitViews();
        loadNews();
    }
    private void InitViews() {
        swipeRefreshAM = findViewById(R.id.swipeRefreshAM);
        progressBarAM = findViewById(R.id.progressBarAM);
        btnBreakingNews = findViewById(R.id.btnBreakingNews);
        btnBusiness  = findViewById(R.id.btnBusiness);
        btnHealth = findViewById(R.id.btnHealth);
        btnScience = findViewById(R.id.btnScience);
        btnTechnology = findViewById(R.id.btnTechnology);
        btnSports  =findViewById(R.id.btnSports);
        buttonCategory.add(btnBreakingNews);
        buttonCategory.add(btnBusiness);
        buttonCategory.add(btnTechnology);
        buttonCategory.add(btnSports);
        buttonCategory.add(btnScience);
        buttonCategory.add(btnHealth);
        for (int i = 0; i < buttonCategory.size(); i++) {
            buttonCategory.get(i).setOnClickListener(this);
        }
        swipeRefreshAM.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                loadNews();
                swipeRefreshAM.setRefreshing(false);
            }
        });

        newsCategory = "breaking-news";
        newsCategory = buttonCategory.get(last).getText().toString();
        btnBreakingNews.setBackgroundResource(R.drawable.category_button_selected_design);
        btnBreakingNews.setTextColor(Color.parseColor("#000000"));

        newsUrl = "https://gnews.io/api/v4/top-headlines?&country="+getCountry()+"&topic="+newsCategory+"&token=API_TOEKN";

        ivLanguage = findViewById(R.id.ivLanguage);
        ivBookmark = findViewById(R.id.ivBookmark);
        ivLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRadioButtonDialog();
                showToast("Language");
            }
        });
        ivBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
                showToast("SignIn");
            }
        });
        ivBookmark.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                signOut();
                showToast("signOut");
                return true;
            }
        });

        newsAdapter = new NewsAdapter(this,newsData);
        rvNewsAM =  findViewById(R.id.rvNewsAM);
        rvNewsAM.setHasFixedSize(true);
        rvNewsAM.setAdapter(newsAdapter);
        rvNewsAM.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(newsAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(rvNewsAM);


    }


    private void loadNews() {

        progressBarAM.setVisibility(View.VISIBLE);
        String url = newsUrl;

        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray articleResponse = response.getJSONArray("articles");
                    newsData.clear();
                    for (int i=0; i < articleResponse.length();i++) {

                        JSONObject newsJsonObject = articleResponse.getJSONObject(i);

                         NewsData news = new NewsData(
                                newsJsonObject.getString("title"),
                                newsJsonObject.getString("description"),
                                newsJsonObject.getString("image"),
                                newsJsonObject.getString("url")
                        );
                         newsData.add(news);
                    }
//                    newsAdapter.updateNewsList(newsData);
                    rvNewsAM.getAdapter().notifyDataSetChanged();
                    progressBarAM.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonObject);
        rvNewsAM.setAdapter(new NewsAdapter(getApplicationContext(), newsData));


    }
    private  boolean isSignedIn(){
        return  true;
    }
    private void signIn(){
//        if (isSignedIn()) {
//             Start Bookmark Activity
            startActivity(new Intent(this, Bookmark.class));
//        } else {
            // Initiate google signin
//            val signInIntent = googleSignInClient.signInIntent
//            startActivityForResult(signInIntent, RC_SIGN_IN)
//            showToast("to google signup");
//        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void signOut(){
        if(isSignedIn()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm Logout");
            builder.setMessage("Are you sure you want to logout?");
            builder.setIcon(R.drawable.icon_logout);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ivBookmark.setImageResource(R.drawable.icon_login);
                    dialogInterface.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();


        }
    }

    @Override
    public void onClick(View view) {
        int position=0;
        switch (view.getId()){
            case  R.id.btnBreakingNews:position=0;break;
            case  R.id.btnBusiness:position=1;break;
            case  R.id.btnTechnology:position=2;break;
            case  R.id.btnSports:position=3;break;
            case  R.id.btnScience:position=4;break;
            case  R.id.btnHealth:position=5;break;
        }
        updateUI(position);

    }
    private  void updateUI(int position){
        buttonCategory.get(position).setBackgroundResource(R.drawable.category_button_design);
        buttonCategory.get(position).setTextColor(Color.parseColor("#000000"));
        buttonCategory.get(position).setClickable(false);

        newsCategory = buttonCategory.get(position).getText().toString();
        Toast.makeText(getApplicationContext(),newsCategory,Toast.LENGTH_LONG).show();
        newsUrl = "https://gnews.io/api/v4/top-headlines?&country="+getCountry()+"&topic="+newsCategory+"&token=API_TOEKN";
        Log.d("UPDATE",newsUrl);
        for (int i = 0; i < buttonCategory.size(); i++) {
            if(i==position) {
                last = i;
                continue;
            }
            buttonCategory.get(i).setBackgroundResource(R.drawable.category_button_design);
            buttonCategory.get(i).setTextColor(Color.parseColor("#DAE0E2"));
            buttonCategory.get(i).setClickable(true);
        }
        loadNews();

    }
    @SuppressLint("ResourceAsColor")
    private void showRadioButtonDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_country);
        List<String> stringList=new ArrayList<>();
        stringList.add("us");
        stringList.add("fr");
        stringList.add("mar");

        List<String> stringLTag=new ArrayList<>();
        stringLTag.add("en");
        stringLTag.add("fr");
        stringLTag.add("ar");

        RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radioGroupCountry);
        for(int i=0;i<stringList.size();i++) {
            RadioButton rb=new RadioButton(this);
            rb.setText(stringList.get(i));
            rb.setButtonTintList(ColorStateList.valueOf(R.color.yellow_custom));
            rb.setTag(stringLTag.get(i));
            rb.setPadding(4,4,4,4);
            rb.setTextColor(R.color.grey);
            rb.setTextSize(1,20);
            rg.addView(rb);
        }
        dialog.show();

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int childCount = group.getChildCount();

                for (int x = 0; x < childCount; x++) {
                    RadioButton btn = (RadioButton) group.getChildAt(x);
                    if (btn.getId() == checkedId) {
                        String lg = btn.getText().toString();
                        setCountry(lg);
                        Toast.makeText(getApplicationContext(),getCountry(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Button btnSave = dialog.findViewById(R.id.btnSaveCountry);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                setLanguage(lang);
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private String getCountry() {
        return  this.country;
    }

    private void setCountry(String lg) {
        this.country = lg;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}