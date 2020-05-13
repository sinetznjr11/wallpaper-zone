package com.sinetcodes.wallpaperzone.Home;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.sinetcodes.wallpaperzone.Common.ContentType;
import com.sinetcodes.wallpaperzone.POJO.CategoryItem;
import com.sinetcodes.wallpaperzone.POJO.Collection;
import com.sinetcodes.wallpaperzone.POJO.Photos;
import com.sinetcodes.wallpaperzone.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeHorizontalAdapter extends RecyclerView.Adapter<HomeHorizontalAdapter.ExploreHorizontalVH> {
    Context context;
    List<Object> list = new ArrayList<>();
    String type;
    int parentPosition=0;

    private OnChildItemClickedListener mListener;
    private static final String TAG = "HorizontalAdapter";

    public HomeHorizontalAdapter(Context context, OnChildItemClickedListener onChildItemClickedListener, int parentPosition, List<Object> list, String type) {
        this.context = context;
        this.list = list;
        this.type = type;
        this.mListener = onChildItemClickedListener;
        this.parentPosition = parentPosition;
    }


    @Override
    public ExploreHorizontalVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = chooseDynamicLayout(parent);
        final HomeHorizontalAdapter.ExploreHorizontalVH viewHolder = new HomeHorizontalAdapter.ExploreHorizontalVH(v, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExploreHorizontalVH holder, int position) {
        switch (type) {
            case ContentType.CATEGORY:
                List<CategoryItem> categoryItems = (List<CategoryItem>) (List<?>) list;
                holder.itemName.setText(categoryItems.get(position).getName());
                Picasso.get().load(categoryItems.get(position).getImage_url()).into(holder.itemImage);
                break;

            case ContentType.COLLECTIONS:
                List<Collection> collectionList = (List<Collection>) (List<?>) list;
                holder.itemImage.setBackgroundColor(Color.parseColor(collectionList.get(position).getCoverPhoto().getColor()));

                holder.itemName.setText(collectionList.get(position).getTitle());
                Picasso.get()
                        .load(collectionList.get(position).getCoverPhoto().getUrls().getThumb())
                        .placeholder(R.drawable.placeholder)
                        .into(holder.itemImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                Picasso.get()
                                        .load(collectionList.get(position).getCoverPhoto().getUrls().getSmall())
                                        .into(holder.itemImage);
                            }

                            @Override
                            public void onError(Exception e) {
                            }
                        });
                holder.photoUserTV.setText(collectionList.get(position).getUser().getName());
                holder.totalPhotosTV.setText(collectionList.get(position).getTotalPhotos() + " photos");
                break;

            case ContentType.POPULAR:
                List<Photos> photosList = (List<Photos>) (List<?>) list;
                holder.itemImage.setBackgroundColor(Color.parseColor(photosList.get(position).getColor()));
                Picasso.get().load(photosList.get(position).getUrls().getThumb()).into(holder.itemImage);

                String ratio = photosList.get(position).getWidth() + ":" + photosList.get(position).getHeight();
                ConstraintSet set = new ConstraintSet();
                set.clone(holder.constraintLayout);
                set.setDimensionRatio(holder.itemImage.getId(), ratio);
                set.applyTo(holder.constraintLayout);

                break;


        }
    }


    public void addPopularContent(List<Object> photos) {
        if (type == ContentType.POPULAR)
            list.addAll(photos);
            notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        /*
        todo dynamic size
         */
        return list.size();
    }

   public Photos getPhotoItem(int position){
        return (Photos) list.get(position);
   }

    public class ExploreHorizontalVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.single_name)
        TextView itemName;
        @BindView(R.id.single_image)
        RoundedImageView itemImage;

        @Nullable
        @BindView(R.id.constraint_image)
        ConstraintLayout constraintLayout;

        @Nullable
        @BindView(R.id.total_photos)
        TextView totalPhotosTV;

        @Nullable
        @BindView(R.id.photo_user)
        TextView photoUserTV;

        @Nullable
        @BindView(R.id.item_view)
        FrameLayout item_view;


        //public View container;
        OnChildItemClickedListener mOnChildItemClickedListener;

        public ExploreHorizontalVH(@NonNull View itemView, OnChildItemClickedListener onChildItemClickedListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mOnChildItemClickedListener = onChildItemClickedListener;
            itemImage.setOnClickListener(this);
            item_view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mListener.onChildItemClicked(v, parentPosition, getAdapterPosition());
        }
    }

    public View chooseDynamicLayout(ViewGroup parent) {
        View view = null;
        switch (type) {
            case ContentType.CATEGORY:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_explore_category_item, null);
                break;
            case ContentType.COLLECTIONS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_explore_collections_item, null);
                break;
            case ContentType.POPULAR:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_explore_popular_item, null);
                break;

        }

        return view;
    }

    public interface OnChildItemClickedListener {
        public void onChildItemClicked(View view, int parentPosition, int childPosition);
    }


}
