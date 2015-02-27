package com.testcode.cacheemall.callback;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.testcode.cacheemall.callback.RequestTask;
import com.testcode.cacheemall.util.Utilities;
import com.testcode.cacheemall.models.CImage;
import com.testcode.cacheemall.models.SearchResult;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafi on 24/2/15.
 */
public class GImageSearch implements RequestTask.OnRequestTaskListener {

    private static final String API_KEY = "AIzaSyBILmBzi_DAZVE8NnjbexBUkCXyEY7LFNU";
    private static final String CSE_KEY = "006141016826933911230:kpimq01hrco";
    private static final int DEFAULT_SIZE = 10;

    private final OnCImageSearchListener mGImageSearchListener;

    private int mOffset;
    private String mQueryString;
    private ImageLoadTask mImageLoadTask;

    public GImageSearch(OnCImageSearchListener searchListener) {
        this.mGImageSearchListener = searchListener;
    }

    public void setOffset(int offset) {
        this.mOffset = offset;
    }

    public void setQuery(String query) {
        this.mQueryString = query;
    }

    public void request() {

        String url = populateUrl();
        RequestTask requestTask = new RequestTask(this);
        requestTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
    }

    private String populateUrl() {

        try {
            return String.format("https://www.googleapis.com/customsearch/v1?" +
                            "key=%s&cx=%s&q=%s&start=%d&num=%d&searchType=image&imgsz=icon",
                    API_KEY,
                    CSE_KEY,
                    URLEncoder.encode(mQueryString, "UTF-8"),
                    mOffset,
                    DEFAULT_SIZE
            );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void onSuccess(JSONObject response) {

        final SearchResult result = new Gson().fromJson(response.toString(), SearchResult.class);
        ArrayList<String> imageLinks = result.getGImageLinks();

        if (result.getgImages().isEmpty()) {
            mGImageSearchListener.onError(response.toString());
            return;
        }

        this.mGImageSearchListener.onImageLinksFetched(response.toString(), imageLinks);
        String key = result.getSearchKey();

        mImageLoadTask = new ImageLoadTask(key);
        mImageLoadTask.executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR,
                imageLinks.toArray(new String[imageLinks.size()]));
    }

    @Override
    public void onError(String message) {
        Log.d("test_image_search", "RequestTask onError::" + message);
        mGImageSearchListener.onError(message);
    }

    public void cancelRequest() {
        if (mImageLoadTask != null) {
            mImageLoadTask.cancel(true);
        }
    }

    private class ImageLoadTask extends AsyncTask<String, CImage, List<CImage>> {

        private String mSearchKey;

        public ImageLoadTask(String s) {
            this.mSearchKey = s;
        }

        @Override
        protected void onProgressUpdate(CImage... values) {
            super.onProgressUpdate(values);

            if (values[0] != null)
                mGImageSearchListener.onCImageFetched(values[0]);
        }

        @Override
        protected List<CImage> doInBackground(String... params) {

            ArrayList<CImage> cImages = new ArrayList<>();
            for (final String s : params) {

                Bitmap bitmap = ImageLoader.getInstance().loadImageSync(s);
                if (bitmap != null) {
                    CImage cImage = new CImage();
                    cImage.setKey(mSearchKey);
                    cImage.setImageUrl(s);
                    cImage.setBitmap(Utilities.getScaledBitmap(bitmap, 200, 180));
                    publishProgress(cImage);
                    cImages.add(cImage);
                }
            }
            return cImages;
        }

        @Override
        protected void onPostExecute(List<CImage> cImages) {
            super.onPostExecute(cImages);
            mGImageSearchListener.onFinished();
        }
    }
}
