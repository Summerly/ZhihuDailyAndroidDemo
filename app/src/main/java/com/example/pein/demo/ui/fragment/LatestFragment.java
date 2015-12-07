package com.example.pein.demo.ui.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.OnItemClickListener;
import com.example.pein.demo.R;
import com.example.pein.demo.cache.ImageCacheManger;
import com.example.pein.demo.dao.STORY;
import com.example.pein.demo.database.DemoDatabase;
import com.example.pein.demo.ui.activity.NewsActivity;
import com.example.pein.demo.ui.holder.ImageHolderView;
import com.orhanobut.logger.Logger;

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
    private ListView listView;
    private StoryAdapter storyAdapter;

    public LatestFragment() {
    }

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
                        storyAdapter.notifyDataSetChanged();
                        ptrFrameLayout.refreshComplete();
                    }
                }, 1800);
            }
        });

        listView = (ListView) view.findViewById(R.id.listView);

        storyAdapter = new StoryAdapter(stories);
        listView.setAdapter(storyAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewsActivity.class);
                intent.putExtra("storyId", stories.get(position - 1).getStoryId());
                startActivity(intent);
            }
        });

        return view;
    }

    private void getLatestStoriesFromDB() {
        stories = DemoDatabase.getStories(getActivity());
        topStories = DemoDatabase.getTopStories(getActivity());
    }

    public Bitmap getBitmapFromResources(int resId) {
        return BitmapFactory.decodeResource(getResources(), resId);
    }

    private class StoryAdapter extends ArrayAdapter<STORY> {
        public StoryAdapter(ArrayList<STORY> stories) {
            super(getActivity(), 0, stories);
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? 0 : 1;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {

                LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                if (getItemViewType(position) == 0) {
                    convertView = mInflater.inflate(R.layout.fragment_convenient_banner, null);
                } else {
                    convertView = mInflater.inflate(R.layout.fragment_item, null);
                }
            }

            if (getItemViewType(position) == 0) {
                ConvenientBanner convenientBanner = (ConvenientBanner) convertView.findViewById(R.id.convenientBanner);
                convenientBanner.setPages(new CBViewHolderCreator() {
                    @Override
                    public Object createHolder() {
                        return new ImageHolderView(getResources());
                    }
                }, topStories)
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Intent intent = new Intent(getActivity(), NewsActivity.class);
                                intent.putExtra("storyId", topStories.get(position).getStoryId());
                                startActivity(intent);
                            }
                        });
            } else {
                TextView titleTextView = (TextView) convertView.findViewById(R.id.story_title);
                ImageView imageView = (ImageView) convertView.findViewById(R.id.story_image);

                STORY story = stories.get(position - 1);

                String imageURL = story.getImages();

                ImageCacheManger.loadImage(imageURL, imageView,
                        getBitmapFromResources(R.drawable.ic_rotate_right_black),
                        getBitmapFromResources(R.drawable.ic_tag_faces_black));

                titleTextView.setText(story.getTitle());

            }
            return convertView;
        }

        @Override
        public int getCount() {
            return 1 + stories.size();
        }
    }
}
