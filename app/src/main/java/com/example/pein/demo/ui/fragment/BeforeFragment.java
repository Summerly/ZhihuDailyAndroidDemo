package com.example.pein.demo.ui.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.pein.demo.Constants;
import com.example.pein.demo.ui.adapter.NormalRecyclerViewAdapter;
import com.example.pein.demo.R;
import com.example.pein.demo.cache.RequestQueueManager;
import com.example.pein.demo.dao.STORY;
import com.example.pein.demo.database.DemoDatabase;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BeforeFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayAdapter<STORY> arrayAdapter;
    private ArrayList<STORY> stories = new ArrayList<STORY>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_before, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.before_recycler_view);

        getStories();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new NormalRecyclerViewAdapter(stories));

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
}
