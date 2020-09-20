package com.sinetcodes.wallpaperzone.Adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;
import com.sinetcodes.wallpaperzone.data.network.responses.Wallpaper;
import com.sinetcodes.wallpaperzone.databinding.SinglePhotoListItemBinding;
import com.sinetcodes.wallpaperzone.pojo.Collection;
import com.sinetcodes.wallpaperzone.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    Context mContext;
    OnItemClickListener mListener;
    List<Wallpaper> mWallpaperList;

    public ListAdapter(Context context, OnItemClickListener listener, List<Wallpaper> wallpaperList) {
        mContext = context;
        mListener = listener;
        mWallpaperList = wallpaperList;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SinglePhotoListItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.single_photo_list_item, parent, false);
        return new ListViewHolder(binding, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.mBinding.setWallpaper(mWallpaperList.get(position));

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        //setting dynamic height for staggered view.
        String width = mWallpaperList.get(position).getWidth();
        String height = String.valueOf(Integer.parseInt(mWallpaperList.get(position).getHeight()) * 2);
        String ratio = width + ":" + height;
        ConstraintSet set = new ConstraintSet();
        set.clone(holder.mBinding.constraintImage);
        set.setDimensionRatio(holder.mBinding.singleImage.getId(), ratio);
        set.applyTo(holder.mBinding.constraintImage);
    }

    @Override
    public int getItemCount() {
        return mWallpaperList.size();
    }

    public void addItems(List<Wallpaper> items) {
        mWallpaperList.addAll(items);
        notifyItemInserted(mWallpaperList.size()-1);
    }

    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnItemClickListener mOnItemClickListener;
        SinglePhotoListItemBinding mBinding;


        public ListViewHolder(SinglePhotoListItemBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            mBinding = binding;
            mOnItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnItemClickListener.onItemClicked(mWallpaperList.get(getAdapterPosition()));
        }
    }


    public interface OnItemClickListener {
        void onItemClicked(Wallpaper wallpaper);
    }
}
