package com.sinetcodes.wallpaperzone.Home;

import android.content.Context;

import com.sinetcodes.wallpaperzone.Common.ContentType;

import java.util.List;

public class HomePresenter implements HomeMVPInterface.presenter {
    Context context;
    HomeMVPInterface.view view;
    HomeMVPInterface.model model;

    public HomePresenter(Context context, HomeMVPInterface.view view) {
        this.context = context;
        this.view = view;
        this.model = new HomeModel(context, this);
    }

    @Override
    public void getContent(String contentType, int page) {
        if (view != null) {
            view.showProgress();
            switch (contentType) {
                case ContentType.CATEGORY:
                    model.askCategory();
                    break;
                case ContentType.POPULAR:
                    model.askPopular(page);
                    break;
                case ContentType.COLLECTIONS:
                    model.askCollections(page);
                    break;
            }
        }
    }

    @Override
    public void takeContent(List<Object> items, String contentType) {
        if (view != null) {
            view.hideProgress();
            view.setContent(items, contentType);
        }
    }

    @Override
    public void onError(String error) {
        view.onError(error);
    }
}
