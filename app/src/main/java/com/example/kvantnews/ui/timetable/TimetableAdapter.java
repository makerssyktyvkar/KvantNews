package com.example.kvantnews.ui.timetable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kvantnews.R;

public class TimetableAdapter extends ArrayAdapter<Timetable> {

    TimetableAdapter(Context context, Timetable[] arr){
        super(context, R.layout.timetable_adapter, arr);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Timetable timetable = getItem(position);

        assert timetable != null;
        String monday = timetable.monday;
        String tuesday = timetable.tuesday;
        String wednesday = timetable.wednesday;
        String thursday = timetable.thursday;
        String friday = timetable.friday;
        String saturday = timetable.saturday;
        String sunday = timetable.sunday;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.timetable_adapter, null);
        }

        ((TextView)convertView.findViewById(R.id.course_name)).setText("Курс: " + timetable.course);
        ((TextView)convertView.findViewById(R.id.groupp)).setText("Группа: " + timetable.group);
        ((TextView)convertView.findViewById(R.id.monday)).setText(monday.equals("")? null: "Понедельник: "+monday);
        if(monday.equals("")) ((TextView)convertView.findViewById(R.id.monday)).setHeight(0);
        ((TextView)convertView.findViewById(R.id.tuesday)).setText(tuesday.equals("")? null: "Вторник: " + tuesday);
        if(tuesday.equals("")) ((TextView)convertView.findViewById(R.id.tuesday)).setHeight(0);
        ((TextView)convertView.findViewById(R.id.wednesday)).setText(wednesday.equals("")? null : "Среда: " + wednesday);
        if(wednesday.equals("")) ((TextView)convertView.findViewById(R.id.wednesday)).setHeight(0);
        ((TextView)convertView.findViewById(R.id.thursday)).setText(thursday.equals("")? null: "Четверг: " + thursday);
        if(thursday.equals("")) ((TextView)convertView.findViewById(R.id.thursday)).setHeight(0);
        ((TextView)convertView.findViewById(R.id.friday)).setText(friday.equals("")? null: "Пятница: " + friday);
        if(friday.equals("")) ((TextView)convertView.findViewById(R.id.friday)).setHeight(0);
        ((TextView)convertView.findViewById(R.id.saturday)).setText(saturday.equals("")? null: "Суббота: " + saturday);
        if(saturday.equals("")) ((TextView)convertView.findViewById(R.id.saturday)).setHeight(0);
        ((TextView)convertView.findViewById(R.id.sunday)).setText(sunday.equals("")? null: "Воскресенье: " + sunday);
        if(sunday.equals("")) ((TextView)convertView.findViewById(R.id.sunday)).setHeight(0);

        return convertView;
    }
}
