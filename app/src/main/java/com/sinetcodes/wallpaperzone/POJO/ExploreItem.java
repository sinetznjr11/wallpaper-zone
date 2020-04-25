package com.sinetcodes.wallpaperzone.POJO;

import java.util.List;

public class ExploreItem {
    String title;
    List<Object> items;

    public ExploreItem(String title, List<Object> items) {
        this.title = title;
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Object> getItems() {
        return items;
    }

    public void setItems(List<Object> items) {
        this.items = items;
    }
}
