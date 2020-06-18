package com.example.kvantnews;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.kvantnews.ui.achievements.AchievementsDB;
import com.example.kvantnews.ui.posts.CoursesNewsOfUserDB;
import com.example.kvantnews.ui.support.SupportsDB;
import com.example.kvantnews.ui.timetable.ChildrenDB;
import com.example.kvantnews.ui.timetable.TimetableDB;
import com.onesignal.OneSignal;


import static com.example.kvantnews.MainActivity.isOnline;

public class ExitDialogFragment extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Вы хотите выйти?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(isOnline()) {
                    Toast.makeText(getActivity(), "Вы вышли",
                            Toast.LENGTH_LONG).show();
                    try {
                        new MainActivity.AddToMainDBOSId(new User(getContext()).getLogin(), "", getContext()).execute().get();
                    }catch (Exception e){
                        //
                    }
                    CoursesNewsOfUserDB coursesNewsOfUserDB = new CoursesNewsOfUserDB(getContext());
                    coursesNewsOfUserDB.deleteAll();
                    SupportsDB supportsDB = new SupportsDB(getContext());
                    supportsDB.deleteAll();
                    TimetableDB timetableDB = new TimetableDB(getContext());
                    timetableDB.deleteAll();
                    ChildrenDB childrenDB = new ChildrenDB(getContext());
                    childrenDB.deleteAll();
                    AchievementsDB achievementsDB = new AchievementsDB(getContext());
                    achievementsDB.deleteAll();
                    CoursesOfUserDB coursesOfUserDB = new CoursesOfUserDB(getContext());
                    coursesNewsOfUserDB.deleteAll();
                    User user = new User(getContext());
                    user.deleteAll();
                    OneSignal.sendTag("sub", "false");
                    startLoginActivity();
                }else{
                    Toast.makeText(getContext(), R.string.no_internet_connection,Toast.LENGTH_SHORT).show();
                }
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.setCancelable(false);

        return builder.create();
    }

    private void startLoginActivity(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

}
