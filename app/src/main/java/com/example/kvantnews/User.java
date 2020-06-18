package com.example.kvantnews;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import java.util.ArrayList;

public class User {

    public static String UniqueID = "null";

    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "user";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_LOGIN = "login";
    private static final String COLUMN_TYPE = "type";

    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_LOGIN = 1;
    private static final int NUM_COLUMN_TYPE = 2;

    private SQLiteDatabase mDataBase;

    public User(Context context) {
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }

    void insert(String login, int type) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_LOGIN, login);
        cv.put(COLUMN_TYPE, type);
        mDataBase.insert(TABLE_NAME, null, cv);
    }

    public void deleteAll() {
        mDataBase.execSQL("DELETE FROM " + TABLE_NAME);
    }


    ArrayList<Pair<String, Integer>> selectAll() {
        @SuppressLint("Recycle") Cursor mCursor = mDataBase.query(TABLE_NAME, null, null, null, null, null, null);

        ArrayList<Pair<String, Integer>> arr = new ArrayList<>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                String login = mCursor.getString(NUM_COLUMN_LOGIN);
                int type = Integer.parseInt(mCursor.getString(NUM_COLUMN_TYPE));
                arr.add(new Pair<>(login, type));
            } while (mCursor.moveToNext());
        }
        return arr;
    }
    public String getLogin(){
        return this.selectAll().get(0).first;
    }
    public int getType(){
        return this.selectAll().get(0).second;
    }

    private class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LOGIN+ " TEXT, " +
                    COLUMN_TYPE + " INTEGER);";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}