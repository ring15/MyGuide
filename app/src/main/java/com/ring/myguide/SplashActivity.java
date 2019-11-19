package com.ring.myguide;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.hyphenate.chat.EMClient;
import com.ring.myguide.entity.User;
import com.ring.myguide.login.view.LoginActivity;
import com.ring.myguide.main.view.MainActivity;
import com.ring.myguide.utils.CacheUtils;

public class SplashActivity extends AppCompatActivity {

    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
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
        if (EMClient.getInstance().isLoggedInBefore()) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        } else {
            User user = CacheUtils.getUser();
            if (user != null && user.getBadge() == 1) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }
    }
}
