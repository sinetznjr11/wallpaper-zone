package com.sinetcodes.wallpaperzone.ui.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sinetcodes.wallpaperzone.data.network.responses.WallpaperList;
import com.sinetcodes.wallpaperzone.data.repository.WallpaperRepository;

public class ListViewModel extends ViewModel {
    private WallpaperRepository mRepository;

    MutableLiveData<WallpaperList> mWallpaperListMutableLiveData;

    public ListViewModel(WallpaperRepository repository) {
        mRepository = repository;
    }

    public void init(String url) {
        mWallpaperListMutableLiveData = mRepository.getWallpaperListLiveData(url);
    }

    public LiveData<WallpaperList> getWallpaperListMutableLiveData() {
        return mWallpaperListMutableLiveData;
    }
}
