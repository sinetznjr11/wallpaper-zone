package com.sinetcodes.wallpaperzone.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.sinetcodes.wallpaperzone.data.repository.WallpaperRepository;
import com.sinetcodes.wallpaperzone.pojo.HomeItem;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private WallpaperRepository mRepository;

    public HomeViewModel(WallpaperRepository repository) {
        mRepository = repository;
        mRepository.getHomeItems();
    }


    public LiveData<List<HomeItem>> getHomeItemLiveData() {
        return mRepository.getHomeItemsLiveData();
    }
}
