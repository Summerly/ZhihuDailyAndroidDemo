package com.example.pein.demo.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pein.demo.R;
import com.example.pein.demo.dao.STORY;
import com.example.pein.demo.database.DemoDatabase;
import com.example.pein.demo.ui.adapter.MultipleItemAdapter;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * A simple {@link Fragment} subclass.
 */
public class LatestFragment extends Fragment {
    private ArrayList<STORY> stories = new ArrayList<STORY>();
    private ArrayList<STORY> topStories = new ArrayList<STORY>();
    private PtrFrameLayout ptrFrameLayout;
    private RecyclerView recyclerView;
    private MultipleItemAdapter multipleItemAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLatestStoriesFromDB();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest, container, false);

        ptrFrameLayout = (PtrFrameLayout) view.findViewById(R.id.ptrFrameLayout);
        MaterialHeader materialHeader = new MaterialHeader(getActivity());

        ptrFrameLayout.setDurationToCloseHeader(1500);
        ptrFrameLayout.setHeaderView(materialHeader);
        ptrFrameLayout.addPtrUIHandler(materialHeader);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DemoDatabase.getLatestStories(getActivity());
                        getLatestStoriesFromDB();
                        multipleItemAdapter.notifyDataSetChanged();
                        ptrFrameLayout.refreshComplete();
                    }
                }, 1800);
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.latest_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        multipleItemAdapter = new MultipleItemAdapter(getActivity(), stories, topStories);
        recyclerView.setAdapter(multipleItemAdapter);

        return view;
    }

    private void getLatestStoriesFromDB() {
        stories = DemoDatabase.getStories(getActivity());
        topStories = DemoDatabase.getTopStories(getActivity());
    }
}
