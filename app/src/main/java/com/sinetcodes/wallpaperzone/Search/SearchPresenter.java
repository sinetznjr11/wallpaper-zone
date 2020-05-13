package com.sinetcodes.wallpaperzone.Search;

import android.content.Context;

import androidx.navigation.fragment.NavHostFragment;

import com.sinetcodes.wallpaperzone.POJO.Photos;
import com.sinetcodes.wallpaperzone.Utilities.StringsUtil;

import java.util.List;

public class SearchPresenter implements SearchMVPInterface.presenter{
    private SearchMVPInterface.view view;
    private SearchMVPInterface.model model;
    Context context;

    public SearchPresenter(SearchMVPInterface.view homeView, Context context) {
        this.view = homeView;
        this.context = context;
        this.model = new SearchModel(this,context);


    }

    @Override
    public void getContent(String query,int collectionId, int page,String type) {
      if(view!=null){
          view.showProgress();
          if(type.equalsIgnoreCase(StringsUtil.RANDOM)) model.askRandomPhotos(query,page);
          else if(type.equalsIgnoreCase(StringsUtil.SEARCH)) model.askSearchResults(query, page);
          else if(type.equalsIgnoreCase(StringsUtil.COLLECTION)) model.askCollectionPhotos(collectionId,page);
      }
    }

    @Override
    public void takeContent(List<Photos> photos) {
        if(view!=null){
            view.hideProgress();
            view.setContent(photos);
        }
    }
}
