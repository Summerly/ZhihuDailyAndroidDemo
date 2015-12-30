package com.example.pein.demo.ui.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

public class BeforeFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<STORY> stories = new ArrayList<STORY>();
    private NormalRecyclerViewAdapter _normalRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_before, container, false);

        Logger.init();

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
        stories = DemoDatabase.getStories(getActivity(), date);

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
        Logger.e("tryRxAndroid");

        Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext(getArguments().getString("date"));
                        Logger.e("subscriber onNext");
                        subscriber.onCompleted();
                    }
                })
                .map(new Func1<String, ArrayList<STORY>>() {
                    @Override
                    public ArrayList<STORY> call(String s) {
                        ArrayList<STORY> tempStories = DemoDatabase.getStories(getActivity(), s);
                        Logger.e(tempStories.toString());
                        return tempStories;
                    }
                })
                .isEmpty()
                .doOnNext(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        Logger.e("doOnNext isEmpty");
                    }
                })
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {
                        Logger.e("Observer onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        Logger.e("Observer onNext");
                    }
                })
        ;
    }
}
