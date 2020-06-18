package com.example.kvantnews.ui.support;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import com.example.kvantnews.R;
import com.example.kvantnews.User;

public class SupportFragment extends Fragment implements View.OnClickListener {
    private ArrayList<Support> listSupports = new ArrayList<>();
    private SupportsDB supportsDB;
    private SupportAdapter adapter;
    private ListView lv;
    private String login;
    private EditText user_question;
    private Button submit_question_btn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_support, container, false);
        login = new User(getActivity()).getLogin();
        user_question = root.findViewById(R.id.user_question);
        submit_question_btn = root.findViewById(R.id.submit_question_btn);
        lv =  (ListView) root.findViewById(R.id.list_container);
        supportsDB = new SupportsDB(root.getContext());
        listSupports = supportsDB.selectAll();
        if(listSupports.size()!=0){
            adapter = new SupportAdapter(getContext(), R.layout.support_adapter, listSupports);
            lv.setAdapter(adapter);
        }
        ImageButton reload_btn = (ImageButton)root.findViewById(R.id.reload_btn);
        reload_btn.setOnClickListener(this);
        submit_question_btn.setOnClickListener(this);
        reloadPressed();
        return root;
    }

    private void reloadPressed() {
        if(isOnline()) {
            new GetUserSupports().execute();
        }else{
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
        }
    }

    public class GetUserSupports extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... args) {
            try {
                String url = getString(R.string.main_host_dns) + "ReturnSupportsForUser.php?login=" + login;
                Document document = Jsoup.connect(url).maxBodySize(0).get();
                Elements element = document.select("li[class=support-item]");
                supportsDB.deleteAll();
                for (int i = 0; i < element.size(); i++) {
                    long id = Integer.parseInt(element.eq(i).select("p[class=id]").eq(0).text());
                    String answer = element.eq(i).select("p[class=answer]").eq(0).text();
                    String question = element.eq(i).select("p[class=question]").eq(0).text();
                    supportsDB.insert(id, answer,question);
                }
            } catch (Exception e) {
                //
            }
            return 1;
        }

        @Override
        protected void onPostExecute(Integer s){
            super.onPostExecute(s);
            listSupports = supportsDB.selectAll();
            adapter = new SupportAdapter(getContext(), R.layout.support_adapter, listSupports);
            lv.setAdapter(adapter);
        }
    }

    boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException | InterruptedException e)          { e.printStackTrace(); }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reload_btn:
                reloadPressed();
                break;
            case R.id.submit_question_btn:
                InputMethodManager imm2 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                try {
                    new SubmitSupport(login, user_question.getText().toString(),getContext()).execute().get();
                } catch (Exception e){
                    //
                }
                reloadPressed();
                user_question.setText("");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        listSupports = supportsDB.selectAll();
        adapter = new SupportAdapter(getContext(), R.layout.support_adapter, listSupports);
        lv.setAdapter(adapter);
    }
    @Override
    public void onResume() {
        super.onResume();
        listSupports = supportsDB.selectAll();
        adapter = new SupportAdapter(getContext(), R.layout.support_adapter, listSupports);
        lv.setAdapter(adapter);
    }
}
