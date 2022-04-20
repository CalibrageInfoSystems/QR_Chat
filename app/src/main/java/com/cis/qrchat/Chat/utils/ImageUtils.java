package com.cis.qrchat.Chat.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

public class ImageUtils {
    public static void displayGifImageFromUrl(Context context, String url, ImageView imageView, RequestListener listener) {
        RequestOptions myOptions = new RequestOptions()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

        if (listener != null) {
            Glide.with(context)
                    .asGif()
                    .load(url)
                    .apply(myOptions)
                    .listener(listener)
                    .into(imageView);
        } else {
            Glide.with(context)
                    .asGif()
                    .load(url)
                    .apply(myOptions)
                    .into(imageView);
        }
    }
    public static void displayCircularImageFromUrl(Context context, String imageUrl, ImageView imageView) {
        if (context == null || TextUtils.isEmpty(imageUrl) || imageView == null) {
            return;
        }

        Glide.with(context)
            .asBitmap()
            .apply(new RequestOptions().centerCrop().dontAnimate())
            .load(imageUrl)
            .into(new BitmapImageViewTarget(imageView) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    imageView.setImageDrawable(circularBitmapDrawable);
                }
            });
    }
}
