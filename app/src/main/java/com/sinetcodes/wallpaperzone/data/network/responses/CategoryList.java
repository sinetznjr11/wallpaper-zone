package com.sinetcodes.wallpaperzone.data.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryList {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

}
