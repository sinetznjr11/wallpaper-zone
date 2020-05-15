package com.sinetcodes.wallpaperzone.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.sinetcodes.wallpaperzone.Ads.AdsUtil;
import com.sinetcodes.wallpaperzone.Ads.MyInterstitialAd;
import com.sinetcodes.wallpaperzone.Home.HomeFragment;
import com.sinetcodes.wallpaperzone.Favourites.FavouritesFragment;
import com.sinetcodes.wallpaperzone.Profile.ProfileFragment;
import com.sinetcodes.wallpaperzone.Search.SearchFragment;
import com.sinetcodes.wallpaperzone.R;
import com.sinetcodes.wallpaperzone.Utilities.AppUtil;
import com.sinetcodes.wallpaperzone.Utilities.MyUser;
import com.sinetcodes.wallpaperzone.Utilities.NetworkConnectivity;
import com.squareup.okhttp.internal.Network;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements
        BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String BACK_STACK_ROOT_TAG = "root_fragment";
    private static final String TAG = "MainActivity";

    final Fragment mHomeFragment = new HomeFragment();
    final Fragment mSearchFragment = new SearchFragment();
    final Fragment mFavouritesFragment = new FavouritesFragment();
    final Fragment mProfileFragment = new ProfileFragment();

    final FragmentManager mFragmentManager = getSupportFragmentManager();

    Fragment active = mHomeFragment;


    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.bannerAdView)
    View bannerAdView;


    MyInterstitialAd mInterstitialAd;
    private FirebaseAnalytics mFirebaseAnalytics;

    AdsUtil adsUtil = new AdsUtil();
    public static int photosInterstitialAdCounter = 0;

    boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //AppUtil.setTransparentStatusBar(this);


        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(adsUtil.getBannerAdUnit());


        ((RelativeLayout) bannerAdView).addView(adView);
        adView.loadAd(new AdRequest.Builder().build());
        mInterstitialAd = new MyInterstitialAd(this, AdsUtil.getAdUnit(AdsUtil.MULTIPLE_PHOTOS_INTERSTITIAL));

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        mFragmentManager.beginTransaction().add(R.id.main_frame, mProfileFragment, "4").hide(mProfileFragment).commit();
        mFragmentManager.beginTransaction().add(R.id.main_frame, mFavouritesFragment, "3").hide(mFavouritesFragment).commit();
        mFragmentManager.beginTransaction().add(R.id.main_frame, mSearchFragment, "2").hide(mSearchFragment).commit();
        mFragmentManager.beginTransaction().add(R.id.main_frame, mHomeFragment, "1").commit();

    }




    @Override
    public void onBackPressed() {

        if (active instanceof SearchFragment) {
            if (((SearchFragment) active).onBackPressed()) {

            } else {
                existOnBackPressed();
            }
        } else {
            existOnBackPressed();
        }


    }

    public void existOnBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MyUser userUtil = new MyUser(this);
        userUtil.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: " + photosInterstitialAdCounter);
        if (photosInterstitialAdCounter >= 5) {

            mInterstitialAd.showAd();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                if (active.getTag().equals("1")) {
                    ((HomeFragment) active).scrollToTop();
                }
                mFragmentManager.beginTransaction().hide(active).show(mHomeFragment).commit();
                active = mHomeFragment;
                ((HomeFragment)active).handleNetworkErrLayout();
                return true;
            case R.id.navigation_explore:
                if (active.getTag().equals("2")) {
                    ((SearchFragment) active).scrollToTop();
                }
                mFragmentManager.beginTransaction().hide(active).show(mSearchFragment).commit();
                active = mSearchFragment;
                ((SearchFragment)active).handleNetworkErrLayout();
                return true;
            case R.id.navigation_favourites:
                if (active.getTag().equals("3")) {
                    ((FavouritesFragment) active).scrollToTop();
                }
                mFragmentManager.beginTransaction().hide(active).show(mFavouritesFragment).commit();
                active = mFavouritesFragment;
                return true;
            case R.id.navigation_profile:
                if (active.getTag().equals("4")) {
                    ((ProfileFragment) active).scrollToTop();
                }
                mFragmentManager.beginTransaction().hide(active).show(mProfileFragment).commit();
                active = mProfileFragment;
                return true;
        }

        return false;
    }

}
