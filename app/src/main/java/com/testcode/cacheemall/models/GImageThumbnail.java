package com.testcode.cacheemall.models;

/**
 * Created by rafi on 26/2/15.
 */
public class GImageThumbnail {

    private String thumbnailLink;

    public String getThumbnailLink() {
        return thumbnailLink == null ? "" : thumbnailLink;
    }

    public void setThumbnailLink(String thumbnailLink) {
        this.thumbnailLink = thumbnailLink;
    }
}
