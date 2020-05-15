package com.sinetcodes.wallpaperzone.PhotoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.makeramen.roundedimageview.RoundedImageView;
import com.sinetcodes.wallpaperzone.Activities.MainActivity;
import com.sinetcodes.wallpaperzone.Dialogs.DownloadSuccessDialog;
import com.sinetcodes.wallpaperzone.Dialogs.WallpaperSetDialog;
import com.sinetcodes.wallpaperzone.POJO.Photos;
import com.sinetcodes.wallpaperzone.R;
import com.sinetcodes.wallpaperzone.Utilities.AppUtil;
import com.sinetcodes.wallpaperzone.Utilities.Favorites;
import com.sinetcodes.wallpaperzone.Utilities.FirebaseEventManager;
import com.sinetcodes.wallpaperzone.Utilities.StringsUtil;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class
PhotoViewActivity
        extends
        AppCompatActivity
        implements
        MorePhotoAdapter.OnPhotoClickedListener,
        UserPhotosMVPInterface.view,
        Favorites.OnAddFavoriteListener
        {

    private static final String TAG = "PhotoViewActivity";
    public static final int PERMISSION_WRITE_STORAGE = 111;

    @BindView(R.id.top_controls)
    View topControls;
    @BindView(R.id.bottom_controls)
    View bottomControls;
    @BindView(R.id.user_name)
    TextView userNameTV;
    @BindView(R.id.description)
    TextView descriptionTV;
    @BindView(R.id.sub_description)
    TextView subDescriptionTV;
    @BindView(R.id.user_photo)
    RoundedImageView userPhoto;
    @BindView(R.id.single_image_recycler_view)
    ViewPager2 mViewPager;
    @BindView(R.id.btn_fav)
    ImageButton btnFav;


    MorePhotoAdapter mAdapter;
    List<Photos> mPhotoList = new ArrayList<>();
    ViewPager2.OnPageChangeCallback mCallback;
    PhotoViewPresenter presenter;

    WallpaperSetDialog dialog = new WallpaperSetDialog();
    DownloadSuccessDialog downloadDialog = new DownloadSuccessDialog();


    boolean isShowingControls = true;
    int currentPosition = 0;
    boolean mIsInFavorites = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        AppUtil.setTransparentStatusBar(this);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Photos photoItem = (Photos) intent.getSerializableExtra("photoItem");
        mPhotoList.add(photoItem);
        presenter = new PhotoViewPresenter(this, this);
        presenter.getContent(photoItem.getUser().getUsername());


        mAdapter = new MorePhotoAdapter(this, this, mPhotoList);
        mViewPager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        mCallback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.d(TAG, "onPageSelected: url => " + mPhotoList.get(position).getUrls().getThumb());

                setCurrentPosition(position);
                MainActivity.photosInterstitialAdCounter++;

                presenter.checkIsPhotoInFavorites(mPhotoList.get(position).getId());
                userNameTV.setText(mPhotoList.get(position).getUser().getName());

                //description
                if (mPhotoList.get(position).getDescription() != null) {
                    if (mPhotoList.get(position).getDescription().equals(""))
                        descriptionTV.setText(mPhotoList.get(position).getUser().getName() + " Collections");
                    else
                        descriptionTV.setText(mPhotoList.get(position).getDescription());
                } else
                    descriptionTV.setText(mPhotoList.get(position).getUser().getName() + " Collections");

                //alternate description
                if (mPhotoList.get(position).getAlt_description() != null)
                    subDescriptionTV.setText(mPhotoList.get(position).getAlt_description());
                else
                    subDescriptionTV.setText(mPhotoList.get(position).getUser().getName() + " Collections");

                Glide.with(PhotoViewActivity.this)
                        .load(mPhotoList.get(position).getUser().getProfile_image().getMedium())
                        .thumbnail(
                                Glide.with(PhotoViewActivity.this)
                                        .load(mPhotoList.get(position).getUser().getProfile_image().getSmall())
                                        .thumbnail(0.1f)
                        )
                        .into(userPhoto);

            }
        };
        mViewPager.registerOnPageChangeCallback(mCallback);
        mViewPager.setAdapter(mAdapter);
    }

    @OnClick(R.id.btn_share)
    void onBtnShareClicked() {

        Glide.with(PhotoViewActivity.this)
                .asBitmap()
                .load(mPhotoList.get(getCurrentPosition()).getUrls().getSmall())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        new FirebaseEventManager(PhotoViewActivity.this).shareEvent(mPhotoList.get(getCurrentPosition()).getUrls().getSmall(), AppUtil.getDeviceId(PhotoViewActivity.this));
                        i.setType("*/*");
                        i.putExtra(Intent.EXTRA_TEXT, "Hey check this amazing wallpaper from Wallpaper Zone app. You can download the app here, https://play.google.com/store/apps/details?id=" + getPackageName());
                        i.putExtra(Intent.EXTRA_STREAM, AppUtil.getLocalBitmapUri(resource, PhotoViewActivity.this));
                        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(i, "Share Image"));
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btn_set)
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
                presenter.downloadFile(mPhotoList.get(getCurrentPosition()), operation);
                //downloadFile();

            }
        } else {
            Log.e(TAG, "onLoadClicked: check not required");
            presenter.downloadFile(mPhotoList.get(getCurrentPosition()), operation);
            //downloadFile();
        }
    }

    @Override
    public void onPhotoClicked(View view, int position) {
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
        mViewPager.unregisterOnPageChangeCallback(mCallback);
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

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
            btnFav.setImageResource(R.drawable.ic_heart);
            btnFav.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_red_dark), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            btnFav.setImageResource(R.drawable.ic_heart_stroke);
            btnFav.setColorFilter(ContextCompat.getColor(this, android.R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }


    @Override
    public void onComplete(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }



    private void showLayout() {
        topControls.animate()
                .alpha(1.0f)
                .setDuration(200);
        bottomControls.animate()
                .translationY(1)
                .alpha(1.0f)
                .setDuration(200);
    }

    private void hideLayout() {
        topControls.animate()
                .alpha(0.0f)
                .setDuration(200);
        bottomControls.animate()
                .translationY(0)
                .alpha(0.0f)
                .setDuration(200);
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }
}
