package com.sinetcodes.wallpaperzone.data.network;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SafeApiRequest {

    private static final String TAG = "SafeApiRequest";

    public static <T> MutableLiveData<List<T>> callRetrofit(Call<List<T>> call) {
        MutableLiveData<List<T>> responseList = new MutableLiveData<>();
        Log.e("inside", call.request().url() + "------");

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
        Log.e(TAG, call.request().url() + "------");

        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                responseObject.setValue(response.body());
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });

        return responseObject;
    }

    public static <T> MutableLiveData<T> callRetrofitObservableObject(Observable<T> observable) {
        MutableLiveData<T> observeObject = new MutableLiveData<>();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull T t) {
                        observeObject.setValue(t);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return observeObject;
    }

}


