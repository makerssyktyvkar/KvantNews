package com.example.kvantnews.ui.timetable;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import com.example.kvantnews.R;
import com.example.kvantnews.User;

public class TimetableFragment extends Fragment implements View.OnClickListener {
    private String login;
    private int type;
    private ArrayList<String> childrenNames;
    private String mainChild;
    private Button mainChildName;
    private ArrayList<Timetable> TimetableList = new ArrayList<>();
    private TimetableDB timetableDB;
    private TimetableAdapter adapter;
    private ListView lv;
    private boolean is_thread = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_timetable, container, false);
        lv = root.findViewById(R.id.timetable);
        mainChildName = (Button)root.findViewById(R.id.main_child_name);
        User user = new User(getContext());
        login = user.getLogin();
        type = user.getType();
        if(type == 3){
            if(isOnline()) {
                loaderChildren();
                childrenNames = new ChildrenDB(getContext()).selectAll();
                if(childrenNames.size() == 0){
                    mainChildName.setText(R.string.you_havent_children);
                }else{
                    mainChild = childrenNames.get(0);
                    mainChildName.setText(mainChild);
                    loader(mainChild);
                    showInformationAbout(mainChild);
                }
            }else{
                childrenNames = new ChildrenDB(getContext()).selectAll();
                if(childrenNames.size() == 0){
                    mainChildName.setText(R.string.you_havent_children);
                }else{
                    mainChild = childrenNames.get(0);
                    mainChildName.setText(mainChild);
                    showInformationAbout(mainChild);
                }
            }
        }else if(type == 2 || type == 4){
            mainChildName.getLayoutParams().height = 0;
            if(isOnline()){
                loader(login);
            }
            showInformationAbout(login);
        }
        ImageButton reload_btn = (ImageButton)root.findViewById(R.id.reload_btn);
        reload_btn.setOnClickListener(this);
        mainChildName.setOnClickListener(this);
        return root;
    }

    private void showInformationAbout(String name){
        timetableDB = new TimetableDB(getContext());
        TimetableList = timetableDB.selectAll(name);
        if (TimetableList.size()!=0){
            adapter = new TimetableAdapter(getContext(), drawThreadTimetable());
            lv.setAdapter(adapter);
        }else{
            loader(name);
        }
    }

    private Timetable[] drawThreadTimetable(){
        Timetable[] timetable = new Timetable[TimetableList.size()];
        for(int i=0;i<TimetableList.size();i++){
            timetable[i] = TimetableList.get(i);
        }
        return timetable;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reload_btn:
                if(type == 2 || type == 4) {
                    loader();
                }else if(type == 3){
                    loaderChildren();
                    if(childrenNames.size()!=0){
                        mainChild = childrenNames.get(0);
                        mainChildName.setText(mainChild);
                        loader(mainChild);
                    }
                }
                break;
            case R.id.main_child_name:
                PopupMenu popupMenu = new PopupMenu(getContext(),v);
                for(int i=0;i<childrenNames.size();i++) {
                    popupMenu.getMenu().add(0, i, 0, childrenNames.get(i));
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        mainChild = childrenNames.get(item.getItemId());
                        mainChildName.setText(mainChild);
                        loader(mainChild);
                        return false;
                    }
                });
                popupMenu.show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetTimetable extends AsyncTask<String, Void, String>{
        private String name;
        GetTimetable(String name){
            this.name = name;
        }
        @Override
        protected String doInBackground(String... strings) {
            String url = getString(R.string.main_host_dns) + "ReturnTimetable.php?login="+name;
            Document document = null;
            try {
                document = Jsoup.connect(url).maxBodySize(0).get();
                Elements element = document.select("li[class=timetable-item]");
                timetableDB.delete(name);
                TimetableList.clear();
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
                    TimetableList.add(new Timetable(course, group, monday, tuesday, wednesday, thursday, friday, saturday, sunday));
                    timetableDB.insert(course, group, monday, tuesday, wednesday, thursday, friday, saturday, sunday, login);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            adapter = new TimetableAdapter(getContext(), drawThreadTimetable());
            lv.setAdapter(adapter);
            is_thread = false;
        }

    }
    private void loader() {
        if (isOnline()) {
            if (!is_thread) {
                is_thread=true;
                new GetTimetable(login).execute();
            }
        }else{
            Toast.makeText(getContext(),R.string.no_internet_connection,Toast.LENGTH_SHORT).show();
        }
    }
    private void loader(String name) {
        if (isOnline()) {
            if (!is_thread) {
                is_thread=true;
                new GetTimetable(name).execute();
            }
        }else{
            Toast.makeText(getContext(),R.string.no_internet_connection,Toast.LENGTH_SHORT).show();
        }
    }

    void loaderChildren(){
        try {
            new GetChildren(login, getContext()).execute().get();
        }catch (Exception e){
            //
        }
        childrenNames = new ChildrenDB(getContext()).selectAll();
    }

    private boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException | InterruptedException e)          { e.printStackTrace(); }
        return false;
    }
}
