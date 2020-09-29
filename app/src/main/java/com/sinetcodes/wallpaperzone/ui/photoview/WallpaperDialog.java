package com.sinetcodes.wallpaperzone.ui.photoview;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.sinetcodes.wallpaperzone.R;
import com.sinetcodes.wallpaperzone.databinding.DialogDownloadWallpaperBinding;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WallpaperDialog extends DialogFragment implements View.OnClickListener{

    DialogDownloadWallpaperBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_download_wallpaper, container, false);
        //attrs
        setCancelable(false);
        showLoadingLayout();
        mBinding.btnViewPhoto.setOnClickListener(this);
        mBinding.btnDialogDismiss.setOnClickListener(this);
        return mBinding.getRoot();
    }



    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null)
            return;
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void showLoadingLayout() {
        mBinding.pleaseWaitLayout.setVisibility(View.VISIBLE);
        mBinding.successLayout.setVisibility(View.GONE);
    }

    public void showSuccessLayout() {

        try {
            if (getView() != null) {
                mBinding.pleaseWaitLayout.setVisibility(View.GONE);
                mBinding.successLayout.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {
        if(v.getId()==mBinding.btnViewPhoto.getId()) openGallery();

        if(v.getId()==mBinding.btnDialogDismiss.getId()) dismiss();
    }

    void openGallery() {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
