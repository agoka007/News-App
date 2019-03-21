package com.example.agoston.newsapp;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity
        extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<News>> {

    public static final int NEWS_LOADER_ID = 1;
    public static final String GUARDIAN_REQUEST_URL =
            "http://content.guardianapis.com/search?order-by=newest&show-tags=contributor&page-size=15&q=politics&api-key=test";

    NewsAdapter mAdapter;
    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.text_view)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNews = mAdapter.getItem(position);
                Uri newsUrl = Uri.parse(currentNews.getUrl());

                Intent intent = new Intent(Intent.ACTION_VIEW, newsUrl);

                PackageManager packageManager = getPackageManager();
                List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
                boolean isIntentSafe = activities.size() > 0;

                if (isIntentSafe) {
                    startActivity(intent);
                }

            }
        });
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.progressBar);
            loadingIndicator.setVisibility(View.GONE);
            textView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortBy = sharedPreferences.getString(
                getString(R.string.settings_sort_by_key),
                getString(R.string.settings_sort_by_default));

        String numbersOfNews = sharedPreferences.getString(
                getString(R.string.settings_page_size_key),
                getString(R.string.settings_page_size_default));

        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("order-by", sortBy);
        uriBuilder.appendQueryParameter("page-size", numbersOfNews);

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        View loadingIndicator = findViewById(R.id.progressBar);
        loadingIndicator.setVisibility(View.GONE);
        textView.setText(R.string.no_information_found);
        mAdapter.clear();

        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}