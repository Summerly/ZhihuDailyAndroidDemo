package com.example.pein.demo.ui.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.pein.demo.Constants;
import com.example.pein.demo.R;
import com.example.pein.demo.cache.RequestQueueManager;
import com.example.pein.demo.dao.STORY;
import com.example.pein.demo.dao.STORYDao;
import com.example.pein.demo.database.DBHelper;
import com.example.pein.demo.ui.fragment.LatestListFragment;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "XiYuexin";
    private static final String STRING_REQUEST_TAG = "latest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logger.init();

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = new LatestListFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLatestStories();
    }

    private void getLatestStories() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET, Constants.URL.latestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    STORYDao storyDao = DBHelper.getInstance(getApplicationContext()).getSTORYDao();
                    String date = object.getString("date");

                    JSONArray topStoriesArray = object.getJSONArray("top_stories");
                    insertStory(storyDao, topStoriesArray, date, true);

                    JSONArray storiesArray = object.getJSONArray("stories");
                    insertStory(storyDao, storiesArray, date, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.v(TAG, "Error:" + error.getMessage());
                progressDialog.hide();
            }
        });
        RequestQueueManager.addRequest(strReq, STRING_REQUEST_TAG);
    }

    private void insertStory(STORYDao storyDao,JSONArray storiesArray,String date,boolean isTopStories) {
        try {
            for (int i = 0; i < storiesArray.length(); i++) {
                JSONObject row = storiesArray.getJSONObject(i);
                String storyId = row.getString("id");
                List<STORY> storiesFromDB = storyDao.queryBuilder()
                        .where(STORYDao.Properties.StoryId.eq(storyId), STORYDao.Properties.Date.eq(date))
                        .list();
                if (storiesFromDB.isEmpty()) {
                    STORY story = new STORY();
                    story.setStoryId(row.getString("id"));
                    story.setTitle(row.getString("title"));
                    if (isTopStories) {
                        story.setImages(row.getString("image"));
                    } else {
                        story.setImages(row.getJSONArray("images").getString(0));
                    }
                    story.setTopStories(isTopStories);
                    story.setDate(date);
                    storyDao.insert(story);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
