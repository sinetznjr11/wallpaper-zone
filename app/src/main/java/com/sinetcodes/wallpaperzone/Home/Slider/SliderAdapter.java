package com.sinetcodes.wallpaperzone.Home.Slider;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.sinetcodes.wallpaperzone.POJO.Photos;
import com.sinetcodes.wallpaperzone.PhotoView.PhotoViewActivity;
import com.sinetcodes.wallpaperzone.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {
    private static final String TAG = "SliderAdapter";
    private Context context;
    private List<Photos> mSliderItems = new ArrayList<>();

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public void addItems(List<Photos> sliderItems) {
        mSliderItems.addAll(sliderItems);
        notifyDataSetChanged();
    }

    public void removeItems(){
        mSliderItems.removeAll(mSliderItems);
        notifyDataSetChanged();
    }


    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout_item,null);
        return new SliderAdapterVH(view);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        Photos sliderItem = mSliderItems.get(position);
        Picasso.get()
                .load(sliderItem.getUrls().getSmall())
                .into(viewHolder.imageViewBackground);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PhotoViewActivity.class);
                Photos photoItem = mSliderItems.get(position);
                intent.putExtra("photoItem", photoItem);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    public class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
        @BindView(R.id.iv_auto_image_slider)
        ImageView imageViewBackground;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }


}
