package com.sinetcodes.wallpaperzone.PhotoView;

import com.sinetcodes.wallpaperzone.POJO.Photos;

import java.util.List;

public interface UserPhotosMVPInterface {
    interface view{
        void setContent(List<Photos> photosList);
        void onError(String error);
        void onDownloadStarted();
        void onDownloadDialogDismiss();
        void onDownloadCompleted();
        void setIsInFavorites(boolean isInFavorites);
        void showToast(String message);
    }
    interface presenter{
        void getContent(String userName);
        void takeContent(List<Photos> photosList);
        void checkIsPhotoInFavorites(String photoId);
        void isPhotoInFavoritesResponse(boolean isInFavorites);
        void showToast(String message);

    }
    interface model{
        void askContent(String userName);
        void isPhotoInFavorites(String photoId);
    }
}
