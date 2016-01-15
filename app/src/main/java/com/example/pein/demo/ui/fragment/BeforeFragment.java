package com.example.pein.demo.ui.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.pein.demo.Constants;
import com.example.pein.demo.TimeUtils;
import com.example.pein.demo.ui.adapter.NormalRecyclerViewAdapter;
import com.example.pein.demo.R;
import com.example.pein.demo.cache.RequestQueueManager;
import com.example.pein.demo.dao.STORY;
import com.example.pein.demo.database.DemoDatabase;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import rx.Observable;

import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class BeforeFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<STORY> stories = new ArrayList<STORY>();
    private NormalRecyclerViewAdapter _normalRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_before, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.before_recycler_view);

        getStories();

        _normalRecyclerViewAdapter = new NormalRecyclerViewAdapter(stories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(_normalRecyclerViewAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        tryRxAndroid();
    }

    private void getStories() {
        String date = getArguments().getString("date");

        String tomorrowDate = TimeUtils.getTomorrowDate(date);

        stories = DemoDatabase.getStories(getActivity(), tomorrowDate);

        if (stories.isEmpty()) {
            final ProgressDialog _progressDialog = new ProgressDialog(getActivity());
            _progressDialog.setTitle("Loading...");
            _progressDialog.show();
            StringRequest strReq = new StringRequest(Request.Method.GET, Constants.URL.beforeURL + date, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        String date = object.getString("date");

                        JSONArray storiesArray = object.getJSONArray("stories");
                        DemoDatabase.saveStories(getActivity(), storiesArray, date, false);

                        stories = DemoDatabase.getStories(getActivity(), date);
                        _normalRecyclerViewAdapter.notifyDataSetChanged();
                        _progressDialog.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.v(Constants.TAG.errorTAG, "Error:" + error.getMessage());
                    _progressDialog.hide();
                }
            });
            RequestQueueManager.addRequest(strReq, Constants.TAG.latestTAG);
        }
    }

    private void tryRxAndroid() {
        Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext(getArguments().getString("date"));
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, ArrayList<STORY>>() {
                    @Override
                    public ArrayList<STORY> call(String s) {
                        ArrayList<STORY> tempStories = DemoDatabase.getStories(getActivity(), s);
                        return tempStories;
                    }
                })
                .map(new Func1<ArrayList<STORY>, Boolean>() {
                    @Override
                    public Boolean call(ArrayList<STORY> stories) {
                        return stories.isEmpty();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {

                    }
                })
        ;
    }
}
