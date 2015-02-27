package com.testcode.cacheemall.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by rafi on 25/2/15.
 */
public class SearchResult {

    @SerializedName("items")
    private ArrayList<GImage> gImages = new ArrayList<>();

    private GQueries queries;

    public ArrayList<GImage> getgImages() {
        return gImages;
    }

    public void setgImages(ArrayList<GImage> gImages) {
        this.gImages = gImages;
    }

    public GQueries getQueries() {
        return queries;
    }

    public void setQueries(GQueries queries) {
        this.queries = queries;
    }

    public String getSearchKey(){
        if (queries == null || queries.getRequest().isEmpty()) return "";

        return queries.getRequest().get(0).getSearchTerms();
    }

    public ArrayList<String> getGImageLinks(){
        ArrayList<String> list = new ArrayList<>();

        if (gImages.isEmpty()) return list;

        for (GImage gImage : gImages){
            list.add(gImage.getLink());
        }
        return list;
    }

}
