package com.sinetcodes.wallpaperzone.Search.CardStack;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.sinetcodes.wallpaperzone.POJO.Photos;

import java.util.List;

public class CardStackCallback extends DiffUtil.Callback {
    private static final String TAG = "CardStackCallback";

    private List<Photos> oldModel, newModel;

    public CardStackCallback(List<Photos> oldModel, List<Photos> newModel) {
        this.oldModel = oldModel;
        this.newModel = newModel;
    }

    @Override
    public int getOldListSize() {
        return oldModel.size();
    }

    @Override
    public int getNewListSize() {
        return newModel.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldModel.get(oldItemPosition).getId().equals(newModel.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldModel.get(oldItemPosition) == newModel.get(newItemPosition);
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
