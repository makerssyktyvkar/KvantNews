package com.example.kvantnews.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.kvantnews.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ActivitiesFragment extends Fragment implements View.OnClickListener {
    View b1,b2,b3;
    int pos = 0;
    String[][] counts = new String[3][3];
    TextView competitionsCount, learningCount, eventsCount;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_activities, container, false);

        b1 = root.findViewById(R.id.selected_period_bar_btn1);
        b2 = root.findViewById(R.id.selected_period_bar_btn2);
        b3 = root.findViewById(R.id.selected_period_bar_btn3);

        competitionsCount = (TextView) root.findViewById(R.id.competitions_count);
        learningCount = (TextView) root.findViewById(R.id.learning_count);
        eventsCount = (TextView) root.findViewById(R.id.events_count);
        counts = new CountsDB(getContext()).selectAll();
        if(counts[0][0] != null) setBar();

        new FindCounts(getContext()).execute();

        ((Button)root.findViewById(R.id.this_week_btn)).setOnClickListener(this);
        ((Button)root.findViewById(R.id.this_month_btn)).setOnClickListener(this);
        ((Button)root.findViewById(R.id.this_year_btn)).setOnClickListener(this);

        ((RelativeLayout) root.findViewById(R.id.competitions)).setOnClickListener(this);
        ((RelativeLayout) root.findViewById(R.id.learning)).setOnClickListener(this);
        ((RelativeLayout) root.findViewById(R.id.events)).setOnClickListener(this);

        return root;
    }

    private void setBar(){
        switch (pos){
            case 0:
                b1.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red));
                b2.setBackgroundColor(Color.TRANSPARENT);
                b3.setBackgroundColor(Color.TRANSPARENT);

                competitionsCount.setText(counts[0][0]);
                learningCount.setText(counts[0][1]);
                eventsCount.setText(counts[0][2]);
                break;
            case 1:
                b1.setBackgroundColor(Color.TRANSPARENT);
                b2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red));
                b3.setBackgroundColor(Color.TRANSPARENT);

                competitionsCount.setText(counts[1][0]);
                learningCount.setText(counts[1][1]);
                eventsCount.setText(counts[1][2]);
                break;
            case 2:
                b1.setBackgroundColor(Color.TRANSPARENT);
                b2.setBackgroundColor(Color.TRANSPARENT);
                b3.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red));

                competitionsCount.setText(counts[2][0]);
                learningCount.setText(counts[2][1]);
                eventsCount.setText(counts[2][2]);
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
            case R.id.competitions:
                Intent intent1 = new Intent(getActivity(), ActivitiesActivity.class);
                intent1.putExtra("time_type", pos);
                intent1.putExtra("type", 1);
                startActivity(intent1);
                break;
            case R.id.learning:
                Intent intent2 = new Intent(getActivity(), ActivitiesActivity.class);
                intent2.putExtra("time_type", pos);
                intent2.putExtra("type", 2);
                startActivity(intent2);
                break;
            case R.id.events:
                Intent intent3 = new Intent(getActivity(), ActivitiesActivity.class);
                intent3.putExtra("time_type", pos);
                intent3.putExtra("type", 3);
                startActivity(intent3);
                break;
        }
    }

    class FindCounts extends AsyncTask<String, String, String>{
        Context context;

        FindCounts(Context context){
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            String res = "fail";
            try {
                CountsDB countsDB = new CountsDB(context);
                String url = context.getResources().getString(R.string.main_host_dns) + "ReturnActivitiesCount.php";
                Document document = Jsoup.connect(url).maxBodySize(0).get();
                counts[0][0] = document.select("p[class=co11]").text();
                counts[0][1] = document.select("p[class=co12]").text();
                counts[0][2] = document.select("p[class=co13]").text();
                counts[1][0] = document.select("p[class=co21]").text();
                counts[1][1] = document.select("p[class=co22]").text();
                counts[1][2] = document.select("p[class=co23]").text();
                counts[2][0] = document.select("p[class=co31]").text();
                counts[2][1] = document.select("p[class=co32]").text();
                counts[2][2] = document.select("p[class=co33]").text();
                countsDB.insert(counts);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setBar();
        }
    }
}