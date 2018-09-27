package com.tutao.common.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageUtils {

    public static void loadLocalImage(Context context, int resId, ImageView imageView) {
        Glide.with(context).load(resId).into(imageView);
    }

    public static void loadImage(Context context, String imageUrl, ImageView imageView) {
        Glide.with(context).load(imageUrl).into(imageView);
    }
}
