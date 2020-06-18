package com.example.kvantnews;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class QAndAActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<Pair<String, String>> listOfAnswers;
    private QuestionsAnswersDB questionsAnswersDB;
    private QuestionsAndAnswersAdapter questionsAndAnswersAdapter;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        lv = findViewById(R.id.questions);
        questionsAnswersDB = new QuestionsAnswersDB(this);
        listOfAnswers = questionsAnswersDB.selectAll();
        questionsAndAnswersAdapter = new QuestionsAndAnswersAdapter(this,listOfAnswers);
        lv.setAdapter(questionsAndAnswersAdapter);
        if(listOfAnswers.size() == 0){
            reloadPressed();
        }
        ((ImageButton)findViewById(R.id.reload_btn)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reload_btn:
                reloadPressed();
                break;
        }
    }

    class GetAnswersAndQuestions extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... args) {
            try {
                String url = getString(R.string.main_host_dns) + "ReturnFamousAnswer.php";
                Document document = Jsoup.connect(url).get();
                Elements element = document.select("li[class=answer-item]");
                questionsAnswersDB.deleteAll();
                for (int i = 0; i < element.size(); i++) {
                    String question = element.eq(i).select("p[class=question]").eq(0).text();
                    String answer = element.eq(i).select("p[class=answer]").eq(0).text();
                    questionsAnswersDB.insert(question, answer);
                }
            } catch (Exception e) {
                //
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            listOfAnswers = questionsAnswersDB.selectAll();
            questionsAndAnswersAdapter = new QuestionsAndAnswersAdapter(getApplicationContext(),listOfAnswers);
            lv.setAdapter(questionsAndAnswersAdapter);
            Toast.makeText(getApplicationContext(),R.string.is_ready, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException | InterruptedException e)          { e.printStackTrace(); }
        return false;
    }

    void reloadPressed(){
        if(isOnline()){
            Toast.makeText(this,R.string.please_wait, Toast.LENGTH_SHORT).show();
            new GetAnswersAndQuestions().execute();
        }else{
            Toast.makeText(this,R.string.no_internet_connection,Toast.LENGTH_SHORT).show();
        }
    }

    private static class QuestionsAnswersDB {

        private static final String DATABASE_NAME = "answers_questions.db";
        private static final int DATABASE_VERSION = 1;
        private static final String TABLE_NAME = "answers";

        private static final String COLUMN_ID = "id";
        private static final String COLUMN_QUESTION = "question";
        private static final String COLUMN_ANSWER = "answer";

        private static final int NUM_COLUMN_ID = 0;
        private static final int NUM_COLUMN_QUESTION = 1;
        private static final int NUM_COLUMN_ANSWER = 2;

        private SQLiteDatabase mDataBase;

        public QuestionsAnswersDB(Context context) {
            OpenHelper mOpenHelper = new OpenHelper(context);
            mDataBase = mOpenHelper.getWritableDatabase();
        }

        void insert(String question, String answer) {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_QUESTION, question);
            cv.put(COLUMN_ANSWER, answer);
            mDataBase.insert(TABLE_NAME, null, cv);
        }

        public void deleteAll() {
            mDataBase.execSQL("DELETE FROM " + TABLE_NAME);
        }


        public ArrayList<Pair<String, String>> selectAll() {
            @SuppressLint("Recycle") Cursor mCursor = mDataBase.query(TABLE_NAME, null, null, null, null, null, null);

            ArrayList<Pair<String, String>> arr = new ArrayList<>();
            mCursor.moveToFirst();
            if (!mCursor.isAfterLast()) {
                do {
                    String question = mCursor.getString(NUM_COLUMN_QUESTION);
                    String answer = mCursor.getString(NUM_COLUMN_ANSWER);
                    arr.add(new Pair<>(question, answer));
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
                        COLUMN_QUESTION+ " TEXT, " +
                        COLUMN_ANSWER + " TEXT);";
                db.execSQL(query);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                onCreate(db);
            }
        }

    }

    private static class QuestionsAndAnswersAdapter extends ArrayAdapter<Pair<String, String>> {
        private LayoutInflater inflater;
        private Context context;

        public QuestionsAndAnswersAdapter(Context context, ArrayList<Pair<String, String>> questions) {
            super(context, R.layout.question_adapter, questions);
            this.inflater = LayoutInflater.from(context);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final Pair<String,String> question = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.question_adapter, null);
            }
            assert question != null;
            ((TextView) convertView.findViewById(R.id.answer)).setText(question.second);
            ((TextView) convertView.findViewById(R.id.question)).setText(question.first);
            return convertView;
        }
    }
}
