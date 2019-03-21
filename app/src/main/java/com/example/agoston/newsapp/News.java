package com.example.agoston.newsapp;

public class News {
    private String mTitle;
    private String mDate;
    private String mUrl;
    private String mAuthor;
    private String mCategory;

    public News(String title, String date, String url, String author, String category) {
        mTitle = title;
        mDate = date;
        mUrl = url;
        mAuthor = author;
        mCategory = category;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDate() {
        return mDate;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getCategory() {
        return mCategory;
    }

    @Override
    public String toString() {
        return "News{" +
                "title='" + mTitle + '\'' +
                ", webUrl='" + mUrl + '\'' +
                '}';
    }
}