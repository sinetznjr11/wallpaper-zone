package com.sinetcodes.wallpaperzone.Common;

import android.content.Context;
import android.util.Log;

import com.sinetcodes.wallpaperzone.Utilities.SetUpRetrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    private OkHttpClient okHttpClient;
    private static Retrofit retrofit = null;
    private static final String TAG = "ApiClient";

    public ApiClient(Context context) {
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    public Retrofit getOkHttpClient() {
        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(SetUpRetrofit.getBaseUrl())
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
