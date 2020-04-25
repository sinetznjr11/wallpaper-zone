package com.sinetcodes.wallpaperzone.PhotoView;

import android.content.Context;

import com.sinetcodes.wallpaperzone.Common.ApiClient;
import com.sinetcodes.wallpaperzone.Common.CommonApiInterface;
import com.sinetcodes.wallpaperzone.POJO.Photos;
import com.sinetcodes.wallpaperzone.Utilities.SetUpRetrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MorePhotoModel implements UserPhotosMVCInterface.model{
    Context mContext;
    UserPhotosMVCInterface.presenter mPresenter;

    public MorePhotoModel(Context context, UserPhotosMVCInterface.presenter presenter) {
        mContext = context;
        mPresenter = presenter;
    }

    @Override
    public void askContent(String userName) {
        ApiClient apiClient = new ApiClient(mContext);
        CommonApiInterface apiInterface=apiClient.getOkHttpClient().create(CommonApiInterface.class);

        try {
            Call<List<Photos>> listCall=apiInterface.getUserPhotos(userName, SetUpRetrofit.getUnsplashClientId(),1,20,"popular","portrait");
            listCall.enqueue(new Callback<List<Photos>>() {
                @Override
                public void onResponse(Call<List<Photos>> call, Response<List<Photos>> response) {
                    if(response.isSuccessful()){
                        mPresenter.takeContent(response.body());
                    }
                }

                @Override
                public void onFailure(Call<List<Photos>> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    }

