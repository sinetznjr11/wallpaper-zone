package com.sinetcodes.wallpaperzone.Utilities;

import android.content.Context;
import android.content.Intent;

import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.sinetcodes.wallpaperzone.Activities.MainActivity;

public class NotificationHandler implements OneSignal.NotificationOpenedHandler {

    Context mContext;

    public NotificationHandler(Context context) {
        mContext = context;
    }

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        try {
            if (result != null) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                /*intent.putExtra("push_title", title);
                intent.putExtra("push_message", desc);*/
                //mContext.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
