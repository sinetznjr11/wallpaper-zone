package com.sinetcodes.wallpaperzone.ui.list;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.sinetcodes.wallpaperzone.Activities.ResultActivity;
import com.sinetcodes.wallpaperzone.Adapters.ListAdapter;
import com.sinetcodes.wallpaperzone.data.network.responses.Wallpaper;
import com.sinetcodes.wallpaperzone.data.network.responses.WallpaperList;
import com.sinetcodes.wallpaperzone.data.repository.WallpaperRepository;
import com.sinetcodes.wallpaperzone.databinding.ActivityListBinding;
import com.sinetcodes.wallpaperzone.pojo.Collection;
import com.sinetcodes.wallpaperzone.utils.EndlessRecyclerViewScrollListener;
import com.sinetcodes.wallpaperzone.utils.StringsUtil;
import com.sinetcodes.wallpaperzone.utils.UrlUtil;

import java.util.List;

public class ListActivity extends AppCompatActivity
        implements
        ListAdapter.OnItemClickListener {

    private static final String TAG = "ListActivity";
    ListAdapter mAdapter;


    boolean isLast = false;
    String requestFrom = "";

    ActivityListBinding mBinding;

    private ListViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        requestFrom = getIntent().getStringExtra(StringsUtil.LIST_ACTIVITY_REQUEST);
        List<Wallpaper> wallpaperListExtra = (List<Wallpaper>) getIntent().getSerializableExtra(StringsUtil.LIST_ACTIVITY_DATA);
        setupToolbar();

        WallpaperRepository repository = new WallpaperRepository();
        ListFactory factory = new ListFactory(repository);
        mViewModel = new ViewModelProvider(this, factory).get(ListViewModel.class);


        setUpListRecyclerView(wallpaperListExtra);

    }


    private void setUpListRecyclerView(List<Wallpaper> wallpaperListExtra) {
        mAdapter = new ListAdapter(this, this, wallpaperListExtra);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mBinding.listRecyclerView.setLayoutManager(layoutManager);
        mBinding.listRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (!isLast) {
                    mBinding.listRecyclerView.post(() -> {
                        mBinding.progress.setVisibility(View.VISIBLE);
                        mViewModel.init(UrlUtil.getUrl(requestFrom, page));
                        mViewModel.getWallpaperListMutableLiveData().observe(ListActivity.this, (Observer<WallpaperList>) wallpaperList -> {
                            isLast=wallpaperList.getIsLast();
                            mBinding.progress.setVisibility(View.GONE);
                            mAdapter.addItems(wallpaperList.getWallpapers());
                        });
                    });
                }else {
                    Toast.makeText(ListActivity.this, "End of list.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mBinding.listRecyclerView.setAdapter(mAdapter);
    }

    private void setupToolbar() {
        setSupportActionBar(mBinding.toolbarLayout.toolbar);
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
    public void onItemClicked(Wallpaper wallpaper) {
        //startSearchActivity(StringsUtil.COLLECTION, collection, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
    }

    public void startSearchActivity(String req, @Nullable Collection collection, @Nullable String title) {
        Intent intent = new Intent(this, ResultActivity.class);
        switch (req) {
            case StringsUtil.COLLECTION:
                intent.putExtra(StringsUtil.SEARCH_ACTIVITY_REQUEST, StringsUtil.COLLECTION);
                intent.putExtra("collection_id", collection.getId());
                intent.putExtra("collection_name", collection.getTitle());
                intent.putExtra("collection_user", collection.getUser().getName());
                intent.putExtra("collection_image", collection.getCoverPhoto().getUrls().getSmall());
                break;

            case StringsUtil.CATEGORY:
                intent.putExtra(StringsUtil.SEARCH_ACTIVITY_REQUEST, StringsUtil.CATEGORY);
                intent.putExtra("query", title);
                break;
        }
        startActivity(intent);
    }

}
