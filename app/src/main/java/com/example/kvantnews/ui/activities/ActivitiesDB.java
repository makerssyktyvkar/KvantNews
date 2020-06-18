package com.example.kvantnews.ui.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ActivitiesDB {

    private static final String DATABASE_NAME = "activities.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "activities";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_MESSAGE = "message";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_TYPE = "type";

    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_TITLE = 1;
    private static final int NUM_COLUMN_MESSAGE = 2;
    private static final int NUM_COLUMN_IMAGE = 3;
    private static final int NUM_COLUMN_TIME = 4;
    private static final int NUM_COLUMN_TYPE = 5;

    private SQLiteDatabase mDataBase;

    public ActivitiesDB(Context context) {
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }

    public long insert(long id, String title, String message, String image, String time, String type) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_ID, id);
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_MESSAGE, message);
        cv.put(COLUMN_IMAGE, image);
        cv.put(COLUMN_TIME, Integer.parseInt(time));
        cv.put(COLUMN_TYPE, type);
        return mDataBase.insert(TABLE_NAME, null, cv);
    }

    public int update(long id, String title, String message, String image, String time, String type) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_MESSAGE, message);
        cv.put(COLUMN_IMAGE, image);
        cv.put(COLUMN_TIME, time);
        cv.put(COLUMN_TYPE, type);
        return mDataBase.update(TABLE_NAME, cv, COLUMN_ID + " = ?",new String[] { String.valueOf(id)});
    }

    public void deleteAll() {
        mDataBase.delete(TABLE_NAME, null, null);
    }

    public void delete(long id) {
        mDataBase.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }



    public ArrayList<Activity> selectAll() {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, null, null, null, null, null);

        ArrayList<Activity> arr = new ArrayList<>();
        mCursor.moveToLast();
        if (!mCursor.isBeforeFirst()) {
            do {
                String title = mCursor.getString(NUM_COLUMN_TITLE);
                String message = mCursor.getString(NUM_COLUMN_MESSAGE);
                String image = mCursor.getString(NUM_COLUMN_IMAGE);
                int time = mCursor.getInt(NUM_COLUMN_TIME);
                String type = mCursor.getString(NUM_COLUMN_TYPE);
                arr.add(new Activity(title, message, image, time, type));
            } while (mCursor.moveToPrevious());
        }
        return arr;
    }

    public ArrayList<Activity> selectSpecials(String type2, int minTime) {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, "type = ?", new String[]{ type2}, null, null, null);

        ArrayList<Activity> arr = new ArrayList<>();
        mCursor.moveToLast();
        if (!mCursor.isBeforeFirst()) {
            do {
                int time = mCursor.getInt(NUM_COLUMN_TIME);
                if(time >= minTime) {
                    String title = mCursor.getString(NUM_COLUMN_TITLE);
                    String message = mCursor.getString(NUM_COLUMN_MESSAGE);
                    String image = mCursor.getString(NUM_COLUMN_IMAGE);
                    String type = mCursor.getString(NUM_COLUMN_TYPE);
                    arr.add(new Activity(title, message, image, time, type));
                }
            } while (mCursor.moveToPrevious());
        }
        return arr;
    }

    private class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER, " +
                    COLUMN_TITLE+ " TEXT, " +
                    COLUMN_MESSAGE+ " TEXT, " +
                    COLUMN_IMAGE + " TEXT, " +
                    COLUMN_TIME + " INTEGER, " +
                    COLUMN_TYPE + " TEXT);";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

}