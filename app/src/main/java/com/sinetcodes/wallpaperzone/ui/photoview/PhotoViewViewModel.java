package com.sinetcodes.wallpaperzone.ui.photoview;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.sinetcodes.wallpaperzone.data.network.responses.Wallpaper;
import com.sinetcodes.wallpaperzone.pojo.Photos;
import com.sinetcodes.wallpaperzone.utils.AppUtil;
import com.sinetcodes.wallpaperzone.utils.FirebaseEventManager;
import com.sinetcodes.wallpaperzone.utils.StringsUtil;

import java.io.File;

public class PhotoViewViewModel extends ViewModel {

    private static final String TAG = "PhotoViewViewModel";

    MutableLiveData<Wallpaper> mWallpaperMutableLiveData;

    MutableLiveData<String> mDownloadListener;


    public PhotoViewViewModel() {
        mWallpaperMutableLiveData = new MutableLiveData<>();
        mDownloadListener = new MutableLiveData<>();
    }

    public void onBackClick(View view) {
        ((AppCompatActivity) view.getContext()).onBackPressed();
    }

    public void onSetWallpaperClick(View view) {
        Log.e(TAG, "onSetWallpaperClick: ");
    }

    public void onShareClick(View view, String imageUrl) {
        Glide.with(view.getContext())
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        new FirebaseEventManager(view.getContext()).shareEvent(imageUrl, AppUtil.getDeviceId(view.getContext()));
                        i.setType("*/*");
                        i.putExtra(Intent.EXTRA_TEXT, "Hey check this amazing wallpaper from Wallpaper Zone app. \n " +
                                "You can download the app here, " + StringsUtil.PLAYSTORE_LINK + StringsUtil.PACKAGE_NAME);
                        i.putExtra(Intent.EXTRA_STREAM, AppUtil.getLocalBitmapUri(resource, view.getContext()));
                        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        view.getContext().startActivity(Intent.createChooser(i, "Share Image"));
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    public void onDownloadClick(View view) {
        Log.e(TAG, "onDownloadClick: ");
    }

    public void onFavoriteClick(View view) {
        Log.e(TAG, "onFavoriteClick: ");
    }

    public MutableLiveData<Wallpaper> getWallpaperMutableLiveData() {
        return mWallpaperMutableLiveData;
    }

    public void setWallpaperMutableLiveData(Wallpaper wallpaper) {
        mWallpaperMutableLiveData.setValue(wallpaper);
    }

    public void downloadFile(Context context, Wallpaper wallpaper, String operation) {
        String filePath = Environment.getExternalStorageDirectory() + "/Wallpaper_Zone/";
        String url = "";
        String fileName = "";

        mDownloadListener.setValue(StringsUtil.STARTED);


        url = wallpaper.getUrlImage();
        fileName = wallpaper.getId() + StringsUtil.SEPARATOR + wallpaper.getUserId() + ".jpg";


        Log.d(TAG, "downloadFile: file name: " + fileName + " file path: " + filePath);

        //check if file exits or not before downloading
        File photoFile = new File(filePath + fileName);
        if (!photoFile.exists()) {
            PRDownloader
                    .download(url, filePath, fileName)
                    .build()
                    .setOnStartOrResumeListener(() -> Log.d(TAG, "onStartOrResume: Download Started"))
                    .setOnProgressListener(progress -> {

                    })
                    .setOnPauseListener(() -> {

                    })
                    .start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            new FirebaseEventManager(context).downloadWallpaperEvent(wallpaper.getUrlImage(), AppUtil.getDeviceId(context));
                            scanGallery(context, filePath);

                            Log.d(TAG, "onDownloadComplete: Download Completed");

                            if (operation.equals(StringsUtil.WALLPAPER_DOWNLOAD))
                                mDownloadListener.setValue(StringsUtil.COMPLETED);
                            else if (operation.equals(StringsUtil.WALLPAPER_SET)) {
                                setWallpaper(context, photoFile, wallpaper.getUrlImage());
                                mDownloadListener.setValue(StringsUtil.DISMISS);

                            }

                        }

                        @Override
                        public void onError(Error error) {
                            mDownloadListener.setValue(StringsUtil.DISMISS);
                            Log.e(TAG, "onError: " + error.getServerErrorMessage());
                        }
                    });

        } else {
            Log.e(TAG, "downloadFile: File already exists");
            //mView.onDownloadDialogDismiss();
            mDownloadListener.setValue(StringsUtil.DISMISS);
            if (operation.equals(StringsUtil.WALLPAPER_DOWNLOAD)) {
                Toast.makeText(context, "Already downloaded this file.", Toast.LENGTH_SHORT).show();
            } else
                setWallpaper(context, photoFile, wallpaper.getUrlImage());
        }
    }

    private void setWallpaper(Context context, File photoFile, String url) {
        Uri photoUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", photoFile);
        try {
            Intent wallpaperIntent = WallpaperManager.getInstance(context).getCropAndSetWallpaperIntent(photoUri);
            wallpaperIntent.setDataAndType(photoUri, "image/*");
            wallpaperIntent.putExtra("mimeType", "image/*");
            ((Activity) context).startActivityForResult(wallpaperIntent, 13451);
        } catch (Exception e) {
            e.printStackTrace();
            Intent wallpaperIntent = new Intent(Intent.ACTION_ATTACH_DATA);
            wallpaperIntent.setDataAndType(photoUri, "image/*");
            wallpaperIntent.putExtra("mimeType", "image/*");
            wallpaperIntent.addCategory(Intent.CATEGORY_DEFAULT);
            wallpaperIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            wallpaperIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            wallpaperIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            context.startActivity(Intent.createChooser(wallpaperIntent, "Set as wallpaper"));
        }
        new FirebaseEventManager(context).wallpaperSetEvent(url, AppUtil.getDeviceId(context));
    }

    private void scanGallery(Context context, String filePath) {
        MediaScannerConnection
                .scanFile(context.getApplicationContext(), new String[]{filePath}, null, (path, uri) -> {
                    Log.d(TAG, "onScanCompleted: " + path);
                    ((Activity) context).runOnUiThread(() -> Toast.makeText(context, "Saved to " + path, Toast.LENGTH_SHORT).show());
                });
    }

    public MutableLiveData<String> getDownloadListener() {
        return mDownloadListener;
    }
}
