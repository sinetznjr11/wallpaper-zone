package com.sinetcodes.wallpaperzone.Search;

import android.content.Context;
import android.util.Log;

import com.sinetcodes.wallpaperzone.Common.ApiClient;
import com.sinetcodes.wallpaperzone.Common.CommonApiInterface;
import com.sinetcodes.wallpaperzone.POJO.Photos;
import com.sinetcodes.wallpaperzone.POJO.Results;
import com.sinetcodes.wallpaperzone.Utilities.FirebaseEventManager;
import com.sinetcodes.wallpaperzone.Utilities.SetUpRetrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchModel implements SearchMVPInterface.model {
    private SearchMVPInterface.presenter presenter;
    Context context;

    private static final String TAG = "HomeModel";

    public SearchModel(SearchMVPInterface.presenter presenter, Context context) {
        this.presenter = presenter;
        this.context = context;
    }

    @Override
    public void askRandomPhotos(String query, int count) {
        //perform API Call

        ApiClient apiClient = new ApiClient(context);
        CommonApiInterface apiInterface = apiClient.getOkHttpClient().create(CommonApiInterface.class);
        Call<List<Photos>> call = apiInterface.getRandomPhoto(
                SetUpRetrofit.getUnsplashClientId(),
                query,
                count,
                "portrait"
        );


        try {
            call.enqueue(new Callback<List<Photos>>() {
                @Override
                public void onResponse(Call<List<Photos>> call, Response<List<Photos>> response) {
                    if (response.isSuccessful()) {
                        presenter.takeContent(response.body());
                        presenter.takeTotalResults(response.body().size());
                    }
                }

                @Override
                public void onFailure(Call<List<Photos>> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void askSearchResults(String query, int page) {

        new FirebaseEventManager(context).searchQueryEvent(query);
        ApiClient apiClient = new ApiClient(context);
        CommonApiInterface apiInterface = apiClient.getOkHttpClient().create(CommonApiInterface.class);
        Call<Results> call = apiInterface.getSearchResults(
                SetUpRetrofit.getUnsplashClientId(),
                query,
                page,
                30,
                "popular",
                "portrait"
        );

        try {
            call.enqueue(new Callback<Results>() {
                @Override
                public void onResponse(Call<Results> call, Response<Results> response) {
                    if (response.isSuccessful()) {
                        presenter.takeContent(response.body().getPhotos());
                        presenter.takeTotalResults(response.body().getTotal());
                    }
                }

                @Override
                public void onFailure(Call<Results> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void askCollectionPhotos(int collectionId,int page) {
        ApiClient apiClient = new ApiClient(context);
        CommonApiInterface apiInterface = apiClient.getOkHttpClient().create(CommonApiInterface.class);
        Call<List<Photos>> call = apiInterface.getCollectionPhotos(
                collectionId,
                SetUpRetrofit.getUnsplashClientId(),
                page,
                30
        );

        try {
            call.enqueue(new Callback<List<Photos>>() {
                @Override
                public void onResponse(Call<List<Photos>> call, Response<List<Photos>> response) {
                    if(response.isSuccessful()){
                        presenter.takeContent(response.body());
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
