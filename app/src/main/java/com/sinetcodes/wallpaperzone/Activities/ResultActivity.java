package com.sinetcodes.wallpaperzone.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.sinetcodes.wallpaperzone.Common.ContentType;
import com.sinetcodes.wallpaperzone.Home.HomeHorizontalAdapter;
import com.sinetcodes.wallpaperzone.POJO.Photos;
import com.sinetcodes.wallpaperzone.PhotoView.PhotoViewActivity;
import com.sinetcodes.wallpaperzone.R;
import com.sinetcodes.wallpaperzone.Search.SearchMVPInterface;
import com.sinetcodes.wallpaperzone.Search.SearchPresenter;
import com.sinetcodes.wallpaperzone.Utilities.StringsUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResultActivity extends AppCompatActivity
        implements
        SearchMVPInterface.view,
        ObservableScrollViewCallbacks,
        HomeHorizontalAdapter.OnChildItemClickedListener {

    HomeHorizontalAdapter mAdapter;
    SearchPresenter mPresenter;
    private static final String TAG = "ResultActivity";

    @BindView(R.id.overline_text)
    TextView overLineText;
    @BindView(R.id.header_text)
    TextView headerText;
    @BindView(R.id.header_image)
    ImageView headerImage;
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
    @BindView(R.id.scroll_to_top)
    FloatingActionButton scrollToTopFab;
    @BindView(R.id.empty_layout)
    View emptyLayout;


    String query = "";
    String requestFrom;

    int collectionId = -1;
    int page = 1;

    boolean isLoadingMore = false;
    private boolean mFabIsShown;

    List<Photos> mPhotos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
        setActionBar();

        ViewHelper.setScaleX(scrollToTopFab, 0);
        ViewHelper.setScaleY(scrollToTopFab, 0);

        mScrollView.setScrollViewCallbacks(this);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);

        requestFrom = getIntent().getStringExtra(StringsUtil.SEARCH_ACTIVITY_REQUEST);

        mPresenter = new SearchPresenter(this, this);

        //result activity will open from Collections, Search etc.
        setupLayoutData();
    }

    private void setActionBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
    }

    private void setupLayoutData() {
        if (requestFrom.equalsIgnoreCase("SearchFragment") || requestFrom.equalsIgnoreCase(StringsUtil.CATEGORY)) {
            Picasso.get()
                    .load(R.drawable.search_card)
                    .into(headerImage);
            query = getIntent().getStringExtra(StringsUtil.SEARCH_QUERY);
            headerText.setText(query);

            if (requestFrom.equalsIgnoreCase("SearchFragment"))
                mPresenter.getContent(query, -1, page, StringsUtil.SEARCH);
            else if (requestFrom.equalsIgnoreCase(StringsUtil.CATEGORY))
                mPresenter.getContent(query, -1, 30, StringsUtil.RANDOM);
        } else if (requestFrom.equalsIgnoreCase(StringsUtil.COLLECTION)) {
            collectionId = getIntent().getIntExtra("collection_id", 0);
            String collectionImage = getIntent().getStringExtra("collection_image");
            String collectionName = getIntent().getStringExtra("collection_name");
            Log.d(TAG, collectionName + " " + collectionId + " image url: " + collectionImage);
            headerText.setText(collectionName);
            Picasso.get()
                    .load(collectionImage)
                    .into(headerImage);
            mPresenter.getContent("", collectionId, page, StringsUtil.COLLECTION);
        }
    }

    @OnClick(R.id.scroll_to_top)
    void onScrollToTopClicked() {
        mScrollView.smoothScrollTo(0, 0);
    }

    @Override
    public void setContent(List<Photos> photos) {

        mPhotos.addAll(photos);
        if (page == 1) {
            if (photos.size() == 0) {
                Toast.makeText(this, "Sorry! No results found.", Toast.LENGTH_SHORT).show();
                emptyLayout.setVisibility(View.VISIBLE);
            }
            mAdapter = new HomeHorizontalAdapter(this, this, 0, (List<Object>) (List<?>) photos, ContentType.POPULAR);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            isLoadingMore = false;
            mAdapter.addPopularContent((List<Object>) (List<?>) photos);
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
        Intent intent = new Intent(this, PhotoViewActivity.class);
        Photos photoItem = mPhotos.get(childPosition);
        intent.putExtra("photoItem", photoItem);
        startActivity(intent);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

        if (scrollY == 0) {
            hideFab();
        }

        if (!isLoadingMore && scrollY == (mScrollView.getChildAt(0).getMeasuredHeight() - mScrollView.getMeasuredHeight())) {
            isLoadingMore = true;
            switch (requestFrom) {
                case "SearchFragment":
                    mPresenter.getContent(query, -1, page, StringsUtil.SEARCH);
                    break;
                case StringsUtil.COLLECTION:
                    mPresenter.getContent("", collectionId, page, StringsUtil.COLLECTION);
                    break;
                case StringsUtil.CATEGORY:
                    mPresenter.getContent(query, -1, 30, StringsUtil.RANDOM);
                    break;
            }
        }

        ViewHelper.setTranslationY(topLayout, scrollY / 2);


    }

    @Override
    public void onDownMotionEvent() {
        showFab();
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if (scrollState == ScrollState.UP) {
            //     hideFab();
        }
    }


    private void showFab() {
        if (!mFabIsShown) {
            ViewPropertyAnimator.animate(scrollToTopFab).cancel();
            ViewPropertyAnimator.animate(scrollToTopFab).scaleX(1).scaleY(1).setDuration(200).start();
            mFabIsShown = true;
        }
    }

    private void hideFab() {
        if (mFabIsShown) {
            ViewPropertyAnimator.animate(scrollToTopFab).cancel();
            ViewPropertyAnimator.animate(scrollToTopFab).scaleX(0).scaleY(0).setDuration(200).start();
            mFabIsShown = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
