package com.testcode.cacheemall.callback;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.testcode.cacheemall.models.CImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rafi on 26/2/15.
 */
public class ImageReadTask extends AsyncTask<CImage, CImage, List<CImage>> {

    private OnCImageSearchListener mOnCImageSearchListener;

    public ImageReadTask(OnCImageSearchListener searchListener) {
        this.mOnCImageSearchListener = searchListener;
    }

    @Override
    protected void onProgressUpdate(CImage... values) {
        super.onProgressUpdate(values);

        if (values[0] != null)
            mOnCImageSearchListener.onCImageFetched(values[0]);
    }

    @Override
    protected List<CImage> doInBackground(CImage... params) {

        for (final CImage cImage : params) {
            String tmpPath = cImage.getPath();
            if (tmpPath.isEmpty()) continue;

            Bitmap bitmap = ImageLoader.getInstance().loadImageSync(cImage.getUriPath());
            if (bitmap != null) {
                cImage.setBitmap(bitmap);
                publishProgress(cImage);
            }
        }
        return Arrays.asList(params);
    }

    @Override
    protected void onPostExecute(List<CImage> cImages) {
        super.onPostExecute(cImages);
        mOnCImageSearchListener.onFinished();
    }
}
