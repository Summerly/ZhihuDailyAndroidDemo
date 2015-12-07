package com.example.pein.demo.ui.holder;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.CBPageAdapter;
import com.example.pein.demo.R;
import com.example.pein.demo.cache.ImageCacheManger;
import com.example.pein.demo.dao.STORY;

/**
 * Created by Pein on 15/12/7.
 */
public class ImageHolderView implements CBPageAdapter.Holder<STORY>{
    private ImageView imageView;
    private Resources resources;

    public ImageHolderView(Resources resources) {
        this.resources = resources;
    }

    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, STORY data) {
        ImageCacheManger.loadImage(data.getImages(), imageView,
                getBitmapFromResources(R.drawable.ic_rotate_right_black),
                getBitmapFromResources(R.drawable.ic_tag_faces_black));
    }

    private Bitmap getBitmapFromResources(int resId) {
        return BitmapFactory.decodeResource(resources, resId);
    }
}
