package com.example.kvantnews.ui.news;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import com.example.kvantnews.R;

public class NewsFragment extends Fragment implements View.OnClickListener {
    private ArrayList<News> listNews = new ArrayList<>();
    private NewsDB newsBD;
    private MyNewsAdapter adapter;
    private ListView lv;
    private boolean is_thread = false;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_news, container, false);
        lv =  (ListView) root.findViewById(R.id.list_container);
        newsBD = new NewsDB(root.getContext());
        listNews = newsBD.selectAll();
        if(listNews.size() != 0){
            adapter = new MyNewsAdapter(getContext(), drawThreadNews());
            lv.setAdapter(adapter);
            is_thread = false;
        }else{
            reloadPressed();
        }
        ImageButton reload_btn = (ImageButton)root.findViewById(R.id.reload_btn);
        reload_btn.setOnClickListener(this);
        return root;
    }
    private News[] drawThreadNews (){
        News[] news = new News[listNews.size()];
        for(int i=0;i<listNews.size();i++){
            news[i] = listNews.get(i);
        }
        return news;
    }
    @SuppressLint("StaticFieldLeak")
    private class GetNews extends AsyncTask<String, Void, String> {
        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... args) {
            try {
                String url = getString(R.string.main_host_dns) + "ReturnNews.php";
                Document document = Jsoup.connect(url).maxBodySize(0).get();
                Elements element = document.select("li[class=news-item]");
                newsBD.deleteAll();
                listNews.clear();
                for(int i=0;i<element.size();i++){
                    String title = element.eq(i).select("h2[class=title]").eq(0).text();
                    String desc = element.eq(i).select("p[class=message]").eq(0).text();
                    String time = element.eq(i).select("p[class=time]").eq(0).text();
                    String linkImage;
                    try{
                        linkImage = element.eq(i).select("img").eq(0).attr("src").substring(24);
                    }catch (Exception e){
                        linkImage = "";
                    }
                    listNews.add(new News(title, desc, linkImage, time));
                    newsBD.insert(title,desc,linkImage,time);
                }
            } catch (Exception e) {
                //
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(listNews.size()!=0) {
                adapter = new MyNewsAdapter(getContext(), drawThreadNews());
                lv.setAdapter(adapter);
                is_thread = false;
            }
            Toast.makeText(getContext(), R.string.news_is_reloaded,Toast.LENGTH_SHORT).show();
        }
    }
    private void reloadPressed() {
        if(isOnline()) {
            if (!is_thread) {
                is_thread = true;
                new GetNews().execute();
                Toast.makeText(getContext(), R.string.please_wait, Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reload_btn:
                reloadPressed();
        }
    }
}
