package com.example.kvantnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    EditText password_et, login_et, mail_et, password_repeat_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        password_et = (EditText) findViewById(R.id.user_password_et);
        password_repeat_et = (EditText) findViewById(R.id.user_password_repeat_et);
        login_et = (EditText) findViewById(R.id.user_login_et);
        mail_et = (EditText) findViewById(R.id.user_mail_et);

        ((Button)findViewById(R.id.login_btn)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn:
                if(MainActivity.isOnline()) {
                    String mail = mail_et.getText().toString();
                    String login = login_et.getText().toString();
                    String password = password_et.getText().toString();
                    String password_repeat = password_repeat_et.getText().toString();
                    if (isEmailValid(mail)) {
                        if (login.length() > 4 && login.length() < 20) {
                            if (password.length() > 4 && password.length() < 16) {
                                if (password.equals(password_repeat)) {
                                    Toast.makeText(this, R.string.please_wait, Toast.LENGTH_SHORT).show();
                                    new RegisterUser(password, login, mail, this).execute();
                                } else {
                                    Toast.makeText(this, R.string.passwords_should_equals, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, R.string.lenght_of_password, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(this, R.string.lenght_of_login, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, R.string.failed_email, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    public boolean isEmailValid(String email) {
        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@" +
                "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" +
                "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\." +
                "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|" +
                "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    static class RegisterUser extends AsyncTask<String, String, String>{
        String password, login, mail;
        Context context;

        RegisterUser(String password, String login, String mail, Context context){
            this.mail = mail;
            this.password = password;
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
                Toast.makeText(context, R.string.you_are_successfully_registrated, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, R.string.its_fail, Toast.LENGTH_SHORT).show();
            }
        }
    }
}