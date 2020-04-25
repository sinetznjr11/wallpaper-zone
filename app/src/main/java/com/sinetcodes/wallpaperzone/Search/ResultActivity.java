package com.sinetcodes.wallpaperzone.Search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.icu.text.StringSearch;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nineoldandroids.view.ViewHelper;
import com.sinetcodes.wallpaperzone.Common.ContentType;
import com.sinetcodes.wallpaperzone.Home.ExploreHorizontalAdapter;
import com.sinetcodes.wallpaperzone.POJO.Photos;
import com.sinetcodes.wallpaperzone.R;
import com.sinetcodes.wallpaperzone.Utilities.StringsUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
//todo implement mvc of search results

public class ResultActivity extends AppCompatActivity
        implements
        HomeMVPInterface.homeView,
        ObservableScrollViewCallbacks,
        ExploreHorizontalAdapter.OnChildItemClickedListener {

    String query;
    String requestFrom;

    @BindView(R.id.header_text)
    TextView headerText;
    @BindView(R.id.results_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.scroll_view)
    ObservableScrollView mScrollView;
    @BindView(R.id.top_layout)
    RelativeLayout topLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.loader_layout)
    View loaderLayout;



    ExploreHorizontalAdapter mAdapter;

    int page=1;
    boolean isLoadingMore=false;

    HomePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");


        query=getIntent().getStringExtra(StringsUtil.SEARCH_QUERY);
        requestFrom=getIntent().getStringExtra(StringsUtil.SEARCH_ACTIVITY_REQUEST);

        headerText.setText(query);

        mScrollView.setScrollViewCallbacks(this);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);

        mPresenter=new HomePresenter(this,this);
        mPresenter.getContent(query,page);


    }

    @Override
    public void setContent(List<Photos> photos) {

        if(page==1){
            mAdapter = new ExploreHorizontalAdapter(this, this, 0, (List<Object>) (List<?>) photos, ContentType.POPULAR);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
        isLoadingMore=false;
        mAdapter.addPopularContent((List<Object>)(List<?>) photos);
        }

        page++;
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void showProgress() {
        loaderLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        loaderLayout.setVisibility(View.GONE);
    }

    @Override
    public void onChildItemClicked(View view, int parentPosition, int childPosition) {

    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

        if(!isLoadingMore && scrollY==(mScrollView.getChildAt(0).getMeasuredHeight()-mScrollView.getMeasuredHeight())){
            isLoadingMore=true;
            mPresenter.getContent(query,page);
        }

        ViewHelper.setTranslationY(topLayout, scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
