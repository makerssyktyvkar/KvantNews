package com.example.kvantnews.ui.timetable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ChildrenDB {

    private static final String DATABASE_NAME = "children.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "children";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_LOGIN = "login";

    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_LOGIN = 1;
    public static int maxId = 0;

    private SQLiteDatabase mDataBase;

    public ChildrenDB(Context context){
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }

    public long insert(String login) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_LOGIN, login);
        return mDataBase.insert(TABLE_NAME, null, cv);
    }

    public void deleteAll()  {
        try{
            mDataBase.delete(TABLE_NAME, null, null);
        }catch (Exception e){
            //
        }
    }

    public ArrayList<String> selectAll() {
        try{
            Cursor mCursor = mDataBase.query(TABLE_NAME, null, null, null, null, null, null);
        }catch (Exception e){
            String query = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LOGIN+ " TEXT);";
            mDataBase.execSQL(query);
        }
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, null, null, null, null, null);

        ArrayList<String> arr = new ArrayList<>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                arr.add(mCursor.getString(NUM_COLUMN_LOGIN));
            } while (mCursor.moveToNext());
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
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LOGIN+ " TEXT);";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
