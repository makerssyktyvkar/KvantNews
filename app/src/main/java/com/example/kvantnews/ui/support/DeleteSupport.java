package com.example.kvantnews.ui.support;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.example.kvantnews.R;

class DeleteSupport extends AppCompatActivity{
    private String id_support;
    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("my_tag","fail");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_course_news);
        info = findViewById(R.id.info_tv);
        /*try {
            id_support = getIntent().getStringExtra("id_support");
            try {
                new DeleteSupportByID().execute().get();
            } catch (Exception e) {
                //
            }
        }catch (Exception e){
            id_support = "fail";
            info.setText(id_support);
        }*/
        info.setText("fail");

    }

    class DeleteSupportByID extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... args) {
            try {
                String url = getString(R.string.main_host_dns) + "DeleteSupportById.php?id=" + id_support;
                Document document = Jsoup.connect(url).get();
            } catch (Exception e) {
                return "fail";
            }
            return "ok";
        }

        @Override
        public void onPostExecute(String s){
            super.onPostExecute(s);
            if(s.equals("ok")) {
                new SupportsDB(getApplicationContext()).delete(Long.parseLong(id_support));
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
