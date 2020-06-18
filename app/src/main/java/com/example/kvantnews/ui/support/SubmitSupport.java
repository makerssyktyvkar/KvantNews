package com.example.kvantnews.ui.support;

import android.content.Context;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.example.kvantnews.R;

public class SubmitSupport extends AsyncTask<String, Void, Integer> {
    private String login, question;
    private SupportsDB supportDB;
    private Context context;

    public SubmitSupport(String login, String question, Context context) {
        this.login = login;
        this.question = question;
        supportDB= new SupportsDB(context);
        this.context = context;
    }

    @Override
    protected Integer doInBackground(String... args) {
        try {
            String url = context.getResources().getString(R.string.main_host_dns) + "AddNewSupport.php?login=" +
                    login + "&question=" + question;
            Document document = Jsoup.connect(url).get();
        } catch (Exception e) {
            //
        }
        return 1;
    }
}