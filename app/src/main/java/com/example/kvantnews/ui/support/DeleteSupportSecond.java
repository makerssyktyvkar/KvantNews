package com.example.kvantnews.ui.support;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.example.kvantnews.R;

public class DeleteSupportSecond extends Activity {
    private String id_support;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id_support = getIntent().getStringExtra("id_support");
        try{
            new DeleteSupportByItId().execute().get();
        }catch (Exception e){
            //
        }
        this.onBackPressed();
    }

    class DeleteSupportByItId extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String res = "ok";
            try{
                String url = getString(R.string.main_host_dns) + "DeleteSupportById.php";
                Document document = Jsoup.connect(url).data("id_support", String.valueOf(id_support)).post();
            }catch (Exception e){
                res = "fail";
            }
            return res;
        }

        @Override
        public void onPostExecute(String s){
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
