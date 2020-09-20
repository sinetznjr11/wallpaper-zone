package com.sinetcodes.wallpaperzone.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sinetcodes.wallpaperzone.Common.ContentType;
import com.sinetcodes.wallpaperzone.R;
import com.sinetcodes.wallpaperzone.data.network.ApiInterface;
import com.sinetcodes.wallpaperzone.data.network.SafeApiRequest;
import com.sinetcodes.wallpaperzone.data.network.SetupRetrofit;
import com.sinetcodes.wallpaperzone.data.network.responses.CategoryList;
import com.sinetcodes.wallpaperzone.data.network.responses.WallpaperList;
import com.sinetcodes.wallpaperzone.pojo.HomeItem;
import com.sinetcodes.wallpaperzone.utils.StringsUtil;
import com.sinetcodes.wallpaperzone.utils.UrlUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class WallpaperRepository extends SafeApiRequest {

    private static final String TAG = "WallpaperRepository";

    ApiInterface mApiInterface;

    MutableLiveData<List<HomeItem>> homeItemsLiveData=new MutableLiveData<>();

    MutableLiveData<WallpaperList> mWallpaperListMutableLiveData=new MutableLiveData<>();

    public WallpaperRepository() {
        this.mApiInterface = SetupRetrofit.createService(ApiInterface.class);
    }


    public void getHomeItems() {

        List<HomeItem> homeItemList = new ArrayList<>();
        Observable<WallpaperList> featuredFlowable = mApiInterface.getWallpaperList(UrlUtil.getUrl(StringsUtil.FEATURED, 1));
        Observable<CategoryList> categoryFlowable = mApiInterface.getCategoryList(UrlUtil.getUrl(StringsUtil.CATEGORY_LIST, 0));
        Observable<WallpaperList> newestFlowable = mApiInterface.getWallpaperList(UrlUtil.getUrl(StringsUtil.NEWEST, 1));
        Observable<WallpaperList> popularFlowable = mApiInterface.getWallpaperList(UrlUtil.getUrl(StringsUtil.POPULAR, 1));
        Observable<WallpaperList> highRatedFlowable = mApiInterface.getWallpaperList(UrlUtil.getUrl(StringsUtil.HIGH_RATED, 1));
        Observable<WallpaperList> highViewedFlowable = mApiInterface.getWallpaperList(UrlUtil.getUrl(StringsUtil.HIGH_VIEWS, 1));


        Observable<List<HomeItem>> result =
                Observable.zip(
                        featuredFlowable.subscribeOn(Schedulers.io()),
                        categoryFlowable.subscribeOn(Schedulers.io()),
                        newestFlowable.subscribeOn(Schedulers.io()),
                        popularFlowable.subscribeOn(Schedulers.io()),
                        highRatedFlowable.subscribeOn(Schedulers.io()),
                        highViewedFlowable.subscribeOn(Schedulers.io()),
                        (wallpaperList, categoryList, wallpaperList2, wallpaperList3, wallpaperList4, wallpaperList5) -> {
                            homeItemList.add(new HomeItem(ContentType.FEATURED, wallpaperList.getWallpapers()));
                            homeItemList.add(new HomeItem(ContentType.CATEGORY, categoryList.getCategories()));
                            homeItemList.add(new HomeItem(ContentType.NEWLY_ADDED, wallpaperList2.getWallpapers()));
                            homeItemList.add(new HomeItem(ContentType.POPULAR, wallpaperList3.getWallpapers()));
                            homeItemList.add(new HomeItem(ContentType.MOST_RATED, wallpaperList4.getWallpapers()));
                            homeItemList.add(new HomeItem(ContentType.MOST_VIEWED, wallpaperList5.getWallpapers()));
                            return homeItemList;
                        }
                );

        result.observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<List<HomeItem>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<HomeItem> homeItems) {
                        homeItemsLiveData.setValue(homeItems);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError: "+e.getMessage() );
                    }

                    @Override
                    public void onComplete() {

                    }
                });



    }

    public MutableLiveData<List<HomeItem>> getHomeItemsLiveData() {
        return homeItemsLiveData;
    }

    public MutableLiveData<WallpaperList> getWallpaperListLiveData(String url) {
        Log.e("URL", "url: "+url );
        return callRetrofitObservableObject(mApiInterface.getWallpaperList(url));
    }


}
