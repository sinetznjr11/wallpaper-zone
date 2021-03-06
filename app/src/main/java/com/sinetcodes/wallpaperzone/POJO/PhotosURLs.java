package com.sinetcodes.wallpaperzone.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PhotosURLs implements Serializable {
    @SerializedName("created_at")
    @Expose
    private String raw;
    @SerializedName("raw")
    @Expose
    private String full;
    @SerializedName("full")
    @Expose
    private String regular;
    @SerializedName("regular")
    @Expose
    private String small;
    @SerializedName("small")
    @Expose
    private String thumb;

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
