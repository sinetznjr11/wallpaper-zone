package com.sinetcodes.wallpaperzone.data.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WallpaperList {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("wallpapers")
    @Expose
    private List<Wallpaper> wallpapers = null;
    @SerializedName("is_last")
    @Expose
    private Boolean isLast;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Wallpaper> getWallpapers() {
        return wallpapers;
    }

    public void setWallpapers(List<Wallpaper> wallpapers) {
        this.wallpapers = wallpapers;
    }

    public Boolean getIsLast() {
        return isLast;
    }

    public void setIsLast(Boolean isLast) {
        this.isLast = isLast;
    }
}
