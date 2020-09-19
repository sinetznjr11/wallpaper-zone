package com.sinetcodes.wallpaperzone.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sinetcodes.wallpaperzone.R;

public class BindingAdapter {
    @androidx.databinding.BindingAdapter("android:src")
    public static void setImageUri(ImageView view, String imageUri) {
        try {
            Glide
                    .with(view.getContext())
                    .load(imageUri)
                    .placeholder(R.drawable.placeholder)
                    .thumbnail(0.1f)
                    .into(view);
        } catch (Exception ignored) {
        }
    }
}
