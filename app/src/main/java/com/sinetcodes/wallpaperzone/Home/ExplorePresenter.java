package com.sinetcodes.wallpaperzone.Home;

import android.content.Context;

import com.sinetcodes.wallpaperzone.Common.ContentType;

import java.util.List;

public class ExplorePresenter implements ExploreMVPInterface.presenter {
    Context context;
    ExploreMVPInterface.view view;
    ExploreMVPInterface.model model;

    public ExplorePresenter(Context context, ExploreMVPInterface.view view) {
        this.context = context;
        this.view = view;
        this.model = new ExploreModel(context, this);
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
