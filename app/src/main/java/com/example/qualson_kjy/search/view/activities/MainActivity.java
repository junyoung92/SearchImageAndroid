package com.example.qualson_kjy.search.view.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.qualson_kjy.search.presenter.MainPresenter;
import com.example.qualson_kjy.search.view.adapters.MainAdapter;
import com.example.qualson_kjy.search.R;
import com.example.qualson_kjy.search.model.ChannelItem;
import com.example.qualson_kjy.search.model.ChannelRoot;
import com.example.qualson_kjy.search.model.Image;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements MainPresenter.View, AbsListView.OnScrollListener {

    @ViewById(R.id.main_gv)
    protected GridView gridView;

    @ViewById(R.id.main_progress)
    protected ProgressBar progressBar;

    private MainAdapter mainAdapter;
    private ArrayList<Image> imageList;
    private int count;
    private MainPresenter mainPresenter = new MainPresenter();
    private String keyword;
    private boolean state;

    @AfterViews
    protected void init() {
        gridView.setOnScrollListener(this);
        imageList = new ArrayList<>();
        mainAdapter = new MainAdapter(this, imageList);
        count = 1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                gridView.setAdapter(mainAdapter);
                SearchFilter(s);
                progressBar.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
        }
        return false;
    }

    private void SearchFilter(String charText) {
        count = 1;
        imageList.clear();
        keyword = charText;
        if (charText.length() != 0) {
            apiCall();
        }
    }

    private void apiCall() {
        progressBar.setVisibility(View.VISIBLE);
        mainPresenter.initialize(MainActivity.this, keyword, count, imageList);
        mainPresenter.execute();
    }

    @Override
    public void success(ArrayList<Image> imageList) {
        this.imageList = imageList;
        mainAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void error(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        progressBar.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && state) {
            count++;
            apiCall();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        state = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount) >= totalItemCount;
    }
}
