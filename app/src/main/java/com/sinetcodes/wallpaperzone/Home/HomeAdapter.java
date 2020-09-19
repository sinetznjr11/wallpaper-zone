package com.sinetcodes.wallpaperzone.Home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sinetcodes.wallpaperzone.Common.ContentType;
import com.sinetcodes.wallpaperzone.pojo.HomeItem;
import com.sinetcodes.wallpaperzone.R;
import com.sinetcodes.wallpaperzone.databinding.SingleHomeVerticalItemBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeVH> {
    Context context;
    List<HomeItem> mHomeItems = new ArrayList<>();
    private static final String TAG = "HomeAdapter";
    OnParentItemClickListener mListener;
    HomeHorizontalAdapter.OnChildItemClickedListener mChildItemClickedListener;


    boolean isScrolling = false;
    RecyclerView homeHorizontalRecyclerView;
    HomeHorizontalAdapter popularAdapter;


    public HomeAdapter(Context context, List<HomeItem> homeItems, OnParentItemClickListener onParentItemClickListener, HomeHorizontalAdapter.OnChildItemClickedListener onChildItemClickedListener) {
        this.context = context;
        this.mHomeItems = homeItems;
        this.mListener = onParentItemClickListener;
        this.mChildItemClickedListener = onChildItemClickedListener;
    }

    @Override
    public HomeVH onCreateViewHolder(ViewGroup parent, int viewType) {
        SingleHomeVerticalItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.single_home_vertical_item, parent, false);
        homeHorizontalRecyclerView = binding.homeHorizontalRv;
        return new HomeVH(binding, mListener);
    }

    @Override
    public void onBindViewHolder(HomeVH holder, int position) {

        HomeItem homeItem = mHomeItems.get(position);
        holder.mBinding.setData(homeItem);
        if (homeItem.getTitle().equalsIgnoreCase(ContentType.CATEGORY)) {
            holder.mBinding.headerText.setVisibility(View.GONE);
            holder.mBinding.btnShowAll.setVisibility(View.GONE);
        }
        setHorizontalRecyclerView(position, homeItem.getTitle(), homeItem.getItems());


    }

    private void setHorizontalRecyclerView(int position, String title, List items) {
        homeHorizontalRecyclerView.setNestedScrollingEnabled(false);

/*            holder.btnShowAll.setVisibility(View.INVISIBLE);
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            holder.horizontalRV.setLayoutManager(layoutManager);
            popularAdapter = new HomeHorizontalAdapter(context, mChildItemClickedListener, position, mHomeItems.get(position).getItems(), title);
            holder.horizontalRV.setAdapter(popularAdapter);*/

        homeHorizontalRecyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        HomeHorizontalAdapter adapter = new HomeHorizontalAdapter(context, mChildItemClickedListener, position, items, title);
        homeHorizontalRecyclerView.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        Log.e(TAG, "getItemCount: " + mHomeItems.size());
        return mHomeItems.size();
    }


    public static class HomeVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnParentItemClickListener mOnParentItemClickListener;
        SingleHomeVerticalItemBinding mBinding;

        public HomeVH(SingleHomeVerticalItemBinding binding, OnParentItemClickListener onParentItemClickListener) {
            super(binding.getRoot());
            mBinding = binding;
            this.mOnParentItemClickListener = onParentItemClickListener;
            itemView.setOnClickListener(this);
            mBinding.btnShowAll.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnParentItemClickListener.onParentItemClicked(v, getAdapterPosition());
        }
    }

    public interface OnParentItemClickListener {
        void onParentItemClicked(View view, int position);
    }
}
