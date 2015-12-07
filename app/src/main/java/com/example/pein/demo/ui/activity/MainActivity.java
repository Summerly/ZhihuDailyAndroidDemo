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
import com.example.pein.demo.database.DemoDatabase;
import com.example.pein.demo.ui.fragment.LatestFragment;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;


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
            fragment = new LatestFragment();
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
                    String date = object.getString("date");

                    JSONArray topStoriesArray = object.getJSONArray("top_stories");
                    DemoDatabase.saveStories(getApplicationContext(), topStoriesArray, date, true);

                    JSONArray storiesArray = object.getJSONArray("stories");
                    DemoDatabase.saveStories(getApplicationContext(), storiesArray, date, false);
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
}
