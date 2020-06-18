package com.example.kvantnews.ui.posts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.kvantnews.R;
import com.example.kvantnews.User;

public class MyCourseNewsAdapter extends ArrayAdapter<courseNews> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<courseNews> listNews;
    private int type;
    private Context context;

    MyCourseNewsAdapter(Context context, int resource, ArrayList<courseNews> products) {
        super(context, resource, products);
        this.listNews = products;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.type = new User(getContext()).getType();
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final courseNews news = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.course_news_adapter, null);
        }

        assert news != null;
        ((TextView) convertView.findViewById(R.id.course_name)).setText(news.courseName);
        ((TextView) convertView.findViewById(R.id.title)).setText(news.title);
        ((TextView) convertView.findViewById(R.id.message)).setText(String.valueOf(news.message));
        ((TextView) convertView.findViewById(R.id.date)).setText(String.valueOf(news.additionalInfo));
        if(news.image != null){
            ((ImageView) convertView.findViewById(R.id.news_image)).setVisibility(View.VISIBLE);
            ((ImageView) convertView.findViewById(R.id.news_image)).setImageBitmap(news.image);
        }
        else{
            ((ImageView) convertView.findViewById(R.id.news_image)).setVisibility(View.GONE);
        }
        if(type == 4){
            ((ImageButton) convertView.findViewById(R.id.delete_course_news_btn)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), DeleteCourseNews.class);
                    intent.putExtra("id_news", (String)String.valueOf(news.id_news));
                    ((Activity) context).startActivityForResult(intent, 42);
                }
            });
            ((ImageButton) convertView.findViewById(R.id.change_course_news_btn)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ChangeCourseNews.class);
                    intent.putExtra("id_news", (String)String.valueOf(news.id_news));
                    ((Activity) context).startActivityForResult(intent, 42);
                }
            });
        }else{
            ((ImageButton) convertView.findViewById(R.id.delete_course_news_btn)).setVisibility(View.GONE);
            ((ImageButton) convertView.findViewById(R.id.change_course_news_btn)).setVisibility(View.GONE);
        }
        return convertView;
    }

}