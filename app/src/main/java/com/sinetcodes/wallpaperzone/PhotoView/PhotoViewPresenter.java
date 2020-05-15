package com.sinetcodes.wallpaperzone.PhotoView;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.sinetcodes.wallpaperzone.POJO.Photos;
import com.sinetcodes.wallpaperzone.Utilities.AppUtil;
import com.sinetcodes.wallpaperzone.Utilities.FirebaseEventManager;
import com.sinetcodes.wallpaperzone.Utilities.StringsUtil;

import java.io.File;
import java.util.List;

public class PhotoViewPresenter implements UserPhotosMVPInterface.presenter {
    Context mContext;
    UserPhotosMVPInterface.view mView;
    PhotoViewModel mModel;

    public int currentPosition = 0;

    private static final String TAG = "PhotoViewPresenter";


    public PhotoViewPresenter(Context context, UserPhotosMVPInterface.view view) {
        mContext = context;
        mView = view;
        mModel = new PhotoViewModel(context, this);
    }

    @Override
    public void getContent(String userName) {
        if (mView != null)
            mModel.askContent(userName);
    }

    @Override
    public void takeContent(List<Photos> photosList) {
        if (mView != null) {
            photosList.remove(0);
            mView.setContent(photosList);
        }

    }

    @Override
    public void checkIsPhotoInFavorites(String photoId) {
        mModel.isPhotoInFavorites(photoId);
    }

    @Override
    public void isPhotoInFavoritesResponse(boolean isInFavorites) {
        mView.setIsInFavorites(isInFavorites);
    }

    public void downloadFile(Photos photoItem, String operation) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String QUALITY;
        String filePath = Environment.getExternalStorageDirectory() + "/Wallpaper_Zone/";
        String url = "";
        String fileName = "";

        mView.onDownloadStarted();

        if (operation.equals(StringsUtil.WALLPAPER_DOWNLOAD)) {
            QUALITY = sharedPreferences.getString("download_quality", "null");
        } else {
            QUALITY = sharedPreferences.getString("wallpaper_quality", "null");
        }

        if (!QUALITY.equals("null")) {
            switch (QUALITY) {
                case "full":
                    url = photoItem.getUrls().getFull();
                    fileName = photoItem.getId() + StringsUtil.SEPARATOR +"full" + ".jpg";
                    break;
                case "regular":
                    url = photoItem.getUrls().getRegular();
                    fileName = photoItem.getId() + StringsUtil.SEPARATOR +"regular" + ".jpg";
                    break;
                case "small":
                    url = photoItem.getUrls().getSmall();
                    fileName = photoItem.getId()+ StringsUtil.SEPARATOR  + "small" + ".jpg";
                    break;
            }
        }


        Log.d(TAG, "downloadFile: file name: " + fileName + " file path: " + filePath);

        //check if file exits or not before downloading
        File photoFile = new File(filePath + fileName);
        if (!photoFile.exists()) {
            PRDownloader
                    .download(url, filePath, fileName)
                    .build()
                    .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                        @Override
                        public void onStartOrResume() {
                            Log.d(TAG, "onStartOrResume: Download Started");
                        }
                    })
                    .setOnProgressListener(new OnProgressListener() {
                        @Override
                        public void onProgress(Progress progress) {

                        }
                    })
                    .setOnPauseListener(new OnPauseListener() {
                        @Override
                        public void onPause() {

                        }
                    })
                    .start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            new FirebaseEventManager(mContext).downloadWallpaperEvent(photoItem.getUrls().getSmall(), AppUtil.getDeviceId(mContext));
                            scanGallery(filePath);

                            Log.d(TAG, "onDownloadComplete: Download Completed");

                            if (operation.equals(StringsUtil.WALLPAPER_DOWNLOAD))
                                mView.onDownloadCompleted();
                            else if (operation.equals(StringsUtil.WALLPAPER_SET)) {
                                setWallpaper(photoFile, photoItem.getUrls().getSmall());
                                mView.onDownloadDialogDismiss();
                            }

                        }

                        @Override
                        public void onError(Error error) {
                            mView.onDownloadDialogDismiss();
                            Log.e(TAG, "onError: " + error.getServerErrorMessage());
                        }
                    });

        } else {
            Log.e(TAG, "downloadFile: File already exists");
            mView.onDownloadDialogDismiss();
            if (operation.equals(StringsUtil.WALLPAPER_DOWNLOAD)) {
                Toast.makeText(mContext, "Already downloaded this file.", Toast.LENGTH_SHORT).show();
            } else
                setWallpaper(photoFile, photoItem.getUrls().getSmall());
        }
    }

    private void setWallpaper(File photoFile, String url) {
        Uri photoUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", photoFile);
        try {
            Intent wallpaperIntent = WallpaperManager.getInstance(mContext).getCropAndSetWallpaperIntent(photoUri);
            wallpaperIntent.setDataAndType(photoUri, "image/*");
            wallpaperIntent.putExtra("mimeType", "image/*");
            ((Activity) mContext).startActivityForResult(wallpaperIntent, 13451);
        } catch (Exception e) {
            e.printStackTrace();
            Intent wallpaperIntent = new Intent(Intent.ACTION_ATTACH_DATA);
            wallpaperIntent.setDataAndType(photoUri, "image/*");
            wallpaperIntent.putExtra("mimeType", "image/*");
            wallpaperIntent.addCategory(Intent.CATEGORY_DEFAULT);
            wallpaperIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            wallpaperIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            wallpaperIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            mContext.startActivity(Intent.createChooser(wallpaperIntent, "Set as wallpaper"));
        }
        new FirebaseEventManager(mContext).wallpaperSetEvent(url, AppUtil.getDeviceId(mContext));
    }

    @Override
    public void showToast(String message) {
        mView.showToast(message);
    }

    private void scanGallery(String filePath) {
        MediaScannerConnection.scanFile(mContext,
                new String[]{filePath}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.d(TAG, "onScanCompleted: " + path + ", Uri: " + uri.toString());
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mView.showToast("Saved to " + path);
                            }
                        });
                    }
                });
    }

    public void addOrRemoveFromFavorites(boolean isInFavorites, Photos photo) {
        if (!isInFavorites)
            mModel.addFavoriteToDatabase(photo);
        else
            mModel.removeFavoriteFromDatabase(photo);
    }
}
