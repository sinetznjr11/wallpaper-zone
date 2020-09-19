package com.sinetcodes.wallpaperzone.PhotoView;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.sinetcodes.wallpaperzone.pojo.Photos;
import com.sinetcodes.wallpaperzone.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MorePhotoAdapter extends RecyclerView.Adapter<MorePhotoAdapter.PhotoViewHolder> {
    Context mContext;
    List<Photos> mPhotos;

    private static final String TAG = "MorePhotoAdapter";

    OnPhotoClickedListener mListener;

    public MorePhotoAdapter(Context context, OnPhotoClickedListener onPhotoClickedListener, List<Photos> photos) {
        this.mContext = context;
        this.mPhotos = photos;
        this.mListener = onPhotoClickedListener;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_photo_item, parent, false);
        PhotoViewHolder viewHolder = new PhotoViewHolder(view, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        holder.photoView.setBackgroundColor(Color.parseColor(mPhotos.get(position).getColor()));
        Glide.with(mContext)
                .load(mPhotos.get(position).getUrls().getRegular())
                .thumbnail(
                        Glide.with(mContext)
                        .load(mPhotos.get(position).getUrls().getThumb())
                        .thumbnail(0.1f)
                )
                .into(holder.photoView);

    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public void addItem(List<Photos> photosList) {
        if (photosList != null) {
            mPhotos.addAll(photosList);
            notifyDataSetChanged();
        }
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.view_image)
        PhotoView photoView;
        OnPhotoClickedListener mOnPhotoClickedListener;

        public PhotoViewHolder(@NonNull View itemView, OnPhotoClickedListener onPhotoClickedListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mOnPhotoClickedListener = onPhotoClickedListener;
            photoView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v != null) {
                mOnPhotoClickedListener.onPhotoClicked(v, getAdapterPosition());
            }
        }
    }

    public interface OnPhotoClickedListener {
        void onPhotoClicked(View view, int position);
    }
}
