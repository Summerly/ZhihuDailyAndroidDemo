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

import com.example.pein.demo.R;
import com.example.pein.demo.cache.ImageCacheManger;
import com.example.pein.demo.dao.STORY;
import com.example.pein.demo.ui.activity.NewsActivity;

import java.util.ArrayList;

/**
 * Created by Pein on 15/12/9.
 */
public class NormalRecyclerViewAdapter extends RecyclerView.Adapter<NormalRecyclerViewAdapter.NormalViewHolder> {
    private static ArrayList<STORY> stories;
    private static Context context;

    public NormalRecyclerViewAdapter(ArrayList<STORY> stories) {
        this.stories = stories;
    }

    @Override
    public NormalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_item, null);
        return new NormalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NormalViewHolder holder, int position) {
        holder.textView.setText(stories.get(position).getTitle());
        ImageCacheManger.loadImage(stories.get(position).getImages(), holder.imageView,
                getBitmapFromResources(R.drawable.ic_rotate_right_black),
                getBitmapFromResources(R.drawable.ic_tag_faces_black));
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    public static class NormalViewHolder  extends RecyclerView.ViewHolder {
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
                    intent.putExtra("storyId", stories.get(getPosition()).getStoryId());
                    context.startActivity(intent);
                }
            });
        }
    }

    public Bitmap getBitmapFromResources(int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }
}
