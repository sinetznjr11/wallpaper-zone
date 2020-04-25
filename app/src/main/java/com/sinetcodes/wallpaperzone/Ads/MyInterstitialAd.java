package com.sinetcodes.wallpaperzone.Ads;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.sinetcodes.wallpaperzone.Activities.MainActivity;

public class MyInterstitialAd extends AdListener {
    InterstitialAd mInterstitialAd;
    Context mContext;
    AdRequest mAdRequest;
    boolean showFlag = false;

    private static final String TAG = "MyInterstitialAd";

    public MyInterstitialAd(Context context,String adUnit) {
        super();
        mAdRequest = new AdRequest.Builder().build();
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(adUnit);
        mInterstitialAd.loadAd(mAdRequest);
        mInterstitialAd.setAdListener(this);
    }

    public void showAd() {
        Log.d(TAG, "showAd:"+ MainActivity.photosInterstitialAdCounter);
        showFlag=true;
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }else{
            mInterstitialAd.loadAd(mAdRequest);
        }
    }

    @Override
    public void onAdClosed() {
        super.onAdClosed();
        Log.e(TAG, "onAdClosed:");
        mInterstitialAd.loadAd(mAdRequest);
    }

    @Override
    public void onAdFailedToLoad(int i) {
        super.onAdFailedToLoad(i);
    }

    @Override
    public void onAdLeftApplication() {
        super.onAdLeftApplication();
    }

    @Override
    public void onAdOpened() {
        MainActivity.photosInterstitialAdCounter=0;
        super.onAdOpened();
        showFlag=false;
    }

    @Override
    public void onAdLoaded() {
        super.onAdLoaded();
        if(showFlag){
            mInterstitialAd.show();
            showFlag=false;
        }
    }

    @Override
    public void onAdClicked() {
        super.onAdClicked();
    }

    @Override
    public void onAdImpression() {
        super.onAdImpression();
    }
}
