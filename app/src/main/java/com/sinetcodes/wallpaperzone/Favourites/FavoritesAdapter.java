package com.sinetcodes.wallpaperzone.Favourites;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.sinetcodes.wallpaperzone.POJO.Photos;
import com.sinetcodes.wallpaperzone.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

class FavoritesAdapter extends FirebaseRecyclerAdapter<Photos, FavoritesAdapter.FavoriteViewHolder> {

    OnItemClickListener mListener;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    FavoritesAdapter(@NonNull FirebaseRecyclerOptions options, OnItemClickListener onItemClickListener) {
        super(options);
        mListener = onItemClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull FavoriteViewHolder holder, int i, @NonNull Photos photos) {
        Picasso.get()
                .load(photos.getUrls().getThumb())
                .into(holder.mImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Picasso.get()
                                .load(photos.getUrls().getSmall());
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
        String ratio = photos.getWidth() + ":" + photos.getHeight();
        ConstraintSet set = new ConstraintSet();
        set.clone(holder.constraintLayout);
        set.setDimensionRatio(holder.mImageView.getId(), ratio);
        set.applyTo(holder.constraintLayout);
    }

    @NonNull
    @Override
    public Photos getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_explore_popular_item, parent, false);
        return new FavoriteViewHolder(view, mListener);
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.constraint_image)
        ConstraintLayout constraintLayout;
        @BindView(R.id.single_image)
        RoundedImageView mImageView;
        OnItemClickListener mOnItemClickListener;

        FavoriteViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mOnItemClickListener = onItemClickListener;
            mImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnItemClickListener.onItemClicked(getItem(getAdapterPosition()));
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(Photos photo);
    }
}
