package com.sinetcodes.wallpaperzone.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;


public class BindingAdapter {
    @androidx.databinding.BindingAdapter({"android:src","app:thumbnail"})
    public static void setImageUri(ImageView view, String imageUri,String thumbnailUri) {
        try {
            Glide.with(view.getContext())
                    .load(imageUri)
                    .thumbnail(
                            Glide.with(view.getContext())
                                    .load(thumbnailUri)
                                    .thumbnail(0.1f)
                    )
                    .into(view);
        } catch (Exception ignored) {
        }
    }
}
