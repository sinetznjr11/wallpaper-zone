package com.sinetcodes.wallpaperzone.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sinetcodes.wallpaperzone.data.network.responses.Category;
import com.sinetcodes.wallpaperzone.data.network.responses.Wallpaper;
import com.sinetcodes.wallpaperzone.ui.list.ListActivity;
import com.sinetcodes.wallpaperzone.Common.ContentType;
import com.sinetcodes.wallpaperzone.Home.HomeAdapter;
import com.sinetcodes.wallpaperzone.Home.HomeHorizontalAdapter;
import com.sinetcodes.wallpaperzone.Home.Slider.SliderAdapter;
import com.sinetcodes.wallpaperzone.R;
import com.sinetcodes.wallpaperzone.data.repository.WallpaperRepository;
import com.sinetcodes.wallpaperzone.databinding.FragmentHomeBinding;
import com.sinetcodes.wallpaperzone.ui.photoview.PhotoViewActivity;
import com.sinetcodes.wallpaperzone.utils.StringsUtil;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;

import java.io.Serializable;
import java.util.List;

public class HomeFragment
        extends
        Fragment
        implements
        HomeAdapter.OnParentItemClickListener,
        HomeHorizontalAdapter.OnChildItemClickedListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    private HomeAdapter adapter;
    private SliderAdapter sliderAdapter;


    private static final String TAG = "HomeFragment";

    private FragmentHomeBinding mBinding;
    private HomeViewModel mViewModel;


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

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        WallpaperRepository repository = new WallpaperRepository();
        HomeFactory factory = new HomeFactory(repository);

        mViewModel = new ViewModelProvider(this, factory).get(HomeViewModel.class);
        mBinding.setViewModel(mViewModel);

        setImageSliderAttr();


        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.getHomeItemLiveData().observe(getActivity(), homeItems -> {
            mBinding.loaderLayout.setVisibility(View.GONE);
            if (!homeItems.isEmpty()) {

                //setting slider view
                if (homeItems.get(0).getTitle().equals(ContentType.FEATURED)) {
                    if (sliderAdapter == null) {
                        sliderAdapter = new SliderAdapter(getContext(), homeItems.get(0).getItems());
                        mBinding.imageSlider.setSliderAdapter(sliderAdapter);
                    } else {
                        sliderAdapter.notifyDataSetChanged();
                    }
                }

                if (adapter == null) {
                    homeItems.remove(0);
                    adapter = new HomeAdapter(getContext(), homeItems, this, this);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                    mBinding.homeVerticalRv.setLayoutManager(layoutManager);
                    mBinding.homeVerticalRv.setNestedScrollingEnabled(false);
                    //mBinding.homeVerticalRv.setHasFixedSize(true);
                    mBinding.homeVerticalRv.setAdapter(adapter);
                } else {
                    Log.e(TAG, "onActivityCreated: notifyDataSetChanged");
                    adapter.notifyDataSetChanged();
                }

            }

        });


    }


    private void setImageSliderAttr() {
        mBinding.imageSlider.setIndicatorAnimation(IndicatorAnimations.THIN_WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        mBinding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        mBinding.imageSlider.startAutoCycle();
        //sliderView.setCurrentPageListener(this);
    }


    public void onPhotoItemClick(View view, int parentPosition, int childPosition) {

/*        switch (exploreItems.get(parentPosition).getTitle()) {
            case ContentType.POPULAR:
                Intent intent = new Intent(getContext(), PhotoViewActivity.class);
                Photos photoItem = (Photos) exploreItems.get(parentPosition).getItems().get(childPosition);
                intent.putExtra("photoItem", photoItem);
                startActivity(intent);
                break;

            case ContentType.COLLECTIONS:
                int collectionId = ((Collection) exploreItems.get(parentPosition).getItems().get(childPosition)).getId();
                Log.d(TAG, "onChildItemClicked: " + collectionId + " url: " + ((Collection) exploreItems.get(parentPosition).getItems().get(childPosition)).getCoverPhoto().getUrls().getSmall());
                Intent collectionIntent = new Intent(getContext(), ResultActivity.class);
                collectionIntent.putExtra(StringsUtil.SEARCH_ACTIVITY_REQUEST, StringsUtil.COLLECTION);
                collectionIntent.putExtra("collection_id", collectionId);
                collectionIntent.putExtra("collection_name", ((Collection) exploreItems.get(parentPosition).getItems().get(childPosition)).getTitle());
                collectionIntent.putExtra("collection_user", ((Collection) exploreItems.get(parentPosition).getItems().get(childPosition)).getUser().getName());
                collectionIntent.putExtra("collection_image", ((Collection) exploreItems.get(parentPosition).getItems().get(childPosition)).getCoverPhoto().getUrls().getSmall());
                startActivity(collectionIntent);
                break;
            case ContentType.CATEGORY:
                String query = ((CategoryItem) exploreItems.get(parentPosition).getItems().get(childPosition)).getName();
                Intent categoryIntent = new Intent(getContext(), ResultActivity.class);
                categoryIntent.putExtra(StringsUtil.SEARCH_ACTIVITY_REQUEST, StringsUtil.CATEGORY);
                categoryIntent.putExtra("query", query);
                startActivity(categoryIntent);
                break;
        }*/

    }

    @Override
    public void onParentItemClicked(View view, int position, List<Wallpaper> wallpaperList) {
        if (R.id.btn_show_all == view.getId()) {
            switch (position) {
                case 1:
                    startListActivity(StringsUtil.NEWEST, wallpaperList);
                    break;
                case 2:
                    startListActivity(StringsUtil.POPULAR, wallpaperList);
                    break;
                case 3:
                    startListActivity(StringsUtil.HIGH_RATED, wallpaperList);
                    break;
                case 4:
                    startListActivity(StringsUtil.HIGH_VIEWS, wallpaperList);
                    break;
                default:
                    Log.d(TAG, "onParentItemClicked: ignored parent click");
                    break;

            }
        }

    }

    private void startListActivity(String requestFrom, List<Wallpaper> wallpaperList) {
        Intent intent = new Intent(getContext(), ListActivity.class);
        intent.putExtra(StringsUtil.LIST_ACTIVITY_REQUEST, requestFrom);
        intent.putExtra(StringsUtil.LIST_ACTIVITY_DATA, (Serializable) wallpaperList);
        startActivity(intent);
    }


    @Override
    public void onPhotoItemClick(List<Wallpaper> wallpaperList, int position) {
        Log.e(TAG, "onPhotoItemClick: name--->"+wallpaperList.get(position).getUserName());
        Intent intent = new Intent(getContext(), PhotoViewActivity.class);
        intent.putExtra("wallpaperList", (Serializable) wallpaperList);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Override
    public void onCategoryItemClick(Category category) {

    }
}
