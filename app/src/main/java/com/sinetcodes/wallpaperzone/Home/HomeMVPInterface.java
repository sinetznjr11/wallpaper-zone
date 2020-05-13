package com.sinetcodes.wallpaperzone.Home;

import java.util.List;

public interface HomeMVPInterface {
    interface view{
        void setContent(List<Object> items, String contentType);
        void onError(String error);
        void showProgress();
        void hideProgress();
    }

    interface presenter{
        void getContent(String contentType,int page);
        void takeContent(List<Object> items,String contentType);
        void onError(String error);
    }

    interface  model{
        void askCategory();
        void askCollections(int page);
        void askPopular(int page);
    }

}
