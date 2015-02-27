package com.testcode.cacheemall.comp;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.testcode.cacheemall.R;
import com.testcode.cacheemall.callback.GImageSearch;
import com.testcode.cacheemall.callback.ImageReadTask;
import com.testcode.cacheemall.callback.ImageWriteTask;
import com.testcode.cacheemall.callback.OnCImageSearchListener;
import com.testcode.cacheemall.db.ImageDataStore;
import com.testcode.cacheemall.models.CImage;
import com.testcode.cacheemall.util.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class HomeActivity extends ActionBarActivity implements OnCImageSearchListener,
        TextView.OnEditorActionListener {

    private AutoCompleteTextView mSearchEt;

    private ArrayList<CImage> mCImages = new ArrayList<>();
    private int mOffset = 1;
    private String mQuery;
    private ProgressBar mLoadingBar;
    private LazyAdapter mLazyAdapter;
    private ImageDataStore mDataStore;
    private Set<String> mStoredKeys;
    private GImageSearch mGImageSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDataStore = new ImageDataStore(this);
        mGImageSearch = new GImageSearch(this);

        GridView galleryGv = (GridView) findViewById(R.id.gallery_gv);
        mSearchEt = (AutoCompleteTextView) findViewById(R.id.search_et);
        mLoadingBar = (ProgressBar) findViewById(R.id.gimage_pb);

        galleryGv.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), false, true));

        mLazyAdapter = new LazyAdapter(this, mCImages);
        galleryGv.setAdapter(mLazyAdapter);

        mSearchEt.setOnEditorActionListener(this);
        invalidateAutoFill();
    }

    private void invalidateAutoFill() {

        mStoredKeys = mDataStore.getAllSearchKeys();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, mStoredKeys.toArray(new String[mStoredKeys.size()]));
        mSearchEt.setAdapter(adapter);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {

            String query = mSearchEt.getText().toString();
            if (query.trim().isEmpty()) return true;

            onSearchClicked(query.toLowerCase());
            hideKeypad();
        }
        return true;
    }

    private void hideKeypad() {

        InputMethodManager imm = (InputMethodManager) getSystemService(
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchEt.getWindowToken(), 0);
    }

    private void onSearchClicked(String query) {

        invalidateAutoFill();
        mCImages.removeAll(mCImages);
        mLazyAdapter.notifyDataSetChanged();

        mGImageSearch.cancelRequest();
        if (mStoredKeys.contains(query.trim())) {
            loadSavedImages(query);
            return;
        }

        mOffset = 1;
        mQuery = query;
        searchImages();
    }

    private void loadSavedImages(String query) {

        List<CImage> cImages = mDataStore.getImages(query.trim());
        mQuery = query;
        mOffset = cImages.size() == 0 ? 1 : cImages.size();
        new ImageReadTask(this).execute(cImages.toArray(new CImage[cImages.size()]));
    }

    private void searchImages() {

        if (mQuery.isEmpty())
            return;

        mLoadingBar.setVisibility(View.VISIBLE);
        mGImageSearch.setOffset(mOffset);
        mGImageSearch.setQuery(mQuery);
        mGImageSearch.request();
    }

    @Override
    public void onCImageFetched(CImage cImage) {

        if (!cImage.getKey().equals(mQuery))
            return;

        mCImages.add(cImage);
        mLazyAdapter.notifyDataSetChanged();

        if (cImage.getPath().isEmpty()) {

            ImageWriteTask writeTask = new ImageWriteTask() {
                @Override
                public void onPathSaved(CImage cImage) {
                    mDataStore.save(cImage);
                }
            };
            writeTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, cImage);
        }
    }

    @Override
    public void onImageLinksFetched(String s, ArrayList<String> imageLinks) {

        mLoadingBar.setVisibility(View.GONE);
    }

    @Override
    public void onFinished() {

        if (Utilities.isNetworkAvailable(this)) {
            mOffset += 10;
            searchImages();
        }
    }

    @Override
    public void onError(String message) {
        mLoadingBar.setVisibility(View.GONE);

        if (Utilities.isNetworkAvailable(this))
            Toast.makeText(this, R.string.api_not_resp, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, R.string.no_network, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataStore.close();
    }
}
