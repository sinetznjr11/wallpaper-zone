package com.sinetcodes.wallpaperzone.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;
import com.sinetcodes.wallpaperzone.pojo.Collection;
import com.sinetcodes.wallpaperzone.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectionListAdapter extends RecyclerView.Adapter<CollectionListAdapter.CollectionViewHolder> {
    Context mContext;
    OnItemClickListener mListener;
    List<Collection>  mCollectionList=new ArrayList<>();

    public CollectionListAdapter(Context context, OnItemClickListener listener, List<Collection> collectionList) {
        mContext = context;
        mListener = listener;
        mCollectionList = collectionList;
    }

    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.single_collections_list_item, parent, false);
        CollectionViewHolder viewHolder=new CollectionViewHolder(view,mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionViewHolder holder, int position) {
        holder.collectionTitle.setText(mCollectionList.get(position).getTitle());
        holder.totalPhotos.setText(mCollectionList.get(position).getTotalPhotos()+" photos");
        holder.collectionUser.setText(mCollectionList.get(position).getUser().getName());
        holder.collectionCoverImage.setBackgroundColor(Color.parseColor(mCollectionList.get(position).getCoverPhoto().getColor()));

        Glide.with(mContext)
                .load(mCollectionList.get(position).getCoverPhoto().getUrls().getSmall())
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.collectionCoverImage);

        Glide.with(mContext)
                .load(mCollectionList.get(position).getUser().getProfile_image().getMedium())
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.collectionUserImage);
    }

    @Override
    public int getItemCount() {
        return mCollectionList.size();
    }

    public void addItems(List<Collection> items) {
        if(items!=null)
        {
            mCollectionList.addAll(items);
            notifyDataSetChanged();
        }
    }

    public class CollectionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        OnItemClickListener mOnItemClickListener;

        @BindView(R.id.total_photos_text_view)
        TextView totalPhotos;
        @BindView(R.id.single_name)
        TextView collectionTitle;
        @BindView(R.id.photo_user)
        TextView collectionUser;
        @BindView(R.id.single_image)
        ImageView collectionCoverImage;
        @BindView(R.id.collection_user_image)
        RoundedImageView collectionUserImage;

        public CollectionViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            mOnItemClickListener=onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnItemClickListener.onItemClicked(mCollectionList.get(getAdapterPosition()));
        }
    }


    public interface OnItemClickListener{
        void onItemClicked(Collection collection);
    }
}
