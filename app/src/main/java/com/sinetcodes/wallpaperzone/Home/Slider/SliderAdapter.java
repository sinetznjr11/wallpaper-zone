package com.sinetcodes.wallpaperzone.Home.Slider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.sinetcodes.wallpaperzone.R;
import com.sinetcodes.wallpaperzone.data.network.responses.Wallpaper;
import com.sinetcodes.wallpaperzone.databinding.SliderLayoutItemBinding;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {
    private static final String TAG = "SliderAdapter";
    private Context context;
    private List<Wallpaper> mSliderItems;

    public SliderAdapter(Context context, List<Wallpaper> wallpaperList) {
        this.mSliderItems = wallpaperList;
        this.context = context;
    }


    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        SliderLayoutItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.slider_layout_item, parent, false
        );
        return new SliderAdapterVH(binding);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        viewHolder.mSliderLayoutItemBinding.setWallpaper(mSliderItems.get(position));
    }

    @Override
    public int getCount() {
        return 5;
    }

    public class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
        private SliderLayoutItemBinding mSliderLayoutItemBinding;

        public SliderAdapterVH(SliderLayoutItemBinding binding) {
            super(binding.getRoot());
            this.mSliderLayoutItemBinding = binding;
        }

    }


}
