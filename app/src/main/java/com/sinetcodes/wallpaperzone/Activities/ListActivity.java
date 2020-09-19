package com.sinetcodes.wallpaperzone.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sinetcodes.wallpaperzone.Adapters.CategoriesAdapter;
import com.sinetcodes.wallpaperzone.Adapters.CollectionListAdapter;
import com.sinetcodes.wallpaperzone.Common.ContentType;
import com.sinetcodes.wallpaperzone.Home.HomeMVPInterface;
import com.sinetcodes.wallpaperzone.Home.HomePresenter;
import com.sinetcodes.wallpaperzone.pojo.CategoryItem;
import com.sinetcodes.wallpaperzone.pojo.Collection;
import com.sinetcodes.wallpaperzone.R;
import com.sinetcodes.wallpaperzone.utils.EndlessRecyclerViewScrollListener;
import com.sinetcodes.wallpaperzone.utils.StringsUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListActivity extends AppCompatActivity
        implements
        HomeMVPInterface.view,
        CollectionListAdapter.OnItemClickListener,
        CategoriesAdapter.OnCategoryClickListener{
    private static final String TAG = "ListActivity";
    HomePresenter mPresenter;
    CollectionListAdapter mAdapter;
    CategoriesAdapter categoryAdapter;

    @BindView(R.id.list_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    MaterialToolbar mToolbar;
    @BindView(R.id.progress)
    LottieAnimationView progress;

    int page = 1;
    String requestFrom = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);

        requestFrom = getIntent().getStringExtra(StringsUtil.LIST_ACTIVITY_REQUEST);
        setupToolbar();
        if (requestFrom.equals(StringsUtil.COLLECTION)) {
            setUpCollectionRV();
            mPresenter = new HomePresenter(this, this);
            mPresenter.getContent(ContentType.COLLECTIONS, page);
        } else if (requestFrom.equals(StringsUtil.CATEGORY)) {
            setUpCategoryRV();
        }


    }

    private void setUpCategoryRV() {
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);

        Query categoryQuery= FirebaseDatabase.getInstance().getReference().child(StringsUtil.FIREBASE_CATEGORY);
        FirebaseRecyclerOptions<CategoryItem> options = new FirebaseRecyclerOptions
                                                            .Builder<CategoryItem>()
                .setQuery(categoryQuery,CategoryItem.class).build();
        categoryAdapter = new CategoriesAdapter(options, this,this);
        mRecyclerView.setAdapter(categoryAdapter);

    }

    private void setUpCollectionRV() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView
                .addOnScrollListener(
                        new EndlessRecyclerViewScrollListener(manager) {
                            @Override
                            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                                mPresenter.getContent(ContentType.COLLECTIONS, page);
                            }
                        });
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(requestFrom);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setContent(List<Object> items, String contentType) {
        if (page == 1) {
            mAdapter = new CollectionListAdapter(this, this, (List<Collection>) (List<?>) items);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.addItems((List<Collection>) (List<?>) items);
        }
        page++;
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void onItemClicked(Collection collection) {
        startSearchActivity(StringsUtil.COLLECTION,collection,null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            categoryAdapter.startListening();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            categoryAdapter.stopListening();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCategoryClicked(String title) {
        startSearchActivity(StringsUtil.CATEGORY,null,title);
    }

    public void startSearchActivity(String req, @Nullable Collection collection, @Nullable String title){
        Intent intent = new Intent(this, ResultActivity.class);
        switch (req){
            case StringsUtil.COLLECTION:
                intent.putExtra(StringsUtil.SEARCH_ACTIVITY_REQUEST, StringsUtil.COLLECTION);
                intent.putExtra("collection_id", collection.getId());
                intent.putExtra("collection_name", collection.getTitle());
                intent.putExtra("collection_user", collection.getUser().getName());
                intent.putExtra("collection_image", collection.getCoverPhoto().getUrls().getSmall());
                break;

            case  StringsUtil.CATEGORY:
                intent.putExtra(StringsUtil.SEARCH_ACTIVITY_REQUEST, StringsUtil.CATEGORY);
                intent.putExtra("query", title);
                break;
        }
        startActivity(intent);
    }
}
