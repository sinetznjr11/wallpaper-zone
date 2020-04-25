package com.sinetcodes.wallpaperzone.Search;

import android.content.Context;

import com.sinetcodes.wallpaperzone.POJO.Photos;

import java.util.List;

public class HomePresenter implements HomeMVPInterface.homePresenter{
    private HomeMVPInterface.homeView homeView;
    private HomeMVPInterface.homeModel homeModel;
    Context context;

    public HomePresenter(HomeMVPInterface.homeView homeView, Context context) {
        this.homeView = homeView;
        this.context = context;
        this.homeModel = new HomeModel(this,context);
    }

    @Override
    public void getContent(String query, int page) {
      if(homeView!=null){
          homeView.showProgress();
          homeModel.askContent(query,page);
      }
    }

    @Override
    public void takeContent(List<Photos> photos) {
        if(homeView!=null){
            homeView.hideProgress();
            homeView.setContent(photos);
        }
    }
}
