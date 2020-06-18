package com.example.kvantnews.ui.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kvantnews.R;

public class MyNewsAdapter extends ArrayAdapter<News> {

        public MyNewsAdapter(Context context, News[] arr) {
            super(context, R.layout.news_adapter, arr);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final News news = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_adapter, null);
            }

            assert news != null;
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

            return convertView;
        }
    }
