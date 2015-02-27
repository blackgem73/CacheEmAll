package com.testcode.cacheemall.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.testcode.cacheemall.models.CImage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by rafi on 26/2/15.
 */
public class ImageDataStore {

    private final DbHelper mDbHelper;
    private SQLiteDatabase mDatabase;

    private String[] columns = {DbHelper.COLUMN_CMIAGE_KEY,
            DbHelper.COLUMN_CMIAGE_PATH};

    public ImageDataStore(Context context) {
        mDbHelper = new DbHelper(context);
    }

    public void open(){
        if (!isOpen())
            mDatabase = mDbHelper.getWritableDatabase();
    }

    private boolean isOpen(){
        return mDatabase != null && mDatabase.isOpen();
    }

    public void close() {
        if (isOpen())
            mDbHelper.close();
    }

    public Set<String> getAllSearchKeys() {

        open();
        Cursor cursor = mDatabase.query(true, DbHelper.TABLE_IMAGES, new String[]{DbHelper.COLUMN_CMIAGE_KEY},
                null, null, null, null, null, null);

        Set<String> set = new HashSet<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            set.add(cursor.getString(0));
            cursor.moveToNext();
        }

        cursor.close();
        close();
        return set;
    }

    public void save(CImage cImage) {

        open();
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_CMIAGE_KEY, cImage.getKey().trim());
        values.put(DbHelper.COLUMN_CMIAGE_PATH, cImage.getPath());

        try {
            mDatabase.insert(DbHelper.TABLE_IMAGES, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            close();
        }
    }

    public List<CImage> getImages(String key) {

        open();
        List<CImage> tempImages = new ArrayList<CImage>();

        Cursor cursor = mDatabase.query(DbHelper.TABLE_IMAGES, columns,
                DbHelper.COLUMN_CMIAGE_KEY + "=?",
                new String[]{key}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CImage cImage = getCImage(cursor);
            tempImages.add(cImage);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return tempImages;
    }

    private CImage getCImage(Cursor cursor) {

        CImage cImage = new CImage();
        cImage.setKey(cursor.getString(0));
        cImage.setPath(cursor.getString(1));
        return cImage;
    }

}
