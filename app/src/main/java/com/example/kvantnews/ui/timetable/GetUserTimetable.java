package com.example.kvantnews.ui.timetable;

import android.content.Context;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.example.kvantnews.R;

public class GetUserTimetable extends AsyncTask<String, Void, Integer> {
    private String login;
    private TimetableDB timetableDB;
    private Context context;

    public GetUserTimetable(String login, Context context) {
        this.login = login;
        this.context = context;
        timetableDB= new TimetableDB(context);
    }

    @Override
    protected Integer doInBackground(String... args) {
        String url = context.getResources().getString(R.string.main_host_dns) + "ReturnTimetable.php?login=" + login;
        Document document = null;
        try {
            document = Jsoup.connect(url).maxBodySize(0).get();
            Elements element = document.select("li[class=timetable-item]");
            timetableDB.delete(login);
            for(int i=0;i<element.size();i++){
                String course = element.eq(i).select("p[class=name_course]").eq(0).text();
                String group = element.eq(i).select("p[class=name_group]").eq(0).text();
                String monday = element.eq(i).select("p[class=monday]").eq(0).text();
                String tuesday = element.eq(i).select("p[class=tuesday]").eq(0).text();
                String wednesday = element.eq(i).select("p[class=wednesday]").eq(0).text();
                String thursday = element.eq(i).select("p[class=thursday]").eq(0).text();
                String friday = element.eq(i).select("p[class=friday]").eq(0).text();
                String saturday = element.eq(i).select("p[class=saturday]").eq(0).text();
                String sunday = element.eq(i).select("p[class=sunday]").eq(0).text();
                timetableDB.insert(course, group, monday, tuesday, wednesday, thursday, friday, saturday, sunday,login);
            }
        } catch (Exception e) {
            //
        }
        return 1;
    }
}