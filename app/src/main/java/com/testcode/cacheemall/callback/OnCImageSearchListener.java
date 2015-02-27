package com.testcode.cacheemall.callback;

import com.testcode.cacheemall.models.CImage;

import java.util.ArrayList;

/**
 * Created by rafi on 26/2/15.
 */

public interface OnCImageSearchListener {

    public void onCImageFetched(CImage cImage);

    public void onImageLinksFetched(String s, ArrayList<String> imageLinks);

    public void onFinished();

    public void onError(String message);
}
