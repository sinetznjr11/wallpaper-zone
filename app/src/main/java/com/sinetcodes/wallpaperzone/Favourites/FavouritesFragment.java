package com.sinetcodes.wallpaperzone.Favourites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nineoldandroids.view.ViewHelper;
import com.sinetcodes.wallpaperzone.pojo.Photos;
import com.sinetcodes.wallpaperzone.PhotoView.PhotoViewActivity;
import com.sinetcodes.wallpaperzone.R;
import com.sinetcodes.wallpaperzone.utils.StringsUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouritesFragment extends Fragment
        implements ObservableScrollViewCallbacks, FavoritesAdapter.OnItemClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private static final String TAG = "FavouritesFragment";

    @BindView(R.id.favorites_recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.scrollView)
    ObservableScrollView mScrollView;
    @BindView(R.id.header_bg)
    FrameLayout headerBg;

    private FavoritesAdapter mAdapter;

    public static FavouritesFragment newInstance(String param1, String param2) {
        FavouritesFragment fragment = new FavouritesFragment();
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
        View view= inflater.inflate(R.layout.fragment_favorites, container, false);
        ButterKnife.bind(this,view);

        mScrollView.setScrollViewCallbacks(this);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser()!=null){
            DatabaseReference favRef = FirebaseDatabase.getInstance().getReference()
                    .child(StringsUtil.FIREBASE_FAVORITES)
                    .child(auth.getCurrentUser().getUid());
            FirebaseRecyclerOptions<Photos> options= new FirebaseRecyclerOptions
                                                            .Builder<Photos>()
                                                                .setQuery(favRef,Photos.class).build();
            mAdapter=new FavoritesAdapter(options,this);
            mRecyclerView.setAdapter(mAdapter);

        }else{
            Log.e(TAG, "onCreateView: Failed to load favorites. Firebase auth failed." );
        }

        return  view;
    }

    @Override
    public void onStart() {
        super.onStart();

        try {
            mAdapter.startListening();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            mAdapter.stopListening();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        ViewHelper.setTranslationY(headerBg,scrollY/2);
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

    @Override
    public void onItemClicked(Photos photo) {
        Log.d(TAG, "onItemClicked: "+photo.getUrls().getSmall());
        Intent intent = new Intent(getContext(), PhotoViewActivity.class);
        intent.putExtra("photoItem", photo);
        startActivity(intent);
    }

}
