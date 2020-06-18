package com.example.kvantnews.ui.posts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class CoursesNewsOfUserDB {

    private static final String DATABASE_NAME = "student_course_news.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "course_news";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME_COURSE = "name_course";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_MESSAGE = "message";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_ADD_INFO = "addInfo";

    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_NAME_COURSE = 1;
    private static final int NUM_COLUMN_TITLE = 2;
    private static final int NUM_COLUMN_MESSAGE = 3;
    private static final int NUM_COLUMN_IMAGE = 4;
    private static final int NUM_COLUMN_ADD_INFO = 5;
    public static int maxId = 0;

    private SQLiteDatabase mDataBase;

    public CoursesNewsOfUserDB(Context context) {
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }

    public long insert(String id_news, String courseName, String title,String message,String image,String addInfo) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_ID, id_news);
        cv.put(COLUMN_NAME_COURSE, courseName);
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_MESSAGE, message);
        cv.put(COLUMN_IMAGE, image);
        cv.put(COLUMN_ADD_INFO, addInfo);
        return mDataBase.insert(TABLE_NAME, null, cv);
    }
    public long insert(courseNews courseName) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_ID, courseName.id_news);
        cv.put(COLUMN_NAME_COURSE, courseName.courseName);
        cv.put(COLUMN_TITLE, courseName.title);
        cv.put(COLUMN_MESSAGE, courseName.message);
        cv.put(COLUMN_IMAGE, courseName.imageString);
        cv.put(COLUMN_ADD_INFO, courseName.additionalInfo);
        return mDataBase.insert(TABLE_NAME, null, cv);
    }

    public int update(String id_news, String courseName, String title,String message,String image,String addInfo) {
        ContentValues cv=new ContentValues();
        maxId++;
        cv.put(COLUMN_NAME_COURSE, courseName);
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_MESSAGE, message);
        cv.put(COLUMN_IMAGE, image);
        cv.put(COLUMN_ADD_INFO, addInfo);
        return mDataBase.update(TABLE_NAME, cv, COLUMN_ID + " = ?",new String[] { id_news});
    }
    public int update(courseNews news) {
        ContentValues cv=new ContentValues();
        maxId++;
        cv.put(COLUMN_NAME_COURSE, news.courseName);
        cv.put(COLUMN_TITLE, news.title);
        cv.put(COLUMN_MESSAGE, news.message);
        cv.put(COLUMN_IMAGE, news.imageString);
        cv.put(COLUMN_ADD_INFO, news.additionalInfo);
        return mDataBase.update(TABLE_NAME, cv, COLUMN_ID + " = ?",new String[] { news.id_news});
    }

    public void deleteAll() {
        mDataBase.delete(TABLE_NAME, null, null);
    }

    public void delete(String id) {
        mDataBase.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { id });
    }



    public ArrayList<courseNews> selectAll() {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, null, null, null, null, null);

        ArrayList<courseNews> arr = new ArrayList<>();
        mCursor.moveToLast();
        if (!mCursor.isBeforeFirst()) {
            do {
                String id = mCursor.getString(NUM_COLUMN_ID);
                String courseName = mCursor.getString(NUM_COLUMN_NAME_COURSE);
                String title = mCursor.getString(NUM_COLUMN_TITLE);
                String message = mCursor.getString(NUM_COLUMN_MESSAGE);
                String image = mCursor.getString(NUM_COLUMN_IMAGE);
                String addInfo = mCursor.getString(NUM_COLUMN_ADD_INFO);
                arr.add(new courseNews(id, courseName,title,message,image,addInfo));
            } while (mCursor.moveToPrevious());
        }
        return arr;
    }
    public courseNews select(String id){
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, "id = ?", new String[] {id},
                null, null, null);
        mCursor.moveToFirst();
        String courseName = mCursor.getString(NUM_COLUMN_NAME_COURSE);
        String title = mCursor.getString(NUM_COLUMN_TITLE);
        String message = mCursor.getString(NUM_COLUMN_MESSAGE);
        String image = mCursor.getString(NUM_COLUMN_IMAGE);
        String addInfo = mCursor.getString(NUM_COLUMN_ADD_INFO);
        return new courseNews(id, courseName,title,message,image,addInfo);
    }

    private class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " TEXT, " +
                    COLUMN_NAME_COURSE+ " TEXT, " +
                    COLUMN_TITLE+ " TEXT, " +
                    COLUMN_MESSAGE + " TEXT, " +
                    COLUMN_IMAGE + " TEXT,"+
                    COLUMN_ADD_INFO+" TEXT);";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

}