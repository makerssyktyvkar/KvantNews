package com.example.kvantnews.ui.posts;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class courseNews {
    public String courseName, title, message, additionalInfo, imageString, id_news;
    public Bitmap image;

    courseNews(String id_news, String courseName, String title, String message, String image_src, String additionalInfo) {
        this.id_news = id_news;
        this.courseName = courseName;
        this.title = title;
        this.message = message;
        this.imageString = image_src;
        if(!image_src.equalsIgnoreCase("")){
            byte[] imageBytes = Base64.decode(image_src, Base64.DEFAULT);
            InputStream is = new ByteArrayInputStream(imageBytes);
            this.image= BitmapFactory.decodeStream(is);
        }else {
            this.image = null;
        }
        this.additionalInfo = additionalInfo;
    }
}

