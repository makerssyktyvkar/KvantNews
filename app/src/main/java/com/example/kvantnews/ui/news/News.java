package com.example.kvantnews.ui.news;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

class News {
    String title;
    String message;
    Bitmap image;
    String additionalInfo;

    News(String title, String message, String image_src, String additionalInfo) {
        this.title = title;
        this.message = message;
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
