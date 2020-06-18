package com.example.kvantnews.ui.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Activity {
    String title, message, image_src, type;
    int time;
    Bitmap image;

    Activity(String title, String message, String image_src, int  time, String type) {
        this.title = title;
        this.message = message;
        this.image_src = image_src;
        this.time = time;
        this.type = type;
        if(!image_src.equalsIgnoreCase("")){
            byte[] imageBytes = Base64.decode(image_src, Base64.DEFAULT);
            InputStream is = new ByteArrayInputStream(imageBytes);
            this.image= BitmapFactory.decodeStream(is);
        }else {
            this.image = null;
        }
    }
}
