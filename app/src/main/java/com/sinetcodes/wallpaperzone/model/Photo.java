package com.sinetcodes.wallpaperzone.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photo {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("width")
    @Expose
    private int width;

    @SerializedName("height")
    @Expose
    private int height;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("photographer")
    @Expose
    private String photographer;

    @SerializedName("photographer_url")
    @Expose
    private String photographer_url;

    @SerializedName("photographer_id")
    @Expose
    private int photographer_id;

    @SerializedName("src")
    @Expose
    Src SrcObject;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhotographer() {
        return photographer;
    }

    public void setPhotographer(String photographer) {
        this.photographer = photographer;
    }

    public String getPhotographer_url() {
        return photographer_url;
    }

    public void setPhotographer_url(String photographer_url) {
        this.photographer_url = photographer_url;
    }

    public int getPhotographer_id() {
        return photographer_id;
    }

    public void setPhotographer_id(int photographer_id) {
        this.photographer_id = photographer_id;
    }

    public Src getSrcObject() {
        return SrcObject;
    }

    public void setSrcObject(Src srcObject) {
        SrcObject = srcObject;
    }

    public void setSrc(Src srcObject) {
        this.SrcObject = srcObject;
    }

    public class Src {
        private String original;
        private String large2x;
        private String large;
        private String medium;
        private String small;
        private String portrait;
        private String landscape;
        private String tiny;


        // Getter Methods

        public String getOriginal() {
            return original;
        }

        public String getLarge2x() {
            return large2x;
        }

        public String getLarge() {
            return large;
        }

        public String getMedium() {
            return medium;
        }

        public String getSmall() {
            return small;
        }

        public String getPortrait() {
            return portrait;
        }

        public String getLandscape() {
            return landscape;
        }

        public String getTiny() {
            return tiny;
        }

        // Setter Methods

        public void setOriginal(String original) {
            this.original = original;
        }

        public void setLarge2x(String large2x) {
            this.large2x = large2x;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public void setLandscape(String landscape) {
            this.landscape = landscape;
        }

        public void setTiny(String tiny) {
            this.tiny = tiny;
        }
    }
}
