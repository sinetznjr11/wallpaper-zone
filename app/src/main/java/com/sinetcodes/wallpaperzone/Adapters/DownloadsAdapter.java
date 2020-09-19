package com.sinetcodes.wallpaperzone.Adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.sinetcodes.wallpaperzone.pojo.PhotoFile;
import com.sinetcodes.wallpaperzone.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownloadsAdapter extends RecyclerView.Adapter<DownloadsAdapter.DownloadViewHolder> {

    List<PhotoFile> photoFileList;
    Context mContext;

    OnItemClickListener mOnItemClickListener;

    public DownloadsAdapter(List<PhotoFile> photoFileList, Context context,OnItemClickListener onItemClickListener) {
       this.photoFileList=photoFileList;
        mContext = context;
        mOnItemClickListener=onItemClickListener;
    }

    @NonNull
    @Override
    public DownloadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_explore_popular_item, parent, false);
        return new DownloadViewHolder(view,mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadViewHolder holder, int position) {


        Uri imageUri = Uri.fromFile(photoFileList.get(position).getPhotoFile());

        Glide.with(mContext).load(imageUri).into(holder.itemImage);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //Returns null, sizes are in the options variable
        BitmapFactory.decodeFile(photoFileList.get(position).getPhotoFile().getAbsolutePath(), options);
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
        return photoFileList.size();
    }



    public class DownloadViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnItemClickListener mOnItemClickListener;

        @BindView(R.id.single_image)
        RoundedImageView itemImage;

        @BindView(R.id.constraint_image)
        ConstraintLayout constraintLayout;

        public DownloadViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mOnItemClickListener=onItemClickListener;
            itemImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnItemClickListener.onItemClick(photoFileList.get(getAdapterPosition()));
        }
    }

    public interface OnItemClickListener{
        void onItemClick(PhotoFile photoFile);
    }
}
