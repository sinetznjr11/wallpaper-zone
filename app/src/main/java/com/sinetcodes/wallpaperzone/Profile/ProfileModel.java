package com.sinetcodes.wallpaperzone.Profile;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.sinetcodes.wallpaperzone.Common.ApiClient;
import com.sinetcodes.wallpaperzone.Common.CommonApiInterface;
import com.sinetcodes.wallpaperzone.POJO.PhotoFile;
import com.sinetcodes.wallpaperzone.POJO.Photos;
import com.sinetcodes.wallpaperzone.Utilities.SetUpRetrofit;
import com.sinetcodes.wallpaperzone.Utilities.StringsUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        List<PhotoFile> photoFileList = new ArrayList<>();
        String path = Environment.getExternalStorageDirectory().toString() + "/Wallpaper_Zone";
        Log.d("Files", "Path: " + path);
        try {
            File directory = new File(path);
            if (directory.exists()) {
                File[] files = directory.listFiles();
                for (int i = 0; i < files.length; i++) {
                    String[] parts = files[i].getName().split(StringsUtil.SEPARATOR);
                    Log.d("Files", "FileName:" + files[i].getName() + " ,key=" + parts[0]);

                    photoFileList.add(new PhotoFile(parts[0],files[i]));
                }
                mPresenter.takeDownloads(photoFileList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "askDownloads: " + e.getMessage());
        }

    }

    @Override
    public void askPhotoFromId(String photoId){
        ApiClient apiClient = new ApiClient(mContext);
        CommonApiInterface apiInterface = apiClient.getOkHttpClient().create(CommonApiInterface.class);
        try {
            Call<Photos> call=apiInterface.getPhoto(photoId,SetUpRetrofit.getUnsplashClientId());
            call.enqueue(new Callback<Photos>() {
                @Override
                public void onResponse(Call<Photos> call, Response<Photos> response) {
                    if(response.isSuccessful())
                        mPresenter.takePhotoFromId(response.body());
                    else
                        mPresenter.onError(response.message());
                }

                @Override
                public void onFailure(Call<Photos> call, Throwable t) {
                        mPresenter.onError(t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            mPresenter.onError(e.getMessage());
        }
    }
}
