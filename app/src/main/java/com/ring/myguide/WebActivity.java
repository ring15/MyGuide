package com.ring.myguide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WebActivity extends AppCompatActivity {

    //标题
    private TextView tvTitle;
    //加载进度条
    private ProgressBar pg;
    //webview
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        findView();
        init();
    }

    private void findView() {
        tvTitle = findViewById(R.id.tv_title);
        webView = findViewById(R.id.web_view);
        pg = findViewById(R.id.progress_bar);
    }

    private void init() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("web_title");
        String h5url = intent.getStringExtra("web_url");
        //设置标题
        tvTitle.setText(title);

        //url拦截
        webView.setWebViewClient(new WebViewClient() {
            //覆写shouldOverrideUrlLoading实现内部显示网页
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        //webview设置
        WebSettings seting = webView.getSettings();
        seting.setUseWideViewPort(true);
        seting.setLoadWithOverviewMode(true);
        seting.setDomStorageEnabled(true);
        seting.setSupportMultipleWindows(true);
        seting.setJavaScriptEnabled(true);//设置webview支持javascript脚本
        seting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        seting.setPluginState(WebSettings.PluginState.ON);
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    pg.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    pg.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    pg.setProgress(newProgress);//设置进度值
                }
            }
        });
        webView.loadUrl(h5url);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_return:
                finish();
                break;
        }
    }

    @Override
    public void onResume() {
        //恢复播放
        webView.resumeTimers();
        super.onResume();
    }

    @Override
    public void onPause() {
        //暂停播放
        webView.pauseTimers();
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        //一定要销毁，否则无法停止播放
        webView.destroy();
        super.onDestroy();
    }
}
