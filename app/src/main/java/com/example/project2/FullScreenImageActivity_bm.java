package com.example.project2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FullScreenImageActivity_bm extends AppCompatActivity{

    private String bmstring;

    public FullScreenImageActivity_bm(String bmstring) {
        this.bmstring = bmstring;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image_bm);

        ImageView fullScreenImageView = (ImageView) findViewById(R.id.fullScreenImageView_bm);

        fullScreenImageView.setImageBitmap(BitmapConverter.StringToBitmap(bmstring));
    }
}
