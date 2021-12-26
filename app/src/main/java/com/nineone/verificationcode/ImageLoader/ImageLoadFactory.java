package com.nineone.verificationcode.ImageLoader;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import com.bumptech.glide.Glide;

public class ImageLoadFactory {


    public static void clear(Context context, ImageView imageView) {
        Glide.with(context).clear(imageView);
    }

}
