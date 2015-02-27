package com.testcode.cacheemall.models;

import android.graphics.Bitmap;

/**
 * Created by rafi on 25/2/15.
 */
public class CImage {

    private String key;

    private String path;

    private String imageUrl;

    private Bitmap bitmap;

    public CImage() {

    }

    public CImage(String key, String imageUrl, Bitmap bitmap) {
        this.key = key;
        this.imageUrl = imageUrl;
        this.bitmap = bitmap;
    }

    public String getKey() {
        return key == null ? "" : key.toLowerCase();
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPath() {
        return path == null ? "" : path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getUriPath() {
        return getPath().isEmpty() ? "" : "file://" + path;
    }
}
