package com.sinetcodes.wallpaperzone.Profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nineoldandroids.view.ViewHelper;
import com.sinetcodes.wallpaperzone.Activities.SettingsActivity;
import com.sinetcodes.wallpaperzone.Adapters.DownloadsAdapter;
import com.sinetcodes.wallpaperzone.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProfileFragment extends Fragment
        implements
        ProfileImpl.view,
        ObservableScrollViewCallbacks {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @BindView(R.id.downloads_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.scrollView)
    ObservableScrollView mScrollView;
    @BindView(R.id.header_bg)
    View headerBg;
    @BindView(R.id.empty_layout)
    View emptyLayout;

    ProfileImpl.presenter mPresenter;

    @OnClick(R.id.btn_settings)
    void btnSettingsClicked() {
        startActivity(new Intent(getContext(), SettingsActivity.class));
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        mScrollView.setScrollViewCallbacks(this);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mPresenter = new ProfilePresenter(getContext(), this);
        mPresenter.getDownloads();

        return view;
    }

    @Override
    public void setDownloads(File[] fileList) {
        if (fileList.length > 0) {
            DownloadsAdapter adapter = new DownloadsAdapter(fileList, getContext());
            mRecyclerView.setAdapter(adapter);
        } else {
            emptyLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        ViewHelper.setTranslationY(headerBg, scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    public void scrollToTop(){
        mScrollView.smoothScrollTo(0,0);
    }
}
