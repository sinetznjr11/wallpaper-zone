package com.sinetcodes.wallpaperzone.utils;

import com.sinetcodes.wallpaperzone.data.network.SetupRetrofit;

public class UrlUtil {
    public static String getUrl(String method, int page) {
        return SetupRetrofit.BASE_URL + "get.php?auth=" + StringsUtil.API_KEY + "&method=" + method + "&sort=&info_level=3&page=" + page + "&width=0&height=0&check_last=1";
    }
}
