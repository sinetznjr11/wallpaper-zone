package com.sinetcodes.wallpaperzone.PhotoView;

import android.content.Context;

import com.sinetcodes.wallpaperzone.POJO.Photos;

import java.util.List;

public class MorePhotoPresenter implements UserPhotosMVCInterface.presenter{
    Context mContext;
    UserPhotosMVCInterface.view mView;
    MorePhotoModel mModel;

    public MorePhotoPresenter(Context context, UserPhotosMVCInterface.view view) {
        mContext = context;
        mView = view;
        mModel=new MorePhotoModel(context,this);
    }

    @Override
    public void getContent(String userName) {
        if(mView!=null)
            mModel.askContent(userName);
    }

    @Override
    public void takeContent(List<Photos> photosList) {
        if(mView!=null)
            mView.setContent(photosList);
    }
}
