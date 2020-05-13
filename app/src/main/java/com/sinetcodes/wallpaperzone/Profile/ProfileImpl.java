package com.sinetcodes.wallpaperzone.Profile;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public interface ProfileImpl {
    interface view{
        void setDownloads(File[] fileList);
        void showProgress();
        void hideProgress();
        void showToast(String message);
    }

    interface presenter{
        void getDownloads();
        void takeDownloads(File[] fileList);
    }

    interface model{
        void askDownloads();
    }
}
