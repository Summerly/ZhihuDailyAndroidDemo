package com.example.pein.demo.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.OnItemClickListener;
import com.example.pein.demo.R;
import com.example.pein.demo.cache.ImageCacheManger;
import com.example.pein.demo.dao.STORY;
import com.example.pein.demo.ui.activity.NewsActivity;
import com.example.pein.demo.ui.holder.ImageHolderView;

import java.util.ArrayList;

/**
 * Created by Pein on 15/12/14.
 */
public class MultipleItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static enum ITEM_TYPE {
        ITEM_TYPE_SCROLLVIEW,
        ITEM_TYPE_ITEM
    }

    private static ArrayList<STORY> stories = new ArrayList<STORY>();
    private static ArrayList<STORY> topStories = new ArrayList<STORY>();
    private static Context context;
    private final LayoutInflater layoutInflater;

    public MultipleItemAdapter(Context context, ArrayList<STORY> stories, ArrayList<STORY> topStories) {
        this.context = context;
        this.stories = stories;
        this.topStories = topStories;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_SCROLLVIEW.ordinal()) {
            return new ScrollViewHolder(layoutInflater.inflate(R.layout.fragment_convenient_banner, parent, false));
        } else {
            return new NormalViewHolder(layoutInflater.inflate(R.layout.fragment_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ScrollViewHolder) {
            ((ScrollViewHolder) holder).convenientBanner.setPages(new CBViewHolderCreator() {
                @Override
                public Object createHolder() {
                    return new ImageHolderView(context.getResources());
                }
            }, topStories);
        } else if (holder instanceof NormalViewHolder) {
            ((NormalViewHolder) holder).textView.setText(stories.get(position - 1).getTitle());
            ImageCacheManger.loadImage(stories.get(position - 1).getImages(), ((NormalViewHolder) holder).imageView,
                    getBitmapFromResources(R.drawable.ic_rotate_right_black),
                    getBitmapFromResources(R.drawable.ic_tag_faces_black));
        }
    }

    @Override
    public int getItemCount() {
        return 1 + stories.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    public static class ScrollViewHolder extends RecyclerView.ViewHolder {
        private ConvenientBanner convenientBanner;

        public ScrollViewHolder(View itemView) {
            super(itemView);

            convenientBanner = (ConvenientBanner) itemView.findViewById(R.id.convenientBanner);
            convenientBanner.setPages(new CBViewHolderCreator() {
                @Override
                public Object createHolder() {
                    return new ImageHolderView(context.getResources());
                }
            }, topStories)
                    .setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Intent intent = new Intent(context, NewsActivity.class);
                            intent.putExtra("storyId", topStories.get(position).getStoryId());
                            context.startActivity(intent);
                        }
                    });
        }
    }

    public static class NormalViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;

        public NormalViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.story_title);
            imageView = (ImageView) itemView.findViewById(R.id.story_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NewsActivity.class);
                    intent.putExtra("storyId", stories.get(getPosition() - 1).getStoryId());
                    context.startActivity(intent);
                }
            });
        }
    }

    public Bitmap getBitmapFromResources(int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }
}
