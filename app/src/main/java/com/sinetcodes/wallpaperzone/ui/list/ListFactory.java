package com.sinetcodes.wallpaperzone.ui.list;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.sinetcodes.wallpaperzone.data.repository.WallpaperRepository;


public class ListFactory extends ViewModelProvider.NewInstanceFactory {
    private WallpaperRepository repository;

    public ListFactory(WallpaperRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ListViewModel(repository);
    }
}
