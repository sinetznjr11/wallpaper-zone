package com.sinetcodes.wallpaperzone.Home;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.sinetcodes.wallpaperzone.POJO.Photos;
import com.sinetcodes.wallpaperzone.PhotoView.PhotoViewActivity;
import com.sinetcodes.wallpaperzone.Common.ContentType;
import com.sinetcodes.wallpaperzone.Home.Slider.SliderAdapter;
import com.sinetcodes.wallpaperzone.POJO.ExploreItem;
import com.sinetcodes.wallpaperzone.R;
import com.sinetcodes.wallpaperzone.Utilities.AppUtil;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment
        extends
        Fragment
        implements
        ExploreMVPInterface.view,
        ObservableScrollViewCallbacks,
        ExploreAdapter.OnParentItemClickListener,
        ExploreHorizontalAdapter.OnChildItemClickedListener, SliderView.OnSliderPageListener {

    /*
    todo slider layout on top, html games
     */
    private ExplorePresenter explorePresenter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @BindView(R.id.app_bar)
    Toolbar mAppBarLayout;

    @BindView(R.id.imageSlider)
    SliderView sliderView;
    //int slideViewHeight;

    @BindView(R.id.slider_overlay)
    View sliderOverlay;

    @BindView(R.id.featured_text)
    TextView featuredText;

    @BindView(R.id.featured_image)
    ImageView featuredImage;

    @BindView(R.id.featured_user)
    TextView featuredUser;

    @BindView(R.id.loader_layout)
    View progressLoader;

    @BindView(R.id.explore_vertical_rv)
    RecyclerView exploreVerticalRV;

    @BindView(R.id.observable_scroll_view)
    ObservableScrollView observableScrollView;

    ExploreAdapter adapter;
    List<ExploreItem> exploreItems = new ArrayList<>();
    List<Photos> popularPhotoList = new ArrayList<>();
    List<Photos> sliderItems = new ArrayList<>();

    boolean isLoadingMore=false;
    int popularPage=1;

    private static final String TAG = "ExploreFragment";

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        Log.d(TAG, "onCreateView: " + AppUtil.getStatusBarHeight(getActivity()));
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, AppUtil.getStatusBarHeight(getActivity()), 0, 0);

        mAppBarLayout.setLayoutParams(params);


        observableScrollView.setScrollViewCallbacks(this);


        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        exploreVerticalRV.setLayoutManager(layoutManager);
        exploreVerticalRV.setNestedScrollingEnabled(false);
        exploreVerticalRV.setHasFixedSize(true);

        explorePresenter = new ExplorePresenter(getContext(), this);
        explorePresenter.getContent(ContentType.POPULAR, popularPage);

        //slider items attr
        setImageSliderAttr();

        return view;
    }

    private void setImageSliderAttr() {
        sliderView.setIndicatorAnimation(IndicatorAnimations.THIN_WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.startAutoCycle();
        sliderView.setCurrentPageListener(this);

    }


    @Override
    public void setContent(List<Object> items, String contentType) {

        switch (contentType) {
            case ContentType.POPULAR:

                //on first load case
                if (popularPage==1) {
                    Log.d(TAG, "setContent: "+popularPage+"st load");
                    popularPhotoList = (List<Photos>) (List<?>) items;
                    for (int i = 0; i < 4; i++) {
                        sliderItems.add((Photos) items.get(i));
                    }
                    setSliderView(0);

                    SliderAdapter sliderAdapter = new SliderAdapter(getContext());
                    sliderAdapter.renewItems(sliderItems);
                    sliderView.setSliderAdapter(sliderAdapter);

                    explorePresenter.getContent(ContentType.CATEGORY, 0);

                }else{
                    Log.d(TAG, "setContent: "+popularPage+" load");
                    isLoadingMore=false;
                   adapter.addPopularContent(items);
                }
                popularPage++;

                //on load more case



                break;
            case ContentType.CATEGORY:
                exploreItems.add(new ExploreItem(contentType, items));
                explorePresenter.getContent(ContentType.COLLECTIONS, 1);

                adapter = new ExploreAdapter(getContext(), this,exploreItems, this, this);
                exploreVerticalRV.setAdapter(adapter);
                break;

            case ContentType.COLLECTIONS:
                exploreItems.add(new ExploreItem(contentType, items));
                //finally setting popular items pulled at first
                Collections.shuffle(popularPhotoList);
                exploreItems.add(new ExploreItem(ContentType.POPULAR, (List<Object>) (List<?>) popularPhotoList));
                adapter.notifyDataSetChanged();
                break;


        }


    }

    @Override
    public void onError(String error) {
        Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        progressLoader.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressLoader.setVisibility(View.GONE);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

        //infinite scroll
        if(!isLoadingMore && scrollY==(observableScrollView.getChildAt(0).getMeasuredHeight()-observableScrollView.getMeasuredHeight())){
            isLoadingMore=true;
            explorePresenter.getContent(ContentType.POPULAR,popularPage);
        }

        int baseColor = getResources().getColor(R.color.colorBackground);
        int height = sliderView.getHeight() / 2;
        float alpha = Math.min(1, (float) scrollY / height);
        sliderView.setForeground(new ColorDrawable(ScrollUtils.getColorWithAlpha(alpha, baseColor)));
        ViewHelper.setTranslationY(sliderView, scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if (scrollState == ScrollState.DOWN) {
            showLayout();
        } else if (scrollState == ScrollState.UP) {
            hideLayout();
        }
    }

    private void showLayout() {
        mAppBarLayout.animate()
                .translationY(0)
                .alpha(1.0f)
                .setDuration(100);

    }

    private void hideLayout() {
        mAppBarLayout.animate()
                .translationY(-mAppBarLayout.getHeight())
                .alpha(0.0f)
                .setDuration(100);
    }

    @Override
    public void onChildItemClicked(View view, int parentPosition, int childPosition) {
        switch (view.getId()) {
            case R.id.single_image:
                switch (exploreItems.get(parentPosition).getTitle()) {
                    case ContentType.POPULAR:
                        Intent intent = new Intent(getContext(), PhotoViewActivity.class);
                        Photos photoItem = (Photos) exploreItems.get(parentPosition).getItems().get(childPosition);
                        intent.putExtra("photoItem", photoItem);
                        startActivity(intent);
                        break;
                }

                break;
        }
    }

    @Override
    public void onParentItemClicked(View view, int position) {
        switch (view.getId()) {
            case R.id.btn_show_all:
                Log.d(TAG, "onParentItemClicked: Show all button clicked ");
                break;
            default:
                Log.d(TAG, "onParentItemClicked: antai");
                break;

        }
    }

    @Override
    public void onSliderPageChanged(int position) {
        setSliderView(position);
    }

    public void setSliderView(int position) {
        featuredUser.setText(sliderItems.get(position).getUser().getName());

        if (sliderItems.get(position).getDescription() != null)
            featuredText.setText(sliderItems.get(position).getDescription());
        else if (sliderItems.get(position).getAlt_description() != null)
            featuredText.setText(sliderItems.get(position).getAlt_description());
        else
            featuredText.setText("Featured Content");

        Picasso.get()
                .load(sliderItems.get(position).getUser().getProfile_image().getMedium())
                .placeholder(R.drawable.user_placeholder)
                .into(featuredImage);
    }


}
