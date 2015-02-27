package com.testcode.cacheemall.callback;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.testcode.cacheemall.models.CImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rafi on 26/2/15.
 */

public abstract class ImageWriteTask extends AsyncTask<CImage, Void, CImage> {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyhhmmss-SSS");

    private static String getUniqueName() {
        return simpleDateFormat.format(new Date());
    }

    @Override
    protected CImage doInBackground(CImage... params) {

        CImage cImage = params[0];

        if (cImage == null) return null;

        FileOutputStream out = null;
        try {

            String path = Environment.getExternalStorageDirectory().toString();
            String uniqueName = getUniqueName();
            File directory = new File(path, "cacheImages");

            if (!directory.exists()) {
                directory.mkdir();
            }

            String concatName = "CA" + uniqueName + ".png";
            File file = new File(directory, concatName);

            out = new FileOutputStream(file);
            cImage.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, out);
            cImage.setPath(directory + "/" + concatName);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return cImage;
    }

    @Override
    protected void onPostExecute(CImage cImage) {
        super.onPostExecute(cImage);

        if (cImage != null && !cImage.getPath().isEmpty()) {
            onPathSaved(cImage);
        }
    }

    public abstract void onPathSaved(CImage cImage);
}
