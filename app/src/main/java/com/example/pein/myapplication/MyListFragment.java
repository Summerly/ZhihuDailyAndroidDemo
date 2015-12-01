package com.example.pein.myapplication;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

/**
 * Created by Pein on 15/11/30.
 */
public class MyListFragment extends ListFragment {
    private ArrayList<Story> stories = new ArrayList<Story>();
    private static final String tag_latestURL = "latest";
    private static final String latestURL = "http://news-at.zhihu.com/api/4/news/latest";
    private static final String TAG = "XiYuexin";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Logger.init();

        getLatestStories();

        StoryAdapter adapter = new StoryAdapter(stories);

        setListAdapter(adapter);

        test();
    }

    private class StoryAdapter extends ArrayAdapter<Story> {
        public StoryAdapter(ArrayList<Story> stories) {
            super(getActivity(), 0, stories);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.fragment_blank, null);
            }

            Story story = stories.get(position);

            TextView idTextView = (TextView)convertView.findViewById(R.id.story_id);
            TextView titleTextView = (TextView)convertView.findViewById(R.id.story_title);

            idTextView.setText(story.getId());
            titleTextView.setText(story.getTitle());

            return convertView;
        }

        @Override
        public int getCount() {
            return stories.size();
        }
    }

    private void getLatestStories() {
        for (int i = 0; i < 10; i++) {
            Story story = new Story(String.valueOf(i), String.valueOf(i*i));
            stories.add(story);
        }
        Logger.v(String.valueOf(stories));
    }

    private void test() {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET, latestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Logger.v(response);
                pDialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        });

        AppController.getInstance().addToRequestQueue(strReq, tag_latestURL);
    }
}

