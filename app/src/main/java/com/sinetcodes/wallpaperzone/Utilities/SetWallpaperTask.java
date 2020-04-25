package com.sinetcodes.wallpaperzone.Utilities;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import androidx.fragment.app.FragmentTransaction;

import com.sinetcodes.wallpaperzone.Dialogs.WallpaperSetDialog;
import com.sinetcodes.wallpaperzone.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class SetWallpaperTask extends AsyncTask<String, String, String> {
    private static final String TAG = "SetWallpaperTask";
    Context mContext;
    OnWallpaperSetListener mListener;
    public SetWallpaperTask(Context context,OnWallpaperSetListener listener) {
        mContext = context;
        mListener=listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }

    @Override
    protected String doInBackground(String... urls) {

        try {
            Bitmap result = Picasso.get()
                    .load(urls[0])
                    .get();

            WallpaperManager wallpaperManager=WallpaperManager.getInstance(mContext);
            wallpaperManager.setBitmap(result);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mListener.onCompletedSetting();
    }

    public interface OnWallpaperSetListener{
        void onCompletedSetting();
    }


}
