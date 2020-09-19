package com.sinetcodes.wallpaperzone.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Results {
    @SerializedName("total")
    @Expose
    private int total;

    @SerializedName("total_pages")
    @Expose
    private String total_pages;

    @SerializedName("results")
    @Expose
    private List<Photos> photos;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(String total_pages) {
        this.total_pages = total_pages;
    }

    public List<Photos> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photos> photos) {
        this.photos = photos;
    }
}
