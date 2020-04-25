package com.sinetcodes.wallpaperzone.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.button.MaterialButton;
import com.sinetcodes.wallpaperzone.Common.ContentType;
import com.sinetcodes.wallpaperzone.POJO.ExploreItem;
import com.sinetcodes.wallpaperzone.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.ExploreVH> {
    Context context;
    List<ExploreItem> exploreItems = new ArrayList<>();
    private static final String TAG = "ExploreAdapter";
    OnParentItemClickListener mListener;
    ExploreHorizontalAdapter.OnChildItemClickedListener mChildItemClickedListener;

    ExploreMVPInterface.view mView;

    boolean isScrolling=false;
    ExploreHorizontalAdapter popularAdapter;


    public ExploreAdapter(Context context, ExploreMVPInterface.view view, List<ExploreItem> exploreItems, OnParentItemClickListener onParentItemClickListener, ExploreHorizontalAdapter.OnChildItemClickedListener onChildItemClickedListener) {
        this.context = context;
        this.exploreItems = exploreItems;
        this.mListener = onParentItemClickListener;
        this.mChildItemClickedListener = onChildItemClickedListener;
        this.mView = view;
    }

    @Override
    public ExploreVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_explore_vertical_item, parent, false);
        return new ExploreVH(v, mListener);
    }

    @Override
    public void onBindViewHolder(ExploreVH holder, int position) {

        String title = exploreItems.get(position).getTitle();
        holder.headerText.setText(title);

        holder.horizontalRV.setNestedScrollingEnabled(false);

        if (title.equalsIgnoreCase(ContentType.POPULAR)) {
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            holder.horizontalRV.setLayoutManager(layoutManager);
            popularAdapter = new ExploreHorizontalAdapter(context, mChildItemClickedListener, position, exploreItems.get(position).getItems(), title);
            holder.horizontalRV.setAdapter(popularAdapter);

        } else {

            holder.horizontalRV.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
            ExploreHorizontalAdapter adapter = new ExploreHorizontalAdapter(context, mChildItemClickedListener, position, exploreItems.get(position).getItems(), title);
            holder.horizontalRV.setAdapter(adapter);
        }


    }

    @Override
    public int getItemCount() {
        return exploreItems.size();
    }

    public void addPopularContent(List<Object> photosList){
        popularAdapter.addPopularContent(photosList);
    }


    public static class ExploreVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.header_text)
        TextView headerText;
        @BindView(R.id.explore_horizontal_rv)
        RecyclerView horizontalRV;
        @BindView(R.id.btn_show_all)
        MaterialButton btnShowAll;
        OnParentItemClickListener mOnParentItemClickListener;

        public ExploreVH(@NonNull View itemView, OnParentItemClickListener onParentItemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mOnParentItemClickListener = onParentItemClickListener;
            itemView.setOnClickListener(this);
            btnShowAll.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnParentItemClickListener.onParentItemClicked(v, getAdapterPosition());
        }
    }

    public interface OnParentItemClickListener {
        public void onParentItemClicked(View view, int position);
    }
}
