package com.sinetcodes.wallpaperzone.PhotoView;

import com.sinetcodes.wallpaperzone.POJO.Photos;

import java.util.List;

public interface UserPhotosMVCInterface {
    interface view{
        void setContent(List<Photos> photosList);
        void onError(String error);
    }
    interface presenter{
        void getContent(String userName);
        void takeContent(List<Photos> photosList);
    }
    interface model{
        void askContent(String userName);
    }
}
