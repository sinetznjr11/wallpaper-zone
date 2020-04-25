package com.sinetcodes.wallpaperzone.Search.CardStack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sinetcodes.wallpaperzone.POJO.Photos;
import com.sinetcodes.wallpaperzone.R;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {
    List<Photos> photoList;
    Context context;
    ViewPager2 viewPager2;


    public CardStackAdapter(List<Photos> photoList, Context context, ViewPager2 viewPager2) {
        this.photoList = photoList;
        this.context = context;
        this.viewPager2=viewPager2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_card_stack_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        
        holder.userName.setText(photoList.get(position).getUser().getInstagram_username());
        holder.instaUsername.setText(photoList.get(position).getUser().getInstagram_username());

        Glide.with(context).load(photoList.get(position).getUser().getProfile_image().getLarge()).into(holder.userImage);
        Glide.with(context)
                .load(photoList.get(position)
                        .getUrls().getRegular())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.loading_placeholder)
                .into(holder.itemImage);

        String[] itemImageUrl = {photoList.get(position).getUrls().getFull()};


       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new StfalconImageViewer.Builder<>(context, itemImageUrl, new ImageLoader<String>() {
                    @Override
                    public void loadImage(ImageView imageView, String image) {
                        Glide.with(context).load(image).into(imageView);
                    }
                });
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_image)
        ImageView itemImage;
        @BindView(R.id.item_user_image)
        ImageView userImage;
        @BindView(R.id.item_name)
        TextView userName;
        @BindView(R.id.item_instagram)
        TextView instaUsername;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public List<Photos> getphotoList() {
        return photoList;
    }

    public void setphotoList(List<Photos> photoList) {
        this.photoList.addAll(photoList);
    }


}
