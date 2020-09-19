package com.sinetcodes.wallpaperzone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.sinetcodes.wallpaperzone.pojo.CategoryItem;
import com.sinetcodes.wallpaperzone.R;

import butterknife.ButterKnife;

public class CategoriesAdapter extends FirebaseRecyclerAdapter<CategoryItem,CategoriesAdapter.CategoryViewHolder> {

    Context mContext;
    OnCategoryClickListener mListener;
    public CategoriesAdapter(@NonNull FirebaseRecyclerOptions<CategoryItem> options,Context context, OnCategoryClickListener onCategoryClickListener) {
        super(options);
        mContext=context;
        mListener=onCategoryClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int i, @NonNull CategoryItem categoryItem) {

    /*    holder.titleText.setText(categoryItem.getName());
        Glide.with(mContext)
                .load(categoryItem.getImage_url())
                .thumbnail(0.1f)
                .into(holder.coverImage);*/

    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       LayoutInflater inflater=LayoutInflater.from(parent.getContext());
       View view = inflater.inflate(R.layout.single_category_item, parent, false);
       CategoryViewHolder viewHolder=new CategoryViewHolder(view,mListener);
       return viewHolder;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

      /*  @BindView(R.id.category_card)
        MaterialCardView mCardView;
        @BindView(R.id.cover_image)
        ImageView coverImage;
        @BindView(R.id.title_text)
        TextView titleText;*/

        OnCategoryClickListener mOnCategoryClickListener;

        public CategoryViewHolder(@NonNull View itemView, OnCategoryClickListener onCategoryClickListener) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            mOnCategoryClickListener=onCategoryClickListener;
            //mCardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnCategoryClickListener.onCategoryClicked(getItem(getAdapterPosition()).getName());
        }
    }

    public interface OnCategoryClickListener{
        void onCategoryClicked(String title);
    }

    @NonNull
    @Override
    public CategoryItem getItem(int position) {
        return super.getItem(position);
    }
}
