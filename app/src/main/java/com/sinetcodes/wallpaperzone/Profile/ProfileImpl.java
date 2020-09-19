package com.sinetcodes.wallpaperzone.Profile;

import com.sinetcodes.wallpaperzone.pojo.PhotoFile;
import com.sinetcodes.wallpaperzone.pojo.Photos;

import java.util.List;

public interface ProfileImpl {
    interface view{
        void setDownloads( List<PhotoFile> photoFileList);
        void setPhotoFromId(Photos photo);

        void showProgress();
        void hideProgress();
        void showToast(String message);
    }

    interface presenter{
        void getDownloads();
        void getPhotoFromId(String photoId);
        void takeDownloads( List<PhotoFile> photoFileList);
        void takePhotoFromId(Photos photo);
        void onError(String err);
    }

    interface model{
        void askDownloads();
        void askPhotoFromId(String photoId);
    }
}
