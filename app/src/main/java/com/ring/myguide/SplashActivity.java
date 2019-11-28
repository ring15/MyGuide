package com.ring.myguide;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hyphenate.chat.EMClient;
import com.ring.myguide.entity.User;
import com.ring.myguide.login.view.LoginActivity;
import com.ring.myguide.main.view.MainActivity;
import com.ring.myguide.utils.CacheUtils;

import java.util.ArrayList;
import java.util.List;

import static com.ring.myguide.login.view.LoginActivity.FROM_SPLASH;

public class SplashActivity extends AppCompatActivity {

    private Handler mHandler;
    private Runnable mRunnable;

    private String[] permissions = {Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
        requestPermission();
    }

    private void requestPermission() {
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(SplashActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (permissionList.size() != 0) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[0]), 1001);
        }
    }

    private void init() {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                enter();
            }
        };
        mHandler = new Handler();
        //3秒后进入APP
        mHandler.postDelayed(mRunnable, 3000);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_skip:
                //跳过启动页，直接进入APP
                mHandler.removeCallbacks(mRunnable);
                enter();
                break;
        }
    }

    /**
     * 判断跳转主界面或者跳转登录界面
     */
    private void enter() {
        User user = CacheUtils.getUser();
        if (user != null && user.getUserName() != null && EMClient.getInstance().isLoggedInBefore()) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        } else {
            if (user != null && user.getBadge() == 1) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            } else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.putExtra("from", FROM_SPLASH);
                startActivity(intent);
                finish();
            }
        }
    }
}
