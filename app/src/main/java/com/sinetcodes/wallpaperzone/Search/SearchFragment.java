package com.sinetcodes.wallpaperzone.Search;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.sinetcodes.wallpaperzone.Activities.ResultActivity;
import com.sinetcodes.wallpaperzone.Common.ContentType;
import com.sinetcodes.wallpaperzone.Home.HomeHorizontalAdapter;
import com.sinetcodes.wallpaperzone.PhotoView.PhotoViewActivity;
import com.sinetcodes.wallpaperzone.POJO.Photos;
import com.sinetcodes.wallpaperzone.R;
import com.sinetcodes.wallpaperzone.Utilities.AppUtil;
import com.sinetcodes.wallpaperzone.Utilities.StringsUtil;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchFragment extends Fragment
        implements
        RewardedVideoAdListener,
        SearchMVPInterface.view,
        ObservableScrollViewCallbacks,
        SearchView.OnQueryTextListener,
        HomeHorizontalAdapter.OnChildItemClickedListener {

    /*
    todo interstellar ad after download completes
     */
    private static final String TAG = "SearchFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @BindView(R.id.observable_scroll_view)
    ObservableScrollView mScrollView;
    @BindView(R.id.loader_layout)
    View progressBar;
    @BindView(R.id.search_layout)
    View searchLayout;
    @BindView(R.id.search_view)
    SearchView mSearchView;


    private RewardedVideoAd mRewardedVideoAd;
    boolean isBtnReloadClicked = false;

    @BindView(R.id.search_fragment_recycler_view)
    RecyclerView mRecyclerView;
    SearchMVPInterface.presenter presenter;
    int count = 1;

    HomeHorizontalAdapter adapter;

    boolean isLoadingMore = false;
    boolean isRandomCardClicked = false;


    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        mScrollView.setScrollViewCallbacks(this);

        mSearchView.setOnQueryTextListener(this);



/*        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getContext());
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();*/


        presenter = new SearchPresenter(this, getContext());
        presenter.getContent(AppUtil.getRandomQuery(), -1, 20, StringsUtil.RANDOM);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);


        return view;
    }

    @OnClick(R.id.random_card_layout)
    void onRandomCardClicked() {
        isRandomCardClicked = true;
        presenter.getContent("", -1, 1, StringsUtil.RANDOM);
    }

    @OnClick(R.id.btn_search)
    void onBtnSearchClicked() {
        searchLayout.setVisibility(View.VISIBLE);
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setFocusable(true);
        mSearchView.setIconified(false);
        mSearchView.requestFocus();
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                hideSearchLayout();
                return false;
            }
        });

        mScrollView.setVisibility(View.GONE);
    }

    @OnClick(R.id.chip_black)
    void onBlackChipClicked() {
        startSearchActivity("black");
    }

    @OnClick(R.id.chip_blue)
    void onBlueChipClicked() {
        startSearchActivity("blue");
    }

    @OnClick(R.id.chip_yellow)
    void onYellowChipClicked() {
        startSearchActivity("yellow");
    }

    @OnClick(R.id.chip_purple)
    void onPurpleChipClicked() {
        startSearchActivity("purple");
    }

    @OnClick(R.id.chip_green)
    void onGreenChipClicked() {
        startSearchActivity("green");
    }

    @OnClick(R.id.chip_red)
    void onRedChipClicked() {
        startSearchActivity("red");
    }


    @Override
    public void setContent(List<Photos> photos) {

        if (isRandomCardClicked) {
            //get one random image
            isRandomCardClicked = false;
            startPhotoViewActivity(photos.get(0));
        } else {
            if (count == 1) {
                //first load
                adapter = new HomeHorizontalAdapter(getContext(), this, 0, (List<Object>) (List<?>) photos, ContentType.POPULAR);
                mRecyclerView.setAdapter(adapter);
            } else {
                //endless scroll
                isLoadingMore = false;
                adapter.addPopularContent((List<Object>) (List<?>) photos);
            }
            count++;
        }

    }


    @Override
    public void onError(String error) {

    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void loadRewardedVideoAd() {
       /* if (!mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.loadAd(adsUtil.getRewardAdUnit(),
                    new AdRequest.Builder().build());
        } else if (isBtnReloadClicked) {
            mRewardedVideoAd.show();
        }*/

    }

    /**
     * Reward VideoAd listener
     */
    @Override
    public void onRewardedVideoAdLoaded() {
        if (isBtnReloadClicked) {
            mRewardedVideoAd.show();
        }
    }

    @Override
    public void onRewardedVideoAdOpened() {
        isBtnReloadClicked = false;
    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        //cardStackView.rewind();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    @Override
    public void onChildItemClicked(View view, int parentPosition, int childPosition) {

        startPhotoViewActivity(adapter.getPhotoItem(childPosition));
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (!isLoadingMore && scrollY == (mScrollView.getChildAt(0).getMeasuredHeight() - mScrollView.getMeasuredHeight())) {
            isLoadingMore = true;
            presenter.getContent(AppUtil.getRandomQuery(), -1, 20, StringsUtil.RANDOM);
        }
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    public boolean onBackPressed() {
        if (searchLayout.getVisibility() == View.VISIBLE) {
            hideSearchLayout();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        startSearchActivity(query);
        return false;
    }

    private void startSearchActivity(String query) {
        Intent intent = new Intent(getContext(), ResultActivity.class);
        intent.putExtra(StringsUtil.SEARCH_QUERY, query);
        intent.putExtra(StringsUtil.SEARCH_ACTIVITY_REQUEST, this.getClass().getSimpleName());
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    private void startPhotoViewActivity(Photos photo) {
        Intent intent = new Intent(getContext(), PhotoViewActivity.class);
        intent.putExtra("photoItem", photo);
        startActivity(intent);
    }

    public void hideSearchLayout() {
        searchLayout.setVisibility(View.GONE);
        mScrollView.setVisibility(View.VISIBLE);
    }

    public void scrollToTop(){
        if(mScrollView.getCurrentScrollY()==0){
            presenter.getContent(AppUtil.getRandomQuery(), -1, 20, StringsUtil.RANDOM);
            count=1;
        }
        else
        mScrollView.smoothScrollTo(0,0);
    }

}
