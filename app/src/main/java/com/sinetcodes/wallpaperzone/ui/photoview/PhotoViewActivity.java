package com.sinetcodes.wallpaperzone.ui.photoview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.sinetcodes.wallpaperzone.Activities.MainActivity;
import com.sinetcodes.wallpaperzone.Dialogs.WallpaperSetDialog;
import com.sinetcodes.wallpaperzone.PhotoView.MorePhotoAdapter;
import com.sinetcodes.wallpaperzone.data.network.responses.Wallpaper;
import com.sinetcodes.wallpaperzone.databinding.ActivityPhotoViewBinding;
import com.sinetcodes.wallpaperzone.utils.AppUtil;
import com.sinetcodes.wallpaperzone.utils.StringsUtil;


import java.util.ArrayList;
import java.util.List;


public class
PhotoViewActivity extends AppCompatActivity
        implements
        MorePhotoAdapter.OnSurfaceClickListener,
        View.OnClickListener {

    private static final String TAG = "PhotoViewActivity";
    public static final int PERMISSION_WRITE_STORAGE = 111;


    MorePhotoAdapter mAdapter;
    List<Wallpaper> wallpaperList = new ArrayList<>();
    ViewPager2.OnPageChangeCallback mCallback;

    WallpaperDialog downloadDialog;
    WallpaperSetDialog dialog = new WallpaperSetDialog();


    boolean isShowingControls = true;
    int currentPosition = 0;
    boolean mIsInFavorites = false;


    ActivityPhotoViewBinding mBinding;
    PhotoViewViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityPhotoViewBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        AppUtil.setTransparentStatusBar(this);

        mViewModel = new ViewModelProvider(this).get(PhotoViewViewModel.class);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            wallpaperList = (List<Wallpaper>) intent.getSerializableExtra("wallpaperList");
            currentPosition = intent.getIntExtra("position", 0);
            Log.e(TAG, "onCreate: " + currentPosition);
            mBinding.setWallpaper(wallpaperList.get(currentPosition));

            mViewModel.setWallpaperMutableLiveData(wallpaperList.get(currentPosition));

        }

        mBinding.setViewModel(mViewModel);

        mAdapter = new MorePhotoAdapter(this, this, wallpaperList);
        mBinding.imageViewPager.post(() -> mBinding.imageViewPager.setCurrentItem(currentPosition, false));

        mBinding.imageViewPager.setPageTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });

        mCallback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                MainActivity.photosInterstitialAdCounter++;
                Wallpaper wallpaper = wallpaperList.get(position);
                mBinding.setWallpaper(wallpaper);
                mViewModel.setWallpaperMutableLiveData(wallpaper);
            }
        };
        mBinding.imageViewPager.registerOnPageChangeCallback(mCallback);
        mBinding.imageViewPager.setAdapter(mAdapter);

        mBinding.btnDownload.setOnClickListener(this);
        mBinding.btnSetWallpaper.setOnClickListener(this);

        observeViewModel();
    }

    private void observeViewModel() {
        mViewModel.getDownloadListener().observe(this, state -> {

            switch (state) {
                case StringsUtil.STARTED:
                    downloadDialog = new WallpaperDialog();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    downloadDialog.show(transaction, "download_dialog");
                    break;
                case StringsUtil.COMPLETED:
                    if (downloadDialog.getView() != null) downloadDialog.showSuccessLayout();
                    break;
                case StringsUtil.DISMISS:
                    if (downloadDialog.getView() != null) downloadDialog.dismiss();
                    break;
            }
        });
    }



   /* @OnClick(R.id.btn_set)
    void onBtnSetClicked() {
        checkPermissionAndProceed(StringsUtil.WALLPAPER_SET);
    }

    @OnClick(R.id.btn_back)
    void onBtnBackClicked() {
        onBackPressed();
    }

    @OnClick(R.id.btn_fav)
    void onBtnFavClicked() {
        presenter.addOrRemoveFromFavorites(mIsInFavorites, mPhotoList.get(getCurrentPosition()));
    }

    @OnClick(R.id.btn_download)
    void onBtnDownloadClicked() {
        checkPermissionAndProceed(StringsUtil.WALLPAPER_DOWNLOAD);
    }

   */

    @Override
    public void onSurfaceTouch(View view, int position) {
        Log.d(TAG, "onPhotoClicked: ");
        if (isShowingControls) {
            hideLayout();
            isShowingControls = false;
        } else {
            showLayout();
            isShowingControls = true;
        }
    }

    @Override
    protected void onDestroy() {
        mBinding.imageViewPager.unregisterOnPageChangeCallback(mCallback);
        mBinding = null;
        super.onDestroy();
    }



/*
    @Override
    public void setContent(List<Photos> photosList) {
        mAdapter.addItem(photosList);
    }

    @Override
    public void onError(String error) {
        Log.e(TAG, "onError: " + error);
    }

    @Override
    public void onDownloadStarted() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        downloadDialog.show(transaction, "download_dialog");
    }

    @Override
    public void onDownloadDialogDismiss() {
        downloadDialog.dismiss();
    }

    @Override
    public void onDownloadCompleted() {
        if (downloadDialog.getView() != null) {
            downloadDialog.showSuccessLayout();
        }
    }

    @Override
    public void setIsInFavorites(boolean isInFavorites) {
        mIsInFavorites = isInFavorites;
        if (isInFavorites) {
            mBinding.btnFav.setImageResource(R.drawable.ic_heart);
            mBinding.btnFav.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_red_dark), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            mBinding.btnFav.setImageResource(R.drawable.ic_heart_stroke);
            mBinding.btnFav.setColorFilter(ContextCompat.getColor(this, android.R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }


    @Override
    public void onComplete(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
*/


    private void showLayout() {
        mBinding.topControls.animate()
                .alpha(1.0f)
                .setDuration(200);
        mBinding.bottomControls.animate()
                .translationY(1)
                .alpha(1.0f)
                .setDuration(200);
    }

    private void hideLayout() {
        mBinding.topControls.animate()
                .alpha(0.0f)
                .setDuration(200);
        mBinding.bottomControls.animate()
                .translationY(0)
                .alpha(0.0f)
                .setDuration(200);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == mBinding.btnSetWallpaper.getId())
            checkPermissionAndProceed(StringsUtil.WALLPAPER_SET);
        if (v.getId() == mBinding.btnDownload.getId())
            checkPermissionAndProceed(StringsUtil.WALLPAPER_DOWNLOAD);
    }

    private void checkPermissionAndProceed(String operation) {
        //check for permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                //denied, request permission
                Log.e(TAG, "denied, request permission");
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this, permissions, PERMISSION_WRITE_STORAGE);
            } else {
                //already granted
                Log.e(TAG, "onLoadClicked: permission already granted");
                mViewModel.downloadFile(this, mViewModel.getWallpaperMutableLiveData().getValue(), operation);

            }
        } else {
            Log.e(TAG, "onLoadClicked: check not required");
            mViewModel.downloadFile(this, mViewModel.getWallpaperMutableLiveData().getValue(), operation);
        }
    }
}
