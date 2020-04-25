package com.sinetcodes.wallpaperzone.PhotoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.makeramen.roundedimageview.RoundedImageView;
import com.sinetcodes.wallpaperzone.Activities.MainActivity;
import com.sinetcodes.wallpaperzone.Dialogs.WallpaperSetDialog;
import com.sinetcodes.wallpaperzone.POJO.Photos;
import com.sinetcodes.wallpaperzone.R;
import com.sinetcodes.wallpaperzone.Utilities.AppUtil;
import com.sinetcodes.wallpaperzone.Utilities.Favorites;
import com.sinetcodes.wallpaperzone.Utilities.SetWallpaperTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PhotoViewActivity extends AppCompatActivity
        implements MorePhotoAdapter.OnPhotoClickedListener,
        UserPhotosMVCInterface.view,
            Favorites.OnAddFavoriteListener,
         SetWallpaperTask.OnWallpaperSetListener{

    //todo populate list of related photos
    private static final String TAG = "PhotoViewActivity";

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

    @OnClick(R.id.btn_back)
    void onBtnBackClicked() {
        onBackPressed();
    }

    @OnClick(R.id.btn_fav)
    void onBtnFavClicked(){
        Favorites favorites =new Favorites(this,mPhotoList.get(currentPosition));
        favorites.addToFavorites();
        favorites.addOnCompleteListener(this);
    }

    @BindView(R.id.btn_download)
    MaterialButton btnDownload;
    @BindView(R.id.btn_set)
    MaterialButton btnSet;
    WallpaperSetDialog dialog=new WallpaperSetDialog();
    @OnClick(R.id.btn_set)
    void onBtnSetClicked(){

        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();

        dialog.show(transaction,"wallpaper_set_dialog");
        //dialog.showLoadingLayout();
        SetWallpaperTask task=new SetWallpaperTask(this,this);
        task.execute(mPhotoList.get(currentPosition).getUrls().getRegular());

    }


    @BindView(R.id.single_image_recycler_view)
    ViewPager2 mViewPager;
    MorePhotoAdapter mAdapter;

    boolean isShowingControls = true;
    ViewPager2.OnPageChangeCallback mCallback;

    List<Photos> mPhotoList = new ArrayList<>();

    int currentPosition=0;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        AppUtil.setTransparentStatusBar(this);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Photos photoItem = (Photos) intent.getSerializableExtra("photoItem");
        mPhotoList.add(photoItem);
        MorePhotoPresenter presenter = new MorePhotoPresenter(this, this);
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

                MainActivity.photosInterstitialAdCounter++;
                Log.d(TAG, "onPageSelected: user=> " + mPhotoList.get(position).getUser().getName() + " __" + mPhotoList.get(position).getDescription());

                currentPosition=position;
                userNameTV.setText(mPhotoList.get(position).getUser().getName());

                //description
                if (mPhotoList.get(position).getDescription() != null) {
                    if (mPhotoList.get(position).getDescription().equals("")) {
                        Log.d(TAG, "onPageSelected: ya xiryo1");
                        descriptionTV.setText(mPhotoList.get(position).getUser().getName() + " Collections");
                    } else {
                        Log.d(TAG, "onPageSelected: ya xiryo2");
                        descriptionTV.setText(mPhotoList.get(position).getDescription());
                    }
                } else {
                    Log.d(TAG, "onPageSelected: ya xiryo3");
                    descriptionTV.setText(mPhotoList.get(position).getUser().getName() + " Collections");
                }
                //alternate description
                if (mPhotoList.get(position).getAlt_description() != null)
                    subDescriptionTV.setText(mPhotoList.get(position).getAlt_description());
                else
                    subDescriptionTV.setText(mPhotoList.get(position).getUser().getName() + " Collections");


                Picasso.get()
                        .load(mPhotoList.get(position).getUser().getProfile_image().getSmall())
                        .placeholder(R.drawable.user_placeholder)
                        .into(userPhoto);
            }
        };
        mViewPager.registerOnPageChangeCallback(mCallback);
        mViewPager.setAdapter(mAdapter);


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
        Log.e(TAG, "onError: "+error);
    }

    @Override
    public void onComplete(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCompletedSetting() {
        dialog.showSuccessLayout();
    }
}
