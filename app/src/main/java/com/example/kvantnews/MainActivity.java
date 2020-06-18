package com.example.kvantnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    EditText password_et, login_et;
    ImageView main_background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        password_et = findViewById(R.id.user_password_et);
        login_et = findViewById(R.id.user_login_et);
        main_background = (ImageView) findViewById(R.id.login_main_iv);
        new ChangeBackgroundIfExist().execute();

        ((Button)findViewById(R.id.create_account_btn)).setOnClickListener(this);
        ((Button)findViewById(R.id.login_btn)).setOnClickListener(this);
        ((Button)findViewById(R.id.remind_password_btn)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_account_btn:
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
                break;
            case R.id.login_btn:
                if(isOnline()){
                    String login = login_et.getText().toString();
                    String password = password_et.getText().toString();
                    String type;
                    try {
                         type = new IsOkUser(login, password, this).execute().get();
                    }catch (Exception e) {
                        type = "fail";
                    }
                }else{
                    Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.remind_password_btn:
                if(isOnline()){
                    String login = login_et.getText().toString();
                    if(login.length() > 4 && login.length() < 20){
                        new RemindPassword(login, this).execute();
                    }else{
                        Toast.makeText(this, R.string.enter_correct_password, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException | InterruptedException e)          { e.printStackTrace(); }
        return false;
    }

    static class RemindPassword extends AsyncTask<String, String, String>{
        String login;
        Context context;

        RemindPassword(String login, Context context){
            this.login = login;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            String res = "fail";

            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("ok")){
                Toast.makeText(context, R.string.password_remind_sended, Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(context, R.string.its_fail, Toast.LENGTH_SHORT).show();
            }
        }
    }

    static class IsOkUser extends AsyncTask<String, String, String>{
        String login, password;
        Context context;

        IsOkUser(String login, String password, Context context){
            this.login = login;
            this.password = password;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            String res = "fail";

            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(!s.equals("fail")){
                Toast.makeText(context, R.string.please_wait, Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(context, R.string.its_fail, Toast.LENGTH_SHORT).show();
            }
        }
    }

    class ChangeBackgroundIfExist extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            String res = "fail";
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(!s.equals("fail")){
                Bitmap newBackground = imageFromString64(s);
                if(newBackground != null) {
                    main_background.setImageBitmap(newBackground);
                }
            }
        }
    }

    public static Bitmap imageFromString64(String image64){
        if(!image64.equalsIgnoreCase("")){
            byte[] imageBytes = Base64.decode(image64, Base64.DEFAULT);
            InputStream is = new ByteArrayInputStream(imageBytes);
            return BitmapFactory.decodeStream(is);
        }
        return null;
    }
}