package com.example.agoston.newsapp;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends ArrayAdapter<News> {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.author)
    TextView author;
    @BindView(R.id.category)
    TextView category;

    List<News> mNewsList;

    public NewsAdapter(@NonNull Context context, List<News> newsList) {
        super(context, 0, newsList);
        mNewsList = newsList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {

        News currentNews = mNewsList.get(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        ButterKnife.bind(this, view);
        String trimedDate = currentNews.getDate().substring(0, 10);

        title.setText(currentNews.getTitle());
        date.setText(trimedDate);
        author.setText(currentNews.getAuthor());
        category.setText(currentNews.getCategory());
        return view;
    }
}