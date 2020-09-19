package com.sinetcodes.wallpaperzone.Search;

import android.content.Context;
import android.util.Log;

import com.sinetcodes.wallpaperzone.pojo.Photos;
import com.sinetcodes.wallpaperzone.utils.StringsUtil;

import java.util.List;

public class SearchPresenter implements SearchMVPInterface.presenter{
    private SearchMVPInterface.view view;
    private SearchMVPInterface.model model;
    Context context;
    private static final String TAG = "SearchPresenter";

    public SearchPresenter(SearchMVPInterface.view homeView, Context context) {
        this.view = homeView;
        this.context = context;
        this.model = new SearchModel(this,context);
    }

    @Override
    public void getContent(String query,int collectionId, int page,String type) {
        Log.d(TAG, "getContent: "+type);
      if(view!=null){
          view.showProgress();
          if(type.equalsIgnoreCase(StringsUtil.RANDOM)) model.askRandomPhotos(query,page);
          else if(type.equalsIgnoreCase(StringsUtil.SEARCH)) model.askSearchResults(query, page);
          else if(type.equalsIgnoreCase(StringsUtil.COLLECTION)) model.askCollectionPhotos(collectionId,page);
      }
    }

    @Override
    public void takeTotalResults(int total) {
        view.setTotalResults(total);
    }

    @Override
    public void takeContent(List<Photos> photos) {
        if(view!=null){
            view.hideProgress();
            view.setContent(photos);
        }
    }
}
