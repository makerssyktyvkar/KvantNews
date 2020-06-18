package com.example.kvantnews;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import java.util.ArrayList;

public class CoursesOfUserDB {

    private static final String DATABASE_NAME = "courses_db.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "courses";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";

    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_NAME = 1;

    private SQLiteDatabase mDataBase;

    public CoursesOfUserDB(Context context) {
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }

    void insert(int id, String name) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, id);
        cv.put(COLUMN_NAME, name);
        mDataBase.insert(TABLE_NAME, null, cv);
    }

    public void deleteAll() {
        mDataBase.execSQL("DELETE FROM " + TABLE_NAME);
    }


    public ArrayList<Pair<Integer, String>> selectAll() {
        @SuppressLint("Recycle") Cursor mCursor = mDataBase.query(TABLE_NAME, null, null, null, null, null, null);

        ArrayList<Pair<Integer, String>> arr = new ArrayList<>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                String name = mCursor.getString(NUM_COLUMN_NAME);
                int id = Integer.parseInt(mCursor.getString(NUM_COLUMN_ID));
                arr.add(new Pair<>(id, name));
            } while (mCursor.moveToNext());
        }
        return arr;
    }

    public int getID(String courseName){
        ArrayList<Pair<Integer,String>> tmp = this.selectAll();
        int i = 0;
        while(!tmp.get(i).second.equals(courseName)){
            i++;
            if(i >= tmp.size()) break;
        }
        return tmp.get(i).first;
    }

    private class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER, " +
                    COLUMN_NAME+ " TEXT);";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

}