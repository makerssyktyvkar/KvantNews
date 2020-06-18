package com.example.kvantnews.ui.posts;

import android.content.Context;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.example.kvantnews.R;

public class GetCoursesNews extends AsyncTask<String, Void, Integer> {
    private String login;
    private CoursesNewsOfUserDB coursesNewsOfUserDB;
    Context context;

    public GetCoursesNews(String login, Context context) {
        this.login = login;
        coursesNewsOfUserDB = new CoursesNewsOfUserDB(context);
        this.context = context;
    }

    @Override
    protected Integer doInBackground(String... args) {
        try {
            String url = context.getResources().getString(R.string.main_host_dns) + "ReturnCoursesNews.php?login=" + login;
            Document document = Jsoup.connect(url).maxBodySize(0).get();
            Elements element = document.select("li[class=news-item]");
            coursesNewsOfUserDB.deleteAll();
            for (int i = element.size() - 1; i >= 0; i--) {
                String id_news = element.eq(i).select("p[class=id_news]").eq(0).text();
                String name = element.eq(i).select("p[class=course_name]").eq(0).text();
                String title = element.eq(i).select("h2[class=title]").eq(0).text();
                String message = element.eq(i).select("p[class=message]").eq(0).text();
                String time = element.eq(i).select("p[class=time]").eq(0).text();
                String linkImage;
                try{
                    linkImage = element.eq(i).select("img").eq(0).attr("src").substring(24);
                }catch (Exception e){
                    linkImage = "";
                }
                coursesNewsOfUserDB.insert(id_news, name, title,message,linkImage,time);
            }
        } catch (Exception e) {
            //
        }
        return 1;
    }
}