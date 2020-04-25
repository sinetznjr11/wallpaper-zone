package com.sinetcodes.wallpaperzone.Search;

import android.content.Context;
import android.util.Log;

import com.sinetcodes.wallpaperzone.Common.ApiClient;
import com.sinetcodes.wallpaperzone.Common.CommonApiInterface;
import com.sinetcodes.wallpaperzone.POJO.Photos;
import com.sinetcodes.wallpaperzone.POJO.Results;
import com.sinetcodes.wallpaperzone.Utilities.SetUpRetrofit;

import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeModel implements HomeMVPInterface.homeModel {
    private HomeMVPInterface.homePresenter homePresenter;
    Context context;

    private static final String TAG = "HomeModel";

    public HomeModel(HomeMVPInterface.homePresenter homePresenter, Context context) {
        this.homePresenter = homePresenter;
        this.context = context;
    }

    @Override
    public void askContent(String query,int count) {
        //perform API Call

        ApiClient apiClient = new ApiClient(context);
        CommonApiInterface apiInterface=apiClient.getOkHttpClient().create(CommonApiInterface.class);
        Call<List<Photos>> call=apiInterface.getRandomPhoto(
                SetUpRetrofit.getUnsplashClientId(),
                query,
                count,
                "portrait"
        );


        try {
            call.enqueue(new Callback<List<Photos>>() {
                @Override
                public void onResponse(Call<List<Photos>> call, Response<List<Photos>> response) {
                    if(response.isSuccessful()){

                        homePresenter.takeContent(response.body());
                    }
                }

                @Override
                public void onFailure(Call<List<Photos>> call, Throwable t) {
                    Log.e(TAG, "onFailure: "+t.getMessage() );
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
