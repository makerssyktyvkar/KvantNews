package com.example.kvantnews.ui.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.kvantnews.R;

public class ActivitiesFragment extends Fragment implements View.OnClickListener {
    View b1,b2,b3;
    int pos = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_activities, container, false);

        b1 = root.findViewById(R.id.selected_period_bar_btn1);
        b2 = root.findViewById(R.id.selected_period_bar_btn2);
        b3 = root.findViewById(R.id.selected_period_bar_btn3);
        setBar();

        ((Button)root.findViewById(R.id.this_week_btn)).setOnClickListener(this);
        ((Button)root.findViewById(R.id.this_month_btn)).setOnClickListener(this);
        ((Button)root.findViewById(R.id.this_year_btn)).setOnClickListener(this);

        return root;
    }

    private void setBar(){
        switch (pos){
            case 0:
                b1.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red));
                b2.setBackgroundColor(Color.TRANSPARENT);
                b3.setBackgroundColor(Color.TRANSPARENT);
                break;
            case 1:
                b1.setBackgroundColor(Color.TRANSPARENT);
                b2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red));
                b3.setBackgroundColor(Color.TRANSPARENT);
                break;
            case 2:
                b1.setBackgroundColor(Color.TRANSPARENT);
                b2.setBackgroundColor(Color.TRANSPARENT);
                b3.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.this_week_btn:
                pos = 0;
                setBar();
                break;
            case R.id.this_month_btn:
                pos = 1;
                setBar();
                break;
            case R.id.this_year_btn:
                pos = 2;
                setBar();
                break;
        }
    }
}