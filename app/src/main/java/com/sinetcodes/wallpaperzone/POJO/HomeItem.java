package com.sinetcodes.wallpaperzone.pojo;

import java.util.List;

public class HomeItem<T> {
    String title;
    List<T> items;

    public HomeItem(String title, List<T> items) {
        this.title = title;
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
