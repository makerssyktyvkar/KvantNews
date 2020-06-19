package com.example.kvantnews.ui.activities;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kvantnews.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Objects;

public class ActivitiesActivity extends AppCompatActivity implements View.OnClickListener {
    ListView activitiesContainer;
    int type, timeType, activitiesCount = 0, maxCount;
    ActivityAdapter activityAdapter;
    ActivitiesDB activitiesDB;
    LoadingActivity loadingActivity;
    ArrayList<Activity> activities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        type = getIntent().getIntExtra("type", 1);
        timeType = getIntent().getIntExtra("time_type", 0);
        maxCount = Integer.parseInt(Objects.requireNonNull(getIntent().getStringExtra("max_count")));
        //Toast.makeText(this, "type: " + type + "timeType: " + timeType, Toast.LENGTH_SHORT).show();

        activitiesContainer = (ListView) findViewById(R.id.activities_container);

        String activityType = null, activityLength = null;
        switch (type){
            case 1:
                activityType = getString(R.string.competitions);
                break;
            case 2:
                activityType = getString(R.string.learning);
                break;
            case 3:
                activityType = getString(R.string.events);
                break;
        }
        switch (timeType){
            case 0:
                activityLength = getString(R.string.this_week);
                break;
            case 1:
                activityLength = getString(R.string.this_month);
                break;
            case 2:
                activityLength = getString(R.string.this_year);
                break;
        }
        ((TextView)findViewById(R.id.type_of_activity)).setText(activityType);
        ((TextView)findViewById(R.id.length_of_activity)).setText(activityLength);

        activitiesDB = new ActivitiesDB(this);
        activityAdapter = new ActivityAdapter(this, activities/*activitiesDB.selectSpecials(String.valueOf(type), timeType)*/);
        activitiesContainer.setAdapter(activityAdapter);
        loadingActivity = new LoadingActivity(this);
        loadingActivity.execute();
        activitiesContainer.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean loadMore = firstVisibleItem + visibleItemCount + 2 >= totalItemCount;

                if ((maxCount > activitiesCount) && loadMore && (loadingActivity.getStatus() == AsyncTask.Status.FINISHED || AsyncTask.Status.PENDING == loadingActivity.getStatus())) {
                    loadingActivity = new LoadingActivity(getApplicationContext());
                    loadingActivity.execute();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {

    }

    class LoadingActivity extends AsyncTask<String, String, Activity>{
        Context context;

        LoadingActivity(Context context){
            this.context = context;
        }

        @Override
        protected Activity doInBackground(String... strings) {
            Activity res = null;
            try {
                String url = context.getResources().getString(R.string.main_host_dns) + "ReturnActivity.php";
                Document document = Jsoup.connect(url).maxBodySize(0).
                        data("type", String.valueOf(type)).
                        data("type_time", String.valueOf(timeType + 1)).
                        data("pos", String.valueOf(activitiesCount)).post();
                Elements elements = document.select("li[class=news-item]").eq(0);
                String title = elements.select("h2[class=title]").eq(0).text();
                String message = elements.select("p[class=message]").eq(0).text();
                String time = elements.select("p[class=time]").eq(0).text();
                String linkImage;
                try{
                    linkImage = elements.select("img").eq(0).attr("src").substring(24);
                }catch (Exception e){
                    linkImage = "";
                }
                res = new Activity(title, message, linkImage, Integer.parseInt(time), String.valueOf(type));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(Activity activity) {
            if(activity != null) {
                activityAdapter.add(activity);
                activityAdapter.notifyDataSetChanged();
                int index = activitiesContainer.getFirstVisiblePosition();
                int top = (activitiesContainer.getChildAt(0) == null) ? 0 : activitiesContainer.getChildAt(0).getTop();
                activitiesContainer.setSelectionFromTop(index, top);
                activitiesCount++;
            }
        }
    }
}
