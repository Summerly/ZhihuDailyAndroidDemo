package com.example.pein.demo.ui.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.pein.demo.Constants;
import com.example.pein.demo.R;
import com.example.pein.demo.cache.ImageCacheManger;
import com.example.pein.demo.cache.RequestQueueManager;
import com.example.pein.demo.dao.STORY;
import com.example.pein.demo.database.DemoDatabase;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BeforeFragment extends Fragment {
    private ListView listView;
    private ArrayAdapter<STORY> arrayAdapter;
    private ArrayList<STORY> stories = new ArrayList<STORY>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_before, container, false);
        listView = (ListView) view.findViewById(R.id.before_listview);

        getStories();

        arrayAdapter = new StoryAdapter(stories);
        listView.setAdapter(arrayAdapter);

        return view;
    }

    private void getStories() {
        String date = getArguments().getString("date");
        stories = DemoDatabase.getStories(getActivity(), date);

        if (stories.isEmpty()) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading Stories ...");
            progressDialog.show();
            StringRequest strReq = new StringRequest(Request.Method.GET, Constants.URL.beforeURL + date, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        String date = object.getString("date");

                        JSONArray storiesArray = object.getJSONArray("stories");
                        DemoDatabase.saveStories(getActivity(), storiesArray, date, false);

                        stories = DemoDatabase.getStories(getActivity(), date);
                        arrayAdapter.notifyDataSetChanged();
                        progressDialog.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.v(Constants.TAG.errorTAG, "Error:" + error.getMessage());
                    progressDialog.hide();
                }
            });
            RequestQueueManager.addRequest(strReq, Constants.TAG.latestTAG);
        }

        Logger.init();
    }

    private class StoryAdapter extends ArrayAdapter<STORY> {

        public StoryAdapter(ArrayList<STORY> stories) {
            super(getActivity(), 0, stories);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_item, null);
            }

            TextView titleTextView = (TextView) convertView.findViewById(R.id.story_title);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.story_image);

            STORY story = stories.get(position);

            String imageURL = story.getImages();

            ImageCacheManger.loadImage(imageURL, imageView,
                    getBitmapFromResources(R.drawable.ic_rotate_right_black),
                    getBitmapFromResources(R.drawable.ic_tag_faces_black));

            titleTextView.setText(story.getTitle());

            return convertView;
        }

        @Override
        public int getCount() {
            return stories.size();
        }
    }

    public Bitmap getBitmapFromResources(int resId) {
        return BitmapFactory.decodeResource(getResources(), resId);
    }
}
