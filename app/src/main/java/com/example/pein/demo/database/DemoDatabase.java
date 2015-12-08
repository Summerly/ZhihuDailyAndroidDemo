package com.example.pein.demo.database;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.pein.demo.Constants;
import com.example.pein.demo.cache.RequestQueueManager;
import com.example.pein.demo.dao.STORY;
import com.example.pein.demo.dao.STORYDao;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pein on 15/12/7.
 */
public class DemoDatabase {
    public static ArrayList<STORY> getStories(Context context) {
        STORYDao storyDao = DBHelper.getInstance(context).getSTORYDao();

        return (ArrayList<STORY>) storyDao.queryBuilder()
                .where(STORYDao.Properties.TopStories.eq(false), STORYDao.Properties.Date.eq(Constants.getCurrentDate()))
                .list();
    }

    public static ArrayList<STORY> getStories(Context context, String date) {
        STORYDao storyDao = DBHelper.getInstance(context).getSTORYDao();

        return (ArrayList<STORY>) storyDao.queryBuilder()
                .where(STORYDao.Properties.TopStories.eq(false), STORYDao.Properties.Date.eq(date))
                .list();
    }

    public static ArrayList<STORY> getTopStories(Context context) {
        STORYDao storyDao = DBHelper.getInstance(context).getSTORYDao();

        return (ArrayList<STORY>) storyDao.queryBuilder()
                .where(STORYDao.Properties.TopStories.eq(true), STORYDao.Properties.Date.eq(Constants.getCurrentDate()))
                .list();
    }

    public static void getLatestStories(final Context context) {
        StringRequest strReq = new StringRequest(Request.Method.GET, Constants.URL.latestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String date = object.getString("date");

                    JSONArray topStoriesArray = object.getJSONArray("top_stories");
                    DemoDatabase.saveStories(context, topStoriesArray, date, true);

                    JSONArray storiesArray = object.getJSONArray("stories");
                    DemoDatabase.saveStories(context, storiesArray, date, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.v(Constants.TAG.errorTAG, "Error:" + error.getMessage());
            }
        });
        RequestQueueManager.addRequest(strReq, Constants.TAG.latestTAG);
    }

    public static void saveStories(Context context, JSONArray storiesArray, String date, boolean isTopStories) {
        STORYDao storyDao = DBHelper.getInstance(context).getSTORYDao();
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
