package com.testcode.cacheemall.comp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.testcode.cacheemall.R;
import com.testcode.cacheemall.models.CImage;

import java.util.ArrayList;

/**
 * Created by rafi on 25/2/15.
 */
public class LazyAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<CImage> mCImages;

    public LazyAdapter(Context context, ArrayList<CImage> cImages) {

        this.mContext = context;
        this.mCImages = cImages;
    }

    @Override
    public int getCount() {
        return mCImages.size();
    }

    @Override
    public Object getItem(int position) {
        return mCImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_gimage, null);
        }

        ImageView mImage = (ImageView) convertView.findViewById(R.id.gimage);
        mImage.setImageBitmap(mCImages.get(position).getBitmap());
        return convertView;
    }
}
