package com.sinetcodes.wallpaperzone.Utilities;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseEventManager {
    Context mContext;
    FirebaseAnalytics analytics;
    public static final String DEVICE_ID="device_id";
    public static final String DUMMY_LOGIN="dummy_login";
    public static final String DUMMY_SIGN_UP="dummy_sign_up";

    public static final String USER_LOGIN="user_login";
    public static final String USER_SIGN_UP="user_sign_up";

    public static final String SET_WALLPAPER="set_wallpaper";
    public static final String DOWNLOAD_WALLPAPER="download_wallpaper";

    public static final String ADD_TO_FAV="add_to_favorites";
    public static final String REMOVE_FROM_FAV="remove_from_favorites";

    public static final String SHARE_IMAGE="share_image";

    public FirebaseEventManager(Context context) {
        mContext = context;
        analytics = FirebaseAnalytics.getInstance(mContext);
    }

    public void dummyLoginEvent(String uid, String deviceId) {
        Bundle bundle = new Bundle();
        bundle.putString(DEVICE_ID,deviceId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID,uid);
        analytics.logEvent(DUMMY_LOGIN,bundle);
    }

    public void dummySignUpEvent(String uid, String deviceId) {
        Bundle bundle = new Bundle();
        bundle.putString(DEVICE_ID,deviceId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID,uid);
        analytics.logEvent(DUMMY_SIGN_UP,bundle);
    }

    public void wallpaperSetEvent(String url,String deviceId){
        Bundle bundle = new Bundle();
        bundle.putString(DEVICE_ID,deviceId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID,url);
        analytics.logEvent(SET_WALLPAPER,bundle);
    }

    public void downloadWallpaperEvent(String url, String deviceId){
        Bundle bundle = new Bundle();
        bundle.putString(DEVICE_ID,deviceId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID,url);
        analytics.logEvent(DOWNLOAD_WALLPAPER,bundle);
    }

    public void addToFavEvent(String url,String deviceId){
        Bundle bundle = new Bundle();
        bundle.putString(DEVICE_ID,deviceId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID,url);
        analytics.logEvent(ADD_TO_FAV,bundle);
    }

    public void removeFromFav(String url, String deviceId){
        Bundle bundle = new Bundle();
        bundle.putString(DEVICE_ID,deviceId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID,url);
        analytics.logEvent(REMOVE_FROM_FAV,bundle);
    }

    public void shareEvent(String url, String deviceId){
        Bundle bundle = new Bundle();
        bundle.putString(DEVICE_ID,deviceId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID,url);
        analytics.logEvent(SHARE_IMAGE,bundle);
    }


}
