package com.testcode.cacheemall.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rafi on 25/2/15.
 */
public class GImage {

    private String link;

    @SerializedName("image")
    private GImageThumbnail thumbnail;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public GImageThumbnail getThumbnail() {
        return thumbnail == null ? new GImageThumbnail() : thumbnail;
    }

    public void setThumbnail(GImageThumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }
}
