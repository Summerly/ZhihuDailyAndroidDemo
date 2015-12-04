package com.example.pein.demo.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.pein.demo.Constants;
import com.example.pein.demo.R;

public class NewsActiity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_actiity);
        Intent intent = getIntent();
        String storyId = intent.getStringExtra("storyId");

        WebView webView = (WebView)findViewById(R.id.webView);

        String url = Constants.URL.newsURL + storyId;
        webView.loadUrl(url);
    }


}
