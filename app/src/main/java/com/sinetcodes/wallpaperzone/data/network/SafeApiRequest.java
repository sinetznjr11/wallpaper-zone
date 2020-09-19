package com.sinetcodes.wallpaperzone.data.network;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;


import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SafeApiRequest {

    public static <T> MutableLiveData<List<T>> callRetrofit(Call<List<T>> call) {
        MutableLiveData<List<T>> responseList = new MutableLiveData<>();
        Log.e("inside", call.request().url()+"------");

        call.enqueue(new Callback<List<T>>() {
            @Override
            public void onResponse(Call<List<T>> call, Response<List<T>> response) {
                responseList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<T>> call, Throwable t) {
                Log.e("inside", t.getMessage());
            }
        });
        return responseList;
    }

    public static <T> MutableLiveData<T> callRetrofitObjectResponse(Call<T> call) {
        MutableLiveData<T> responseObject = new MutableLiveData<>();
        Log.e("inside", call.request().url()+"------");

        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                responseObject.setValue(response.body());
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Log.e("inside", t.getMessage());
            }
        });

        return responseObject;
    }

    public static <T> LiveData<T> callRetrofitObservableObject(Flowable<T> flowable) {
        MediatorLiveData<T> observeObject = new MediatorLiveData<>();
        LiveData<T> source=LiveDataReactiveStreams.fromPublisher(
                flowable.subscribeOn(Schedulers.io())
        );


        observeObject.addSource(source, t -> {
            observeObject.setValue(t);
            observeObject.removeSource(source);
        });

        return observeObject;
    }

}


