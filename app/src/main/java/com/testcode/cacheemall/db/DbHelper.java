package com.testcode.cacheemall.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rafi on 26/2/15.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final String TABLE_IMAGES = "images";
    public static final String COLUMN_CIMAGE_ID = "_id";
    public static final String COLUMN_CMIAGE_KEY = "key";
    public static final String COLUMN_CMIAGE_PATH = "path";

    private static final String DATABASE_NAME = "imagefactory.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table "
            + TABLE_IMAGES + "(" + COLUMN_CIMAGE_ID
            + " integer primary key autoincrement, "
            + COLUMN_CMIAGE_KEY + " text not null,"
            + COLUMN_CMIAGE_PATH + " text not null);";

    DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
        onCreate(db);
    }
}
