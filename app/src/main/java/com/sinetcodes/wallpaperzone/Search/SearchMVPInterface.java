package com.sinetcodes.wallpaperzone.Search;

import com.sinetcodes.wallpaperzone.pojo.Photos;

import java.util.List;

public interface SearchMVPInterface {

    interface view {
        void setContent(List<Photos> photos);

        void setTotalResults(int total);

        void onError(String error);

        void showProgress();

        void hideProgress();
    }

    interface presenter {
        void getContent(String query, int collectionId, int count, String type);
        void takeTotalResults(int total);

        void takeContent(List<Photos> photos);
    }

    interface model {
        void askRandomPhotos(String query, int count);

        void askSearchResults(String query, int page);

        void askCollectionPhotos(int collectionId, int page);
    }


}
