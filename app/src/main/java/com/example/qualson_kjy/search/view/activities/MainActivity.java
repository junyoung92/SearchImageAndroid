package com.example.qualson_kjy.search.view.activities;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.qualson_kjy.search.R;
import com.example.qualson_kjy.search.model.Image;
import com.example.qualson_kjy.search.presenter.MainPresenter;
import com.example.qualson_kjy.search.view.adapters.MainAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements MainPresenter.View {//}, AbsListView.OnScrollListener {

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
        imageList = new ArrayList<>();
        mainAdapter = new MainAdapter(this, imageList);
        count = 1;

        scrollObservable(gridView).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean state) {
                if (state) {
                    count++;
                    apiCall();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchObservable(searchView).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                gridView.setAdapter(mainAdapter);
                SearchFilter(s);
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        return true;
    }

    private Observable scrollObservable(AbsListView absListView) {
        final PublishSubject publishSubject = PublishSubject.create();
        absListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    publishSubject.onNext(state);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                state = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount) >= totalItemCount;
            }
        });
        return publishSubject;
    }

    private Observable searchObservable(SearchView searchView) {
        final PublishSubject publishSubject = PublishSubject.create();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                publishSubject.onNext(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return publishSubject;
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


//    @Override
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && state) {
//            count++;
//            apiCall();
//        }
//    }
//
//    @Override
//    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        state = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount) >= totalItemCount;
//
//    }


//    private void d() {
//
//        WidgetObservable.listScrollEvents(gridView).subscribe(new Action1<OnListViewScrollEvent>() {
//            @Override
//            public void call(OnListViewScrollEvent onListViewScrollEvent) {
//                state = (onListViewScrollEvent.totalItemCount() > 0) && (onListViewScrollEvent.firstVisibleItem() + onListViewScrollEvent.visibleItemCount()) >= onListViewScrollEvent.totalItemCount();
//                Log.d("listScroll", state + "   " + onListViewScrollEvent.toString());
//
//                if (onListViewScrollEvent.scrollState() == SCROLL_STATE_IDLE && state) {
//                    count++;
//                    apiCall();
//
//                }
//            }
//        }, new Action1<Throwable>() {
//            @Override
//            public void call(Throwable throwable) {
//                Log.d("listScroll", "------------------" + state + "  " + throwable.toString());
//            }
//        });
//    }
}
