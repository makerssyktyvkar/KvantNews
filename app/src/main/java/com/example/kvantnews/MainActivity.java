package com.example.kvantnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    EditText password_et, login_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        password_et = findViewById(R.id.user_password_et);
        login_et = findViewById(R.id.user_login_et);

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
                //
                break;
            case R.id.remind_password_btn:
                //
                break;
        }
    }
}