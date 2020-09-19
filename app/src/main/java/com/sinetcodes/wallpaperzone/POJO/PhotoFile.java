package com.sinetcodes.wallpaperzone.pojo;

import java.io.File;

public class PhotoFile {
    String key;
    File photoFile;

    public PhotoFile(String key, File photoFile) {
        this.key = key;
        this.photoFile = photoFile;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public File getPhotoFile() {
        return photoFile;
    }

    public void setPhotoFile(File photoFile) {
        this.photoFile = photoFile;
    }
}
