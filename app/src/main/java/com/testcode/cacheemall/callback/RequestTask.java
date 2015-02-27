package com.testcode.cacheemall.callback;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rafi on 26/2/15.
 */

public class RequestTask extends AsyncTask<String, Void, String> {

    private OnRequestTaskListener mOnRequestTaskListener;

    public RequestTask(OnRequestTaskListener listener) {
        this.mOnRequestTaskListener = listener;
    }

    @Override
    protected String doInBackground(String... params) {

        String json = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(params[0]);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();

            json = EntityUtils.toString(httpEntity);

        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (s == null) {
            mOnRequestTaskListener.onError(null);
            return;
        }

        try {
            JSONObject object = new JSONObject(s);
            mOnRequestTaskListener.onSuccess(object);
        } catch (JSONException e) {
            e.printStackTrace();
            mOnRequestTaskListener.onError(null);
        }
    }

    public interface OnRequestTaskListener {

        public void onSuccess(JSONObject object);

        public void onError(String message);
    }
}
