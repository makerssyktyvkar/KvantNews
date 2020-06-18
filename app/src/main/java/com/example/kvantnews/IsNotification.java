package com.example.kvantnews;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class IsNotification {

    private static final String DATABASE_NAME = "is_notification.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "is_notification";

    private static final String COLUMN_BOOLEAN = "boolean";

    private static final int NUM_COLUMN_BOOLEAN = 0;

    private SQLiteDatabase mDataBase;

    public IsNotification(Context context) {
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }

    public void change(int bol) {
        deleteAll();
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_BOOLEAN, bol);
        mDataBase.insert(TABLE_NAME, null, cv);
    }

    public void deleteAll() {
        mDataBase.execSQL("DELETE FROM " + TABLE_NAME);
    }


    public int select() {
        @SuppressLint("Recycle") Cursor mCursor = mDataBase.query(TABLE_NAME, null, null, null, null, null, null);

        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                return mCursor.getInt(NUM_COLUMN_BOOLEAN);
            } while (mCursor.moveToNext());
        }
        return 1;
    }

    private class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_BOOLEAN + " INTEGER);";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            ContentValues cv=new ContentValues();
            cv.put(COLUMN_BOOLEAN, 1);
            mDataBase.insert(TABLE_NAME, null, cv);
            onCreate(db);
        }
    }
}
