package com.example2.newsapp.model;

public class NewsData {
    private  String title;
    private  String desc;
    private  String imageUrl;
    private  String newsUrl;

    public NewsData() {
    }

    public NewsData(String title, String desc, String imageUrl, String newsUrl) {
        this.title = title;
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.newsUrl = newsUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }
}
