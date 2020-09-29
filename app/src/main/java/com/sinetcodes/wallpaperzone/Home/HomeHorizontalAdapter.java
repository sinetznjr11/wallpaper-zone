package com.sinetcodes.wallpaperzone.Home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sinetcodes.wallpaperzone.Common.ContentType;
import com.sinetcodes.wallpaperzone.data.network.responses.Wallpaper;
import com.sinetcodes.wallpaperzone.databinding.SingleHorizontalPhotoItemBinding;
import com.sinetcodes.wallpaperzone.pojo.Photos;
import com.sinetcodes.wallpaperzone.R;
import com.sinetcodes.wallpaperzone.data.network.responses.Category;
import com.sinetcodes.wallpaperzone.databinding.SingleCategoryItemBinding;

import java.util.List;

public class HomeHorizontalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Object> list;
    String type;
    int parentPosition = 0;

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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (type.equalsIgnoreCase(ContentType.CATEGORY)) {
            SingleCategoryItemBinding categoryItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.single_category_item, parent, false);
            return new HorizontalCategoryViewHolder(categoryItemBinding);
        } else {
            SingleHorizontalPhotoItemBinding photoItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.single_horizontal_photo_item, parent, false);
            return new HorizontalPhotoViewHolder(photoItemBinding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (type.equalsIgnoreCase(ContentType.CATEGORY)) {
            Category category = (Category) list.get(position);
            ((HorizontalCategoryViewHolder) holder).mBinding.setCategory(category);
            ((HorizontalCategoryViewHolder) holder).mBinding.getRoot().setOnClickListener(v -> mListener.onCategoryItemClick(category));
        } else {
            ((HorizontalPhotoViewHolder) holder).mBinding.setWallpaper((Wallpaper) list.get(position));
            ((HorizontalPhotoViewHolder) holder).mBinding.getRoot().setOnClickListener(v -> mListener.onPhotoItemClick((List<Wallpaper>) (List<?>) list, position));
        }
    }

    public void removeAll() {
        list.removeAll(list);
    }

    public void addPopularContent(List<Object> photos) {
        if (type.equals(ContentType.POPULAR)) {
            list.addAll(photos);
        }
    }

    @Override
    public int getItemCount() {
        Log.e(TAG, "getItemCount: " + list.size());
        return list.size();
    }

    public Photos getPhotoItem(int position) {
        return (Photos) list.get(position);
    }

    //category
    public class HorizontalCategoryViewHolder extends RecyclerView.ViewHolder {
        private SingleCategoryItemBinding mBinding;

        public HorizontalCategoryViewHolder(SingleCategoryItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }

    //horizontal photo
    public class HorizontalPhotoViewHolder extends RecyclerView.ViewHolder {
        private SingleHorizontalPhotoItemBinding mBinding;

        public HorizontalPhotoViewHolder(SingleHorizontalPhotoItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }


    public interface OnChildItemClickedListener {
        void onPhotoItemClick(List<Wallpaper> wallpaperList, int position);

        void onCategoryItemClick(Category category);
    }


}
