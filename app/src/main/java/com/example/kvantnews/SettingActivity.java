package com.example.kvantnews;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.onesignal.OneSignal;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    private CheckBox is_notifications_btn;
    private boolean notificated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Button info_btn = findViewById(R.id.button_info);
        is_notifications_btn = findViewById(R.id.checkbox_notification);
        if(new IsNotification(this).select() == 0){
            notificated = false;
        }else{
            notificated = true;
            is_notifications_btn.setChecked(true);
        }
        is_notifications_btn.setOnClickListener(this);
        ((Button) findViewById(R.id.button_info)).setOnClickListener(this);
        ((Button) findViewById(R.id.button_q_and_a)).setOnClickListener(this);
        ((Button) findViewById(R.id.button_exit)).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_q_and_a:
                startQAndA();
                break;
            case R.id.button_info:
                startInfoActivity();
                break;
            case R.id.checkbox_notification:
                if(MainActivity.isOnline()) {
                    notificated = !notificated;
                    if (!notificated) {
                        OneSignal.sendTag("sub","false");
                        new MainActivity.AddToMainDBOSId(new User(this).getLogin(), "", this).execute();
                    } else {
                        OneSignal.sendTag("sub","true");
                        new MainActivity.AddToMainDBOSId(new User(this).getLogin(), User.UniqueID, this).execute();
                    }
                    new IsNotification(this).change(notificated ? 1 : 0);
                    Toast.makeText(this, "Настройки уведомлений успешно изменены", Toast.LENGTH_SHORT).show();
                }else{
                    is_notifications_btn.setChecked(notificated);
                    Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button_exit:
                FragmentManager manager = this.getSupportFragmentManager();
                ExitDialogFragment exitDialogFragment = new ExitDialogFragment();
                exitDialogFragment.show(manager, "myDialog");
        }
    }

    private void startQAndA(){
        Intent intent = new Intent(SettingActivity.this, QAndAActivity.class);
        startActivity(intent);

    }

    private void startInfoActivity(){
        Intent intent = new Intent(SettingActivity.this, InfoActivity.class);
        startActivity(intent);
    }

}
