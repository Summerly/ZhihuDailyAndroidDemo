package com.example.pein.demo.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pein.demo.Constants;
import com.example.pein.demo.R;
import com.example.pein.demo.cache.RequestQueueManager;
import com.example.pein.demo.dao.NEWS;
import com.example.pein.demo.dao.NEWSDao;
import com.example.pein.demo.database.DBHelper;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.List;

public class NewsActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_actiity);
        Intent intent = getIntent();
        String storyId = intent.getStringExtra("storyId");

        Logger.init();

        webView = (WebView)findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());

        NEWS news = loadNewsFromDB(storyId);

        if (news == null) {
            loadNewsFromWeb(storyId);
        } else {
            webView.loadData(news.getBody(), "text/html; charset=UTF-8", null);
        }
    }

    private void loadNewsFromWeb(final String storyId) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("News Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL.newsURL + storyId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    saveNewsToDB(storyId, object);
                    webView.loadData(object.getString("body"), "text/html; charset=UTF-8", null);
                    progressDialog.hide();
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.hide();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.e(error.toString());
            }
        });

        RequestQueueManager.addRequest(stringRequest, "news");
    }

    private void saveNewsToDB(String storyId, JSONObject object) {
        try {
            NEWSDao newsDao = DBHelper.getInstance(this).getNEWSDao();
            List<NEWS> newsList = newsDao.queryBuilder().where(NEWSDao.Properties.NewsId.eq(storyId)).list();
            if(newsList.isEmpty()) {
                NEWS news = new NEWS();
                news.setNewsId(storyId);
                news.setImage(object.getString("image"));
                news.setImageSource(object.getString("image_source"));
                news.setTitle(object.getString("title"));
                news.setBody(object.getString("body"));
                news.setShareURL(object.getString("share_url"));
                newsDao.insert(news);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private NEWS loadNewsFromDB(String storyId) {
        NEWSDao newsDao = DBHelper.getInstance(this).getNEWSDao();
        List<NEWS> newsList = newsDao.queryBuilder().where(NEWSDao.Properties.NewsId.eq(storyId)).list();
        if (newsList.isEmpty()) {
            return null;
        } else {
            return newsList.get(0);
        }
    }
}
