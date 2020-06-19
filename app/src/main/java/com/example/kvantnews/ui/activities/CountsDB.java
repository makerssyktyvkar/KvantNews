package com.example.kvantnews.ui.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CountsDB {

    private static final String DATABASE_NAME = "counts.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "counts";

    private static final String COLUMN_COLUMN = "column";
    private static final String COLUMN_ROW = "row";
    private static final String COLUMN_COUNT = "count";

    private static final int NUM_COLUMN_COLUMN = 0;
    private static final int NUM_COLUMN_ROW = 1;
    private static final int NUM_COLUMN_COUNT = 2;

    private SQLiteDatabase mDataBase;

    public CountsDB(Context context) {
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }

    public long insert(int column, int row, String value) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_COLUMN, column);
        cv.put(COLUMN_ROW, row);
        cv.put(COLUMN_COUNT, value);
        return mDataBase.insert(TABLE_NAME, null, cv);
    }

    public void insert(String[][] counts) {
        for(int i = 0;i < 3; i++){
            for(int j = 0; j < 3; j++){
                ContentValues cv=new ContentValues();
                cv.put(COLUMN_COLUMN, i);
                cv.put(COLUMN_ROW, j);
                cv.put(COLUMN_COUNT, counts[i][j]);
                mDataBase.insert(TABLE_NAME, null, cv);
            }
        }
    }

    public void deleteAll() {
        mDataBase.delete(TABLE_NAME, null, null);
    }



    public String[][] selectAll() {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, null, null, null, null, null);

        String[][] arr = new String[3][3];
        mCursor.moveToLast();
        if (!mCursor.isBeforeFirst()) {
            do {
                int column = mCursor.getInt(NUM_COLUMN_COLUMN);
                int row = mCursor.getInt(NUM_COLUMN_ROW);
                String count = mCursor.getString(NUM_COLUMN_COUNT);
                arr[column][row] = count;
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
                    COLUMN_COLUMN + " INTEGER, " +
                    COLUMN_ROW+ " INTEGER, " +
                    COLUMN_COUNT+ " TEXT);";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

}