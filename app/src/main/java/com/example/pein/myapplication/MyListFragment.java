package com.example.pein.myapplication;

import android.app.ListFragment;
import android.app.ProgressDialog;
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
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
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

            ImageLoader imageLoader = AppController.getInstance().getImageLoader();

            TextView titleTextView = (TextView)convertView.findViewById(R.id.story_title);
            ImageView imageView = (ImageView)convertView.findViewById(R.id.story_image);

            String imageURL = story.getImages();

            Logger.v(imageURL);

            imageLoader.get(imageURL, ImageLoader.getImageListener(imageView, R.drawable.ic_rotate_right_black, R.drawable.ic_tag_faces_black));

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
                        Story story = new Story(row.getString("id"), row.getString("title"), (String) images.get(0));
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

        AppController.getInstance().addToRequestQueue(strReq, tag_latestURL);
    }
}

