package com.example.kvantnews.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
//import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kvantnews.R;

public class ActivitiesActivity extends AppCompatActivity implements View.OnClickListener {
    ListView activitiesContainer;
    int type, timeType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);

        type = getIntent().getIntExtra("type", 1);
        timeType = getIntent().getIntExtra("time_type", 0);
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
    }

    @Override
    public void onClick(View v) {

    }
}
