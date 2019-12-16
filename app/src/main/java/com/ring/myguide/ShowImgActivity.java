package com.ring.myguide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ShowImgActivity extends AppCompatActivity {

    private ImageView mPreImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_img);
        findView();
        init();
    }

    private void init() {
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        Glide.with(this)
                .load(path)
                .error(R.drawable.icon_avatar_default)
                .placeholder(R.drawable.icon_default_photo)
                .into(mPreImg);
    }

    private void findView() {
        mPreImg = findViewById(R.id.img_pre);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_return:
                finish();
                break;
        }
    }

}
