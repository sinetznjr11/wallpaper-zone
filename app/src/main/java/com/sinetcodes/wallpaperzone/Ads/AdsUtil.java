package com.sinetcodes.wallpaperzone.Ads;

import com.sinetcodes.wallpaperzone.BuildConfig;

public class AdsUtil {

    public static final String MULTIPLE_PHOTOS_INTERSTITIAL="multiple_photos_interstitial";
    public static final String MULTIPLE_PHOTOS_INTERSTITIAL_AD_UNIT="ca-app-pub-9607577251750757/6281590010";
    public static final String MULTIPLE_PHOTOS_INTERSTITIAL_TEST_AD_UNIT="ca-app-pub-3940256099942544/1033173712";

    public static final String SEARCH_FRAGMENT_NATIVE="search_fragment_native";
    public static final String SEARCH_FRAGMENT_NATIVE_AD_UNIT="ca-app-pub-9607577251750757/9460279513";
    public static final String SEARCH_FRAGMENT_NATIVE_TEST_AD_UNIT="ca-app-pub-3940256099942544/2247696110";


    String testBannerAd="ca-app-pub-3940256099942544/6300978111";
    String liveBannerAd="ca-app-pub-9607577251750757/6761595451";

    String testRewardAd="ca-app-pub-3940256099942544/5224354917";
    String liveRewardAd="ca-app-pub-9607577251750757/7016870033";


    public  String getBannerAdUnit() {
        if(BuildConfig.DEBUG){
        return testBannerAd;
        }
        else{
        return liveBannerAd;
        }
    }

    public String getRewardAdUnit() {
        if(BuildConfig.DEBUG){
            return testRewardAd;
        }
        else{
            return liveRewardAd;
        }
    }

    public static String getAdUnit(String adUnitName){
        String adUnit="";
        if(BuildConfig.DEBUG){
            switch (adUnitName){
                case MULTIPLE_PHOTOS_INTERSTITIAL:
                    adUnit=MULTIPLE_PHOTOS_INTERSTITIAL_TEST_AD_UNIT;
                    break;

                case SEARCH_FRAGMENT_NATIVE:
                    adUnit=SEARCH_FRAGMENT_NATIVE_TEST_AD_UNIT;
                    break;

            }
        }
        else {
            switch (adUnitName){
                case MULTIPLE_PHOTOS_INTERSTITIAL:
                    adUnit=MULTIPLE_PHOTOS_INTERSTITIAL_AD_UNIT;
                    break;
                case SEARCH_FRAGMENT_NATIVE:
                    adUnit=SEARCH_FRAGMENT_NATIVE_AD_UNIT;
                    break;
            }
        }


        return  adUnit;
    }
}
