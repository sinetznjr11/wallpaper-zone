package com.sinetcodes.wallpaperzone.Profile;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class ProfileModel implements ProfileImpl.model {

    private static final String TAG = "ProfileModel";
    Context mContext;
    ProfileImpl.presenter mPresenter;

    public ProfileModel(Context context, ProfilePresenter presenter) {
        mContext = context;
        mPresenter = presenter;
    }

    @Override
    public void askDownloads() {
        String path = Environment.getExternalStorageDirectory().toString()+"/Wallpaper_Zone";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        if (directory.exists()){
            File[] files = directory.listFiles();
            Log.d("Files", "Size: "+ files.length);
            for (int i = 0; i < files.length; i++) {
                Log.d("Files", "FileName:" + files[i].getName());
            }
            mPresenter.takeDownloads(files);
        }

    }
}
