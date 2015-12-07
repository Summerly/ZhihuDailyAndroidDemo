package com.example.pein.demo.ui.fragment;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.CBPageAdapter;
import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.OnItemClickListener;
import com.example.pein.demo.R;
import com.example.pein.demo.cache.ImageCacheManger;
import com.example.pein.demo.dao.STORY;
import com.example.pein.demo.dao.STORYDao;
import com.example.pein.demo.database.DBHelper;
import com.example.pein.demo.ui.activity.NewsActivity;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Pein on 15/11/30.
 */
public class LatestListFragment extends ListFragment {
    private ArrayList<STORY> stories = new ArrayList<STORY>();
    private List<String[]> imageURLS = new ArrayList<String[]>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Logger.init();

        getLatestStoriesFromDB();

        StoryAdapter adapter = new StoryAdapter(stories);
        setListAdapter(adapter);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity(), NewsActivity.class);
        intent.putExtra("storyId", stories.get(position-1).getStoryId());
        startActivity(intent);
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
                        return new LocalImageHolderView();
                    }
                }, imageURLS)
                    .setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Logger.e(String.valueOf(position));
                            Intent intent = new Intent(getActivity(), NewsActivity.class);
                            Logger.e(imageURLS.get(position)[0]);
                            intent.putExtra("storyId", imageURLS.get(position)[0]);
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

    private void getLatestStoriesFromDB() {
        STORYDao storyDao = DBHelper.getInstance(getActivity()).getSTORYDao();

        stories = (ArrayList<STORY>) storyDao.queryBuilder()
                .where(STORYDao.Properties.TopStories.eq(false), STORYDao.Properties.Date.eq(getCurrentDate()))
                .list();
        List<STORY> topStories = storyDao.queryBuilder()
                .where(STORYDao.Properties.TopStories.eq(true), STORYDao.Properties.Date.eq(getCurrentDate()))
                .list();
        for (STORY story : topStories) {
            imageURLS.add(new String[]{String.valueOf(story.getStoryId()), story.getImages()});
        }
    }

    public class LocalImageHolderView implements CBPageAdapter.Holder<String[]> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, String[] data) {
            ImageCacheManger.loadImage(data[1], imageView,
                    getBitmapFromResources(R.drawable.ic_rotate_right_black),
                    getBitmapFromResources(R.drawable.ic_tag_faces_black));
        }
    }

    public Bitmap getBitmapFromResources(int resId) {
        return BitmapFactory.decodeResource(getResources(), resId);
    }

    public String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Beijing"));
        Date now = new Date();
        String strDate = simpleDateFormat.format(now);
        return strDate;
    }
}

