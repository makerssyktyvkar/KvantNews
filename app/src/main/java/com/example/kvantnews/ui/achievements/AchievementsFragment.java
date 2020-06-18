package com.example.kvantnews.ui.achievements;

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
import com.example.kvantnews.ui.timetable.GetChildren;

public class AchievementsFragment extends Fragment implements View.OnClickListener {
    private ArrayList<Achievement> listAchievements = new ArrayList<>();
    private AchievementsDB achievementsDB;
    private AchievementAdapter adapter;
    private ListView lv;
    private String login;
    private Button change_childName_btn;
    private String mainChild = "";
    private int type;
    private ArrayList<String> children;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_achievements, container, false);
        login = new User(getActivity()).getLogin();
        type = new User(getActivity()).getType();
        lv =  (ListView) root.findViewById(R.id.list_container);
        ImageButton reload_btn = (ImageButton)root.findViewById(R.id.reload_btn);
        change_childName_btn = (Button)root.findViewById(R.id.main_child_name);
        achievementsDB = new AchievementsDB(root.getContext());
        if(type == 2){
            mainChild = login;
            change_childName_btn.getLayoutParams().height = 0;
        }else if(type == 3 || type == 4){
            children = achievementsDB.selectUniqueChildren();
            if(children.size()==0){
                change_childName_btn.setText(R.string.havent_achievements);
                change_childName_btn.setEnabled(false);
            }else{
                mainChild = children.get(0);
                change_childName_btn.setText(mainChild);
            }
            change_childName_btn.setOnClickListener(this);
        }
        listAchievements = achievementsDB.selectAll(mainChild);
        if(listAchievements.size()!=0){
            adapter = new AchievementAdapter(getContext(), drawThreadNews());
            lv.setAdapter(adapter);
        }
        if(isOnline()) reloadPressed();
        reload_btn.setOnClickListener(this);
        return root;
    }

    private Achievement[] drawThreadNews (){
        Achievement[] achievements = new Achievement[listAchievements.size()];
        for(int i=0;i<listAchievements.size();i++){
            achievements[i] = listAchievements.get(i);
        }
        return achievements;
    }

    private void reloadPressed() {
        if(isOnline()) {
            new GetUserAchievements().execute();
        }else{
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
        }
    }

    public class GetUserAchievements extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... args) {
            try {
                String url = getString(R.string.main_host_dns) + "ReturnAchievements.php?login=" + login;
                Document document = Jsoup.connect(url).maxBodySize(0).get();
                Elements el = document.select("li");
                achievementsDB.deleteAll();
                for (int i = 0; i < el.size(); i++) {
                    String achievement = el.eq(i).select("p[class=achievement]").eq(0).text();
                    String login = el.eq(i).select("p[class=login]").eq(0).text();
                    achievementsDB.insert(achievement, login);
                }
            } catch (Exception e) {
                //
            }
            return 1;
        }
        @Override
        protected void onPostExecute(Integer s){
            super.onPostExecute(s);
            listAchievements = achievementsDB.selectAll(mainChild);
            if(type == 3 || type == 4){
                try{
                    new GetChildren(login, getContext()).execute().get();
                }catch (Exception e) {
                    //
                }
                children = achievementsDB.selectUniqueChildren();
                if(children.size()==0){
                    change_childName_btn.setText(R.string.havent_achievements);
                    change_childName_btn.setEnabled(false);
                }else{
                    mainChild = children.get(0);
                    change_childName_btn.setEnabled(true);
                    change_childName_btn.setText(mainChild);
                }
            }

            listAchievements = achievementsDB.selectAll(mainChild);
            if(listAchievements.size()!=0){
                adapter = new AchievementAdapter(getContext(), drawThreadNews());
                lv.setAdapter(adapter);
            }
        }
    }

    boolean isOnline() {
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
                break;
            case R.id.main_child_name:
                PopupMenu popupMenu = new PopupMenu(getContext(),v);
                for(int i=0;i<children.size();i++) {
                    popupMenu.getMenu().add(0,i,0,children.get(i));
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        mainChild = children.get(item.getItemId());
                        change_childName_btn.setText(mainChild);
                        listAchievements = achievementsDB.selectAll(mainChild);
                        if(listAchievements.size() == 0){
                            reloadPressed();
                            return false;
                        }
                        adapter = new AchievementAdapter(getContext(), drawThreadNews());
                        lv.setAdapter(adapter);
                        return false;
                    }
                });
                popupMenu.show();
        }
    }
}
