package com.sinetcodes.wallpaperzone.Adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.sinetcodes.wallpaperzone.R;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownloadsAdapter extends RecyclerView.Adapter<DownloadsAdapter.DownloadViewHolder> {

    File[] mFiles;
    Context mContext;

    public DownloadsAdapter(File[] files, Context context) {
        mFiles = files;
        mContext = context;
    }

    @NonNull
    @Override
    public DownloadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_explore_popular_item, parent, false);
        DownloadViewHolder viewHolder = new DownloadViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadViewHolder holder, int position) {

        Uri imageUri = Uri.fromFile(mFiles[position]);

        Glide.with(mContext).load(imageUri).into(holder.itemImage);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //Returns null, sizes are in the options variable
        BitmapFactory.decodeFile(mFiles[position].getAbsolutePath(), options);
        int width = options.outWidth;
        int height = options.outHeight;

        String ratio = width + ":" + height;
        ConstraintSet set = new ConstraintSet();
        set.clone(holder.constraintLayout);
        set.setDimensionRatio(holder.itemImage.getId(), ratio);
        set.applyTo(holder.constraintLayout);
    }

    @Override
    public int getItemCount() {
        return mFiles.length;
    }

    public class DownloadViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.single_image)
        RoundedImageView itemImage;

        @BindView(R.id.constraint_image)
        ConstraintLayout constraintLayout;

        public DownloadViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
