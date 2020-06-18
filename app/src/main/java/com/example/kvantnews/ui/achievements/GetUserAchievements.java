package com.example.kvantnews.ui.achievements;

import android.content.Context;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.example.kvantnews.R;

public class GetUserAchievements extends AsyncTask<String, Void, Integer> {
    private String login;
    private AchievementsDB achievementDB;
    private  Context context;

    public GetUserAchievements(String login, Context context) {
        this.login = login;
        achievementDB= new AchievementsDB(context);
        this.context = context;
    }

    @Override
    protected Integer doInBackground(String... args) {
        try {
            String url = context.getResources().getString(R.string.main_host_dns) + "ReturnAchievements.php?login=" + login;
            Document document = Jsoup.connect(url).maxBodySize(0).get();
            Elements el = document.select("li");
            achievementDB.deleteAll();
            for (int i = 0; i < el.size(); i++) {
                String achievement = el.eq(i).select("p[class=achievement]").eq(0).text();
                String login = el.eq(i).select("p[class=login]").eq(0).text();
                achievementDB.insert(achievement, login);
            }
        } catch (Exception e) {
            //
        }
        return 1;
    }
}