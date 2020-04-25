package com.sinetcodes.wallpaperzone.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Random;
import java.util.UUID;

public class AppUtil {
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
//onesignal app id eefcc975-b92b-4463-90d1-a0f7cebf921b

    public static int getStatusBarHeight(Activity context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void setTransparentStatusBar(Activity activity) {
        //setting transparent status bar.
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    public synchronized static String getDeviceId(Context context) {
        String uniqueID = null;
        SharedPreferences sharedPrefs = context.getSharedPreferences(
                PREF_UNIQUE_ID, Context.MODE_PRIVATE);
        uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
        if (uniqueID == null) {
            uniqueID = UUID.randomUUID().toString();
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString(PREF_UNIQUE_ID, uniqueID);
            editor.apply();
        }

        return uniqueID;
    }

    public static String getRandomQuery() {
        Random random = new Random();
            String[] names = {
                    "nature",
                    "cool",
                    "cars",
                    "wallpaper",
                    "pink",
                    "minimal",
                    "bikes",
                    "mountains",
                    "rivers",
                    "space",
                    "moon",
                    "dark",
                    "couples",
                    "vintage",
                    "instagram",
                    "aesthetic",
                    "decor",
                    "edits",
                    "sports",
                    "urban",
                    "street",
                    "graffiti",
                    "subtle",
                    "fitness",
                    "random",
                    "amazing",
                    "moody",
                    "interiors",
                    "experimental",
                    "textures",
                    "patterns",
                    "arts",
                    "abstract",
                    "technology",
                    "architecture",
                    "cute",
                    "pets"
            };

        return names[random.nextInt(names.length)];
    }
}
