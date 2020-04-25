package com.sinetcodes.wallpaperzone.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.sinetcodes.wallpaperzone.Ads.AdsUtil;
import com.sinetcodes.wallpaperzone.Ads.MyInterstitialAd;
import com.sinetcodes.wallpaperzone.Common.OnBackPressedFragment;
import com.sinetcodes.wallpaperzone.Home.HomeFragment;
import com.sinetcodes.wallpaperzone.Favourites.FavouritesFragment;
import com.sinetcodes.wallpaperzone.Search.SearchFragment;
import com.sinetcodes.wallpaperzone.R;
import com.sinetcodes.wallpaperzone.Utilities.UserUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private FirebaseAnalytics mFirebaseAnalytics;
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

   MyInterstitialAd mInterstitialAd;

    private static final String TAG = "MainActivity";

    public static int photosInterstitialAdCounter=0;


   /* @OnClick(R.id.btn_one)
    void onOneClicked(){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Button one.");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    @OnClick(R.id.btn_two)
    void onTwoClicked()
    {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "2");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Button two");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }*/
        //ca-app-pub-9607577251750757~6139064330
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //AppUtil.setTransparentStatusBar(this);


        mInterstitialAd=new MyInterstitialAd(this,AdsUtil.getAdUnit(AdsUtil.MULTIPLE_PHOTOS_INTERSTITIAL));



        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        openFragment(new HomeFragment());
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }



    public void openFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        FragmentTransaction ft = manager.beginTransaction();

        if (!fragmentPopped){ //fragment not in back stack, create it.
            ft.replace(R.id.main_frame, fragment);
        }
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.navigation_home:
                openFragment(HomeFragment.newInstance("",""));
                return true;
            case R.id.navigation_favourites:
                openFragment(FavouritesFragment.newInstance("",""));
                return true;
            case R.id.navigation_explore:
                openFragment(SearchFragment.newInstance("",""));
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {

        Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.main_frame);
        if(!(fragment instanceof OnBackPressedFragment) || !((OnBackPressedFragment) fragment).onBackPressed()){
            if (getSupportFragmentManager().getBackStackEntryCount() == 1){
                finish();
            }
            else {
                super.onBackPressed();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        UserUtil userUtil =new UserUtil(this);
        userUtil.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: "+photosInterstitialAdCounter);
        if(photosInterstitialAdCounter>=5){

            mInterstitialAd.showAd();
        }
    }
}
