package com.sinetcodes.wallpaperzone.Search;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.material.card.MaterialCardView;
import com.sinetcodes.wallpaperzone.Ads.AdsUtil;
import com.sinetcodes.wallpaperzone.Common.ContentType;
import com.sinetcodes.wallpaperzone.Common.OnBackPressedFragment;
import com.sinetcodes.wallpaperzone.Home.ExploreHorizontalAdapter;
import com.sinetcodes.wallpaperzone.PhotoView.PhotoViewActivity;
import com.sinetcodes.wallpaperzone.POJO.Photos;
import com.sinetcodes.wallpaperzone.R;
import com.sinetcodes.wallpaperzone.Utilities.AppUtil;
import com.sinetcodes.wallpaperzone.Utilities.StringsUtil;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchFragment extends Fragment
        implements
        RewardedVideoAdListener,
        HomeMVPInterface.homeView,
        ObservableScrollViewCallbacks,
        OnBackPressedFragment,
        SearchView.OnQueryTextListener,
        ExploreHorizontalAdapter.OnChildItemClickedListener {

    /*
    todo interstellar ad after download completes
     */
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    Handler sliderHandler = new Handler();

    List<Photos> photosList = new ArrayList<>();
    private static final String TAG = "SearchFragment";

    /*@OnClick(R.id.btn_reload)
    void onBtnReloadClicked() {
        Toast.makeText(getContext(), "Please complete this ad to rewind card.", Toast.LENGTH_SHORT).show();
        isBtnReloadClicked = true;
        loadRewardedVideoAd();
    }*/
    @BindView(R.id.observable_scroll_view)
    ObservableScrollView mScrollView;

    @BindView(R.id.loader_layout)
    View progressBar;

    @BindView(R.id.bannerAdView)
    View bannerAdView;

    @BindView(R.id.search_layout)
    View searchLayout;
    @BindView(R.id.search_view)
    SearchView mSearchView;
    
    @OnClick(R.id.random_card_layout)
    void onRandomCardClicked(){
        isRandomCardClicked=true;
       homePresenter.getContent("",1);
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

    public void hideSearchLayout() {
        searchLayout.setVisibility(View.GONE);
        mScrollView.setVisibility(View.VISIBLE);
    }

    AdsUtil adsUtil = new AdsUtil();

    private RewardedVideoAd mRewardedVideoAd;
    boolean isBtnReloadClicked = false;

    @BindView(R.id.search_fragment_recycler_view)
    RecyclerView mRecyclerView;
    HomeMVPInterface.homePresenter homePresenter;
    int count = 1;

    ExploreHorizontalAdapter adapter;

    boolean isLoadingMore = false;
    boolean isRandomCardClicked=false;

    @OnClick(R.id.chip_black)
    void onBlackChipClicked(){
        startSearchActivity("black");
    }
    @OnClick(R.id.chip_blue)
    void onBlueChipClicked(){
        startSearchActivity("blue");
    }
    @OnClick(R.id.chip_yellow)
    void onYellowChipClicked(){
        startSearchActivity("yellow");
    }
    @OnClick(R.id.chip_purple)
    void onPurpleChipClicked(){
        startSearchActivity("purple");
    }
    @OnClick(R.id.chip_green)
    void onGreenChipClicked(){
        startSearchActivity("green");
    }
    @OnClick(R.id.chip_red)
    void onRedChipClicked(){
        startSearchActivity("red");
    }

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

        AdView adView = new AdView(getActivity());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(adsUtil.getBannerAdUnit());


        ((RelativeLayout) bannerAdView).addView(adView);
        adView.loadAd(new AdRequest.Builder().build());

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getContext());
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();


        homePresenter = new HomePresenter(this, getContext());
        homePresenter.getContent(AppUtil.getRandomQuery(), 20);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);


        return view;
    }


    public void startDownload(String downloadLink) {
        DownloadManager mManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request mRqRequest = new DownloadManager.Request(
                Uri.parse(downloadLink));
        mRqRequest.setDescription("This is Test File");
//  mRqRequest.setDestinationUri(Uri.parse("give your local path"));
        long idDownLoad = mManager.enqueue(mRqRequest);
    }

    @Override
    public void setContent(List<Photos> photos) {

        if(isRandomCardClicked){
            //get one random image
            isRandomCardClicked=false;
            startPhotoViewActivity(photos.get(0));
        }
        else{
            if (count == 1) {
                //first load
                adapter = new ExploreHorizontalAdapter(getContext(), this, 0, (List<Object>) (List<?>) photos, ContentType.POPULAR);
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
        if (!mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.loadAd(adsUtil.getRewardAdUnit(),
                    new AdRequest.Builder().build());
        } else if (isBtnReloadClicked) {
            mRewardedVideoAd.show();
        }

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
            homePresenter.getContent(AppUtil.getRandomQuery(), 20);
        }
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    @Override
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

    private void startSearchActivity(String query){
        Intent intent=new Intent(getContext(), ResultActivity.class);
        intent.putExtra(StringsUtil.SEARCH_QUERY,query);
        intent.putExtra(StringsUtil.SEARCH_ACTIVITY_REQUEST,this.getClass().getSimpleName());
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    private void startPhotoViewActivity(Photos photo){
        Intent intent = new Intent(getContext(), PhotoViewActivity.class);
        intent.putExtra("photoItem", photo);
        startActivity(intent);
    }

}
