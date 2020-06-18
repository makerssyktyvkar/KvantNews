package com.example.kvantnews.ui.achievements;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kvantnews.R;

public class AchievementAdapter extends ArrayAdapter<Achievement> {

    public AchievementAdapter(Context context, Achievement[] arr) {
        super(context, R.layout.achievement_adapter, arr);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Achievement achievement = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.achievement_adapter, null);
        }
        assert achievement != null;
        ((TextView) convertView.findViewById(R.id.achievement_tv)).setText(achievement.achievement);
        return convertView;
    }
}