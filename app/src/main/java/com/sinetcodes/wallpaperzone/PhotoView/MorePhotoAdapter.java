package com.sinetcodes.wallpaperzone.PhotoView;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sinetcodes.wallpaperzone.data.network.responses.Wallpaper;
import com.sinetcodes.wallpaperzone.databinding.SingleViewPhotoItemBinding;
import com.sinetcodes.wallpaperzone.pojo.Photos;
import com.sinetcodes.wallpaperzone.R;

import java.util.List;

public class MorePhotoAdapter extends RecyclerView.Adapter<MorePhotoAdapter.PhotoViewHolder> {
    Context mContext;
    List<Wallpaper> mWallpaperList;

    private static final String TAG = "MorePhotoAdapter";

    OnSurfaceClickListener mListener;

    public MorePhotoAdapter(Context context, OnSurfaceClickListener onSurfaceClickListener, List<Wallpaper> wallpaperList) {
        this.mContext = context;
        this.mWallpaperList = wallpaperList;
        this.mListener = onSurfaceClickListener;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SingleViewPhotoItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.single_view_photo_item, parent, false);
        return new PhotoViewHolder(binding, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        holder.mBinding.setWallpaper(mWallpaperList.get(position));
    }

    @Override
    public int getItemCount() {
        return mWallpaperList.size();
    }


    public class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnSurfaceClickListener mOnSurfaceClickListener;
        SingleViewPhotoItemBinding mBinding;

        public PhotoViewHolder(@NonNull SingleViewPhotoItemBinding binding, OnSurfaceClickListener onSurfaceClickListener) {
            super(binding.getRoot());
            mBinding = binding;
            this.mOnSurfaceClickListener = onSurfaceClickListener;
            mBinding.viewImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v != null) {
                mOnSurfaceClickListener.onSurfaceTouch(v, getAdapterPosition());
            }
        }
    }

    public interface OnSurfaceClickListener {
        void onSurfaceTouch(View view, int position);
    }
}
