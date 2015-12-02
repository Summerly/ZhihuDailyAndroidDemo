package com.example.pein.demo.ui.fragment;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.example.pein.demo.DemoApplication;
import com.example.pein.demo.R;
import com.example.pein.demo.bean.Story;
import com.example.pein.demo.cache.ImageCacheManger;
import com.example.pein.demo.cache.RequestQueueManager;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Pein on 15/11/30.
 */
public class LatestListFragment extends ListFragment {
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

            TextView titleTextView = (TextView)convertView.findViewById(R.id.story_title);
            ImageView imageView = (ImageView)convertView.findViewById(R.id.story_image);

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

    private void getLatestStories() {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET, latestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    JSONArray tempStories = json.getJSONArray("stories");
                    for (int i=0; i < tempStories.length(); i++) {
                        JSONObject row = tempStories.getJSONObject(i);
                        JSONArray images = row.getJSONArray("images");
                        Story story = new Story();
                        story.setId(row.getString("id"));
                        story.setTitle(row.getString("title"));
                        story.setImages(images.getString(0));
                        stories.add(story);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pDialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        });

        RequestQueueManager.addRequest(strReq, tag_latestURL);
    }

    public Bitmap getBitmapFromResources(int resId) {
        return BitmapFactory.decodeResource(getResources(), resId);
    }
}

