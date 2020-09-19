package com.sinetcodes.wallpaperzone.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("username")
    @Expose
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("instagram_username")
    @Expose
    private String instagram_username;

    @SerializedName("profile_image")
    @Expose
    private PhotosUserProfileImage profile_image;


    public String getInstagram_username() {
        return instagram_username;
    }

    public void setInstagram_username(String instagram_username) {
        this.instagram_username = instagram_username;
    }

    public PhotosUserProfileImage getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(PhotosUserProfileImage profile_image) {
        this.profile_image = profile_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
