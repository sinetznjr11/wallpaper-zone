package com.sinetcodes.wallpaperzone.Utilities;

import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;

public class CrashlyticsUtil {
    private FirebaseAnalytics mFirebaseAnalytics;
    private Context context;

    public CrashlyticsUtil(Context context) {
        this.context = context;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    public void photoDownloadedLog(){

    }
}
