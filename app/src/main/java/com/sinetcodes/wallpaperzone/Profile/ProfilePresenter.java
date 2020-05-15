package com.sinetcodes.wallpaperzone.Profile;

import android.content.Context;

import com.sinetcodes.wallpaperzone.POJO.PhotoFile;
import com.sinetcodes.wallpaperzone.POJO.Photos;

import java.io.File;
import java.util.List;

public class ProfilePresenter implements ProfileImpl.presenter{
    Context mContext;
    ProfileImpl.model mModel;
    ProfileImpl.view mView;

    public ProfilePresenter(Context context, ProfileImpl.view view) {
        mContext = context;
        mModel=new ProfileModel(context,this);
        mView=view;
    }

    @Override
    public void getDownloads() {
        if(mView!=null){
            mView.showProgress();
            mModel.askDownloads();
        }
    }
    @Override
    public void takeDownloads( List<PhotoFile> photoFileList) {
        if(mView!=null){
            mView.hideProgress();
            mView.setDownloads(photoFileList);
        }
    }
    @Override
    public void getPhotoFromId(String photoId) {
        if(mView != null)
            mView.showProgress();
            mModel.askPhotoFromId(photoId);
    }



    @Override
    public void takePhotoFromId(Photos photo) {
        if(mView!=null)
            mView.hideProgress();
            mView.setPhotoFromId(photo);
    }

    @Override
    public void onError(String err) {
        mView.showToast(err);
    }
}
