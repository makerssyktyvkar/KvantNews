package com.example.kvantnews.ui.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kvantnews.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ActivityAdapter extends ArrayAdapter<Activity> {

    public ActivityAdapter(Context context, ArrayList<Activity> arr) {
        super(context, R.layout.activity_adapter, arr);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Activity activity = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_adapter, null);
        }
        assert activity != null;
        ((TextView) convertView.findViewById(R.id.title)).setText(activity.title);
        ((TextView) convertView.findViewById(R.id.message)).setText(String.valueOf(activity.message));
        ((TextView) convertView.findViewById(R.id.date)).setText(getTime(activity.time));
        if(activity.image != null){
            ((ImageView) convertView.findViewById(R.id.news_image)).setVisibility(View.VISIBLE);
            ((ImageView) convertView.findViewById(R.id.news_image)).setImageBitmap(activity.image);
        }
        else{
            ((ImageView) convertView.findViewById(R.id.news_image)).setVisibility(View.GONE);
        }
        return convertView;
    }

    private String getTime(int seconds){
        Date currentDate = new Date((long) seconds * 1000);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);
        return dateText + " " + timeText;
    }
}

