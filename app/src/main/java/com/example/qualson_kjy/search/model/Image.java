package com.example.qualson_kjy.search.model;

import java.io.Serializable;

public class Image implements Serializable {

    private int page;
    private ChannelItem item;

    public Image(int page, ChannelItem item) {
        this.page = page;
        this.item = item;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int count) {
        this.page = count;
    }

    public String getThumbnail() {
        return item.getThumbnail();
    }

    public String getImage() {
        return item.getImage();
    }

    public String getTitle() {
        return item.getTitle();
    }

}
