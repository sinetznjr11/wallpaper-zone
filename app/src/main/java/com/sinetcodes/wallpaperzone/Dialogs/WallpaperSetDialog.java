package com.sinetcodes.wallpaperzone.Dialogs;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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

public class WallpaperSetDialog extends DialogFragment  {
    private static final String TAG = "WallpaperSetDialog";
    @BindView(R.id.please_wait_layout)
    RelativeLayout pleaseWaitLayout;
    @BindView(R.id.success_layout)
    RelativeLayout successLayout;
    @OnClick(R.id.btn_dialog_okay)
    void onBtnOkayClicked(){
        dismiss();
    }

    @OnClick(R.id.btn_dialog_rate_us)
    void onBtnRateUsClicked(){
        dismiss();
        final String appPackageName = getContext().getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dialog_wallpaper_set,container,false);
        ButterKnife.bind(this,view);
        //attrs
        setCancelable(false);
        showLoadingLayout();

        return view;
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
