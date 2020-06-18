package com.example.kvantnews.ui.support;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.kvantnews.R;

public class SupportAdapter extends ArrayAdapter<Support> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<Support> listSupports;
    private Context context;

    public SupportAdapter(Context context, int resource, ArrayList<Support> supports) {
        super(context, resource, supports);
        this.listSupports = supports;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Support support = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.support_adapter, null);
        }
        assert support != null;
        if(!support.answer.equals(""))((TextView) convertView.findViewById(R.id.answer)).setText(support.answer);
        ((TextView) convertView.findViewById(R.id.question)).setText(support.question);
        ((ImageButton) convertView.findViewById(R.id.delete_support_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DeleteSupportSecond.class);
                intent.putExtra("id_support", (String)String.valueOf(support.id));
                try {
                    ((Activity) context).startActivityForResult(intent, 42);
                }catch (Exception e){
                    Log.e("my_tag", e.getMessage() + e.getCause());
                }
            }
        });
        return convertView;
    }
}