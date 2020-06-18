package com.example.kvantnews.ui.posts;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.example.kvantnews.R;

public class DeleteCourseNews extends Activity {
    private String id_news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id_news = getIntent().getStringExtra("id_news");
        try{
            new DeleteThreadNews().execute().get();
        }catch (Exception e){
            //
        }
        this.onBackPressed();
    }

    class DeleteThreadNews extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String res = "ok";
            try{
                String url = getString(R.string.main_host_dns) + "DeleteCourseNews.php";
                Document document = Jsoup.connect(url).data("id_news", String.valueOf(id_news)).post();
            }catch (Exception e){
                res = "fail";
            }
            return res;
        }

        @Override
        public void onPostExecute(String s){
            if(s.equals("ok")) {
                new CoursesNewsOfUserDB(getApplicationContext()).delete(id_news);
            }
        }
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        finish();
    }
}
