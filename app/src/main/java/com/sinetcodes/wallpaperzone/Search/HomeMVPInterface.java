package com.sinetcodes.wallpaperzone.Search;

import com.sinetcodes.wallpaperzone.POJO.Photos;

import java.util.List;

public interface HomeMVPInterface {

    interface homeView{
        void setContent(List<Photos> photos);
        void onError(String error);
        void showProgress();
        void hideProgress();
    }

    interface homePresenter{
        void getContent(String query,int count);
        void takeContent(List<Photos> photos);
    }

    interface  homeModel{
        void askContent(String query, int count);
    }


}
