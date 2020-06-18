package com.example.kvantnews.ui.timetable;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class TimetableDB {

    private static final String DATABASE_NAME = "timetable.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "timetable";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_COURSE = "course";
    private static final String COLUMN_GROUP = "groupp";
    private static final String COLUMN_MONDAY = "monday";
    private static final String COLUMN_TUESDAY = "tuesday";
    private static final String COLUMN_WEDNESDAY = "wednesday";
    private static final String COLUMN_THURSDAY = "thursday";
    private static final String COLUMN_FRIDAY = "friday";
    private static final String COLUMN_SATURDAY = "saturday";
    private static final String COLUMN_SUNDAY = "sunday";
    private static final String COLUMN_LOGIN = "login";

    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_COURSE = 1;
    private static final int NUM_COLUMN_GROUP = 2;
    private static final int NUM_COLUMN_MONDAY = 3;
    private static final int NUM_COLUMN_TUESDAY = 4;
    private static final int NUM_COLUMN_WEDNESDAY = 5;
    private static final int NUM_COLUMN_THURSDAY = 6;
    private static final int NUM_COLUMN_FRIDAY = 7;
    private static final int NUM_COLUMN_SATURDAY = 8;
    private static final int NUM_COLUMN_SUNDAY = 9;
    private static final int NUM_COLUMN_LOGIN = 10;
    public static int maxId = 0;

    private SQLiteDatabase mDataBase;

    public TimetableDB(Context context){
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }

    public long insert(String course, String group, String monday, String tuesday, String wednesday, String thursday, String friday, String saturday, String sunday, String login) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_COURSE, course);
        cv.put(COLUMN_GROUP, group);
        cv.put(COLUMN_MONDAY, monday);
        cv.put(COLUMN_TUESDAY, tuesday);
        cv.put(COLUMN_WEDNESDAY, wednesday);
        cv.put(COLUMN_THURSDAY,thursday);
        cv.put(COLUMN_FRIDAY,friday);
        cv.put(COLUMN_SATURDAY,saturday);
        cv.put(COLUMN_SUNDAY,sunday);
        cv.put(COLUMN_LOGIN,login);
        return mDataBase.insert(TABLE_NAME, null, cv);
    }

    public int update(String course, String group, String monday, String tuesday, String wednesday, String thursday, String friday, String saturday, String sunday, String login){
        ContentValues cv=new ContentValues();
        maxId++;
        cv.put(COLUMN_COURSE, course);
        cv.put(COLUMN_GROUP, group);
        cv.put(COLUMN_MONDAY, monday);
        cv.put(COLUMN_TUESDAY, tuesday);
        cv.put(COLUMN_WEDNESDAY, wednesday);
        cv.put(COLUMN_THURSDAY,thursday);
        cv.put(COLUMN_FRIDAY,friday);
        cv.put(COLUMN_SATURDAY,saturday);
        cv.put(COLUMN_SUNDAY,sunday);
        cv.put(COLUMN_LOGIN,login);
        return mDataBase.update(TABLE_NAME, cv, COLUMN_ID + " = ?",new String[] { String.valueOf(maxId)});
    }

    public void deleteAll()  {
        try{
            mDataBase.delete(TABLE_NAME, null, null);
        }catch (Exception e){
            //
        }
    }

    public void delete(String login) {
        try {
            mDataBase.delete(TABLE_NAME, COLUMN_LOGIN + " = ?", new String[] { login });
        }catch (Exception e){
            String query = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_COURSE+ " TEXT, " +
                    COLUMN_GROUP + " TEXT, " +
                    COLUMN_MONDAY + " TEXT, " +
                    COLUMN_TUESDAY + " TEXT, " +
                    COLUMN_WEDNESDAY + " TEXT, " +
                    COLUMN_THURSDAY + " TEXT, " +
                    COLUMN_FRIDAY + " TEXT, " +
                    COLUMN_SATURDAY + " TEXT, " +
                    COLUMN_SUNDAY + " TEXT, " +
                    COLUMN_LOGIN + " TEXT);";
            mDataBase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            mDataBase.execSQL(query);
        }

    }

    public ArrayList<Timetable> selectAll(String login) {
        @SuppressLint("Recycle") Cursor mCursor = mDataBase.query(TABLE_NAME, null, null, null, null, null, null);

        ArrayList<Timetable> arr = new ArrayList<>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                String log1 = mCursor.getString(NUM_COLUMN_LOGIN);
                if(!log1.equals(login)) continue;
                long id = mCursor.getLong(NUM_COLUMN_ID);
                String course = mCursor.getString(NUM_COLUMN_COURSE);
                String group = mCursor.getString(NUM_COLUMN_GROUP);
                String monday = mCursor.getString(NUM_COLUMN_MONDAY);
                String tuesday = mCursor.getString(NUM_COLUMN_TUESDAY);
                String wednesday = mCursor.getString(NUM_COLUMN_WEDNESDAY);
                String thursday = mCursor.getString(NUM_COLUMN_THURSDAY);
                String friday = mCursor.getString(NUM_COLUMN_FRIDAY);
                String saturday = mCursor.getString(NUM_COLUMN_SATURDAY);
                String sunday = mCursor.getString(NUM_COLUMN_SUNDAY);
                arr.add(new Timetable(course, group, monday, tuesday, wednesday, thursday, friday, saturday, sunday));
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
                    COLUMN_COURSE+ " TEXT, " +
                    COLUMN_GROUP + " TEXT, " +
                    COLUMN_MONDAY + " TEXT, " +
                    COLUMN_TUESDAY + " TEXT, " +
                    COLUMN_WEDNESDAY + " TEXT, " +
                    COLUMN_THURSDAY + " TEXT, " +
                    COLUMN_FRIDAY + " TEXT, " +
                    COLUMN_SATURDAY + " TEXT, " +
                    COLUMN_SUNDAY + " TEXT, " +
                    COLUMN_LOGIN + " TEXT);";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
