package com.sinetcodes.wallpaperzone.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Photos implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("width")
    @Expose
    private int width;

    @SerializedName("height")
    @Expose
    private int height;

    @SerializedName("color")
    @Expose
    private String color;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("alt_description")
    @Expose
    private String alt_description;

    @SerializedName("urls")
    @Expose
    private PhotosURLs urls;

    @SerializedName("links")
    @Expose
    private PhotosLinks links;

    @SerializedName("likes")
    @Expose
    private int likes;

    @SerializedName("user")
    @Expose
    private User user;

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAlt_description() {
        return alt_description;
    }

    public void setAlt_description(String alt_description) {
        this.alt_description = alt_description;
    }

    public PhotosURLs getUrls() {
        return urls;
    }

    public void setUrls(PhotosURLs urls) {
        this.urls = urls;
    }

    public PhotosLinks getLinks() {
        return links;
    }

    public void setLinks(PhotosLinks links) {
        this.links = links;
    }
}
