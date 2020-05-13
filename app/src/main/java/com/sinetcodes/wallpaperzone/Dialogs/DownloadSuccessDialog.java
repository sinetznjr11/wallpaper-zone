package com.sinetcodes.wallpaperzone.Dialogs;

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
import androidx.fragment.app.DialogFragment;

import com.sinetcodes.wallpaperzone.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DownloadSuccessDialog extends DialogFragment {
    @BindView(R.id.please_wait_layout) RelativeLayout pleaseWaitLayout;
    @BindView(R.id.success_layout) RelativeLayout successLayout;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dialog_download_wallpaper,container,false);
        ButterKnife.bind(this,view);
        //attrs
        setCancelable(false);
        showLoadingLayout();

        return view;
    }
    @OnClick(R.id.btn_view_photo) void onViewPhotoClicked(){
        //todo open gallery
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    @OnClick(R.id.btn_dialog_dismiss) void onDismissDialogClicked(){
        dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getDialog()==null)
            return;
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void showLoadingLayout(){
        pleaseWaitLayout.setVisibility(View.VISIBLE);
        successLayout.setVisibility(View.GONE);
    }

    public void showSuccessLayout(){

        try {
            if(getView()!=null){
                pleaseWaitLayout.setVisibility(View.GONE);
                successLayout.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
