package com.example.kvantnews.ui.support;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SupportsDB {

    private static final String DATABASE_NAME = "supports.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "supports";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ANSWER = "answer";
    private static final String COLUMN_QUESTION = "question";

    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_ANSWER = 1;
    private static final int NUM_COLUMN_QUESTION = 2;
    public static int maxId = 0;

    private SQLiteDatabase mDataBase;

    public SupportsDB(Context context) {
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }

    public long insert(long id, String answer, String question) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_ID, id);
        cv.put(COLUMN_ANSWER, answer);
        cv.put(COLUMN_QUESTION, question);
        return mDataBase.insert(TABLE_NAME, null, cv);
    }

    public int update(long id, String answer, String question) {
        ContentValues cv=new ContentValues();
        maxId++;
        cv.put(COLUMN_ID, id);
        cv.put(COLUMN_ANSWER, answer);
        cv.put(COLUMN_QUESTION, question);
        return mDataBase.update(TABLE_NAME, cv, COLUMN_ID + " = ?",new String[] { String.valueOf(maxId)});
    }

    public void deleteAll() {
        mDataBase.delete(TABLE_NAME, null, null);
    }

    public void delete(long id) {
        mDataBase.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }



    public ArrayList<Support> selectAll() {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, null, null, null, null, null);

        ArrayList<Support> arr = new ArrayList<>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                long id = mCursor.getLong(NUM_COLUMN_ID);
                String answer = mCursor.getString(NUM_COLUMN_ANSWER);
                String question = mCursor.getString(NUM_COLUMN_QUESTION);
                arr.add(new Support(id,answer,question));
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
                    COLUMN_ID + " INTEGER, " +
                    COLUMN_ANSWER+ " TEXT, " +
                    COLUMN_QUESTION+ " TEXT);";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

}