package com.example.kvantnews.ui.posts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Base64;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.example.kvantnews.CoursesOfUserDB;
import com.example.kvantnews.R;

public class AddCourseNews extends AppCompatActivity implements View.OnClickListener {
    private Button course_name_btn;
    private ImageButton add_delete_image_btn;
    private EditText title_et;
    private EditText message_et;
    private String selectedCourse;
    private ImageView news_image_iv;
    private int selectedCourseID;
    private Bitmap news_image;
    private ArrayList<Pair<Integer,String>> courses;
    private TextView info_tv;
    private Uri selectedUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course_news);
        course_name_btn = (Button)findViewById(R.id.course_name);
        title_et = (EditText)findViewById(R.id.news_title);
        message_et = (EditText)findViewById(R.id.news_message);
        news_image_iv = (ImageView)findViewById(R.id.news_image);
        info_tv = (TextView)findViewById(R.id.info_tv);
        add_delete_image_btn = (ImageButton) findViewById(R.id.add_image_button);
        Button submit_btn = (Button) findViewById(R.id.submit_news_btn);
        courses = new CoursesOfUserDB(getApplicationContext()).selectAll();
        if(courses.size() == 0){
            this.onBackPressed();
        }else{
            selectedCourse = courses.get(0).second;
            selectedCourseID = courses.get(0).first;
            course_name_btn.setText(selectedCourse);
        }
        news_image_iv.setVisibility(View.GONE);
        course_name_btn.setOnClickListener(this);
        add_delete_image_btn.setOnClickListener(this);
        submit_btn.setOnClickListener(this);
        submit_btn.setText("Добавить");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.course_name:
                showPopupMenu(v);
                break;
            case R.id.add_image_button:
                if(selectedUri == null){
                    performFileSearch();
                }else{
                    selectedUri = null;
                    add_delete_image_btn.setImageResource(R.drawable.add_image);
                    news_image_iv.setVisibility(View.GONE);
                }
                break;
            case R.id.submit_news_btn:
                addNewsToDB();
                break;
        }
    }

    private void showPopupMenu(View v){
        PopupMenu popupMenu = new PopupMenu(getApplicationContext(),v);
        for(Pair<Integer,String> p : courses){
            popupMenu.getMenu().add(0, p.first, 0, p.second);
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                selectedCourseID = item.getItemId();
                selectedCourse = item.getTitle().toString();
                course_name_btn.setText(selectedCourse);
                return false;
            }
        });
        popupMenu.show();
    }

    private static final int READ_REQUEST_CODE = 42;
    public void performFileSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                selectedUri = uri;
                showImage(uri);
            }
            if(selectedUri != null) {
                add_delete_image_btn.setImageResource(R.drawable.delete_image);
                news_image_iv.setVisibility(View.VISIBLE);
            }
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    private void showImage(Uri uri){
        try {
            news_image = getBitmapFromUri(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(news_image != null){
            news_image_iv.setImageBitmap(news_image);
        }
    }

    private void addNewsToDB(){
        if(isOnline()) {
            courseNews newCourseNews = new courseNews(String.valueOf(selectedCourseID),
                    selectedCourse, title_et.getText().toString(),
                    message_et.getText().toString(), getBase64FromBitmap(news_image), getTime());
            new InsertCourseNews(newCourseNews).execute();
        }else{
            Toast.makeText(getApplicationContext(),R.string.no_internet_connection,Toast.LENGTH_LONG).show();
        }
    }

    private String getBase64FromBitmap(Bitmap bitmap){
        if (bitmap == null) return "";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private String getTime(){
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);
        return dateText + " " + timeText;
    }

    private boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException | InterruptedException e)          { e.printStackTrace(); }
        return false;
    }

    public class InsertCourseNews extends AsyncTask<String, Void, String> {
        courseNews news;
        InsertCourseNews(courseNews news){
            this.news = news;
        }

        @Override
        protected String doInBackground(String... strings) {
            String res = "ok";
            try{
                String url = getApplicationContext().getResources().getString(R.string.main_host_dns) + "AddCoursesNews.php";
                Document document;
                if(selectedUri != null) {
                    File f = new File(getApplicationContext().getCacheDir(), "image");
                    f.createNewFile();
                    Bitmap bitmap = news_image;
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] bitmapData = bos.toByteArray();
                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(bitmapData);
                    fos.flush();
                    fos.close();
                    FileInputStream fileInputStream = new FileInputStream(f);
                    document = Jsoup.connect(url).
                            data("id_course", String.valueOf(selectedCourseID)).
                            data("title", news.title).
                            data("message", news.message).
                            data("time", news.additionalInfo).
                            data("image", "image", fileInputStream).post();
                }else{
                    document = Jsoup.connect(url).
                            data("id_course", String.valueOf(selectedCourseID)).
                            data("title", news.title).
                            data("message", news.message).
                            data("time", news.additionalInfo).post();
                    news.image = null;
                    news.imageString = "";
                }
                news.id_news = document.select("p").eq(0).text();
            }catch (Exception e){
                res = "fail";
            }
            return res;
        }

        @Override
        public void onPostExecute(String s){
            if(!s.equals("fail")) {
                new CoursesNewsOfUserDB(getApplicationContext()).insert(news);
                Toast.makeText(getApplicationContext(), R.string.NewsAddedSuccessfully, Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), R.string.AddingNewsError, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        finish();
    }
}