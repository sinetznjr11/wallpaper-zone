package com.sinetcodes.wallpaperzone.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Collection implements Serializable {
    @SerializedName("id")
    @Expose
    int id;

    @SerializedName("title")
    @Expose
    String title;

    @SerializedName("description")
    @Expose
    String description;

    @SerializedName("published_at")
    @Expose
    String publishedAt;

    @SerializedName("total_photos")
    @Expose
    int totalPhotos;

    @SerializedName("share_key")
    @Expose
    String shareKey;

    @SerializedName("user")
    @Expose
     User user;

    @SerializedName("cover_photo")
    @Expose
     Photos coverPhoto;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public int getTotalPhotos() {
        return totalPhotos;
    }

    public void setTotalPhotos(int totalPhotos) {
        this.totalPhotos = totalPhotos;
    }

    public String getShareKey() {
        return shareKey;
    }

    public void setShareKey(String shareKey) {
        this.shareKey = shareKey;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Photos getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(Photos coverPhoto) {
        this.coverPhoto = coverPhoto;
    }
}
