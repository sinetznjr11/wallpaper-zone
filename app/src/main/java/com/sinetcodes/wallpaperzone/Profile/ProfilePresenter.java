package com.sinetcodes.wallpaperzone.Profile;

import android.content.Context;

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
            mModel.askDownloads();
        }
    }

    @Override
    public void takeDownloads(File[] fileList) {
        if(mView!=null){
            mView.setDownloads(fileList);
        }
    }
}
