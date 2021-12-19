package com.nineone.verificationcode.ImageLoader;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import com.bumptech.glide.load.resource.gif.GifDrawable;

public class ImageLoader {
    //displayImage
    public static ImageLoadBuilder load(@NonNull Context context, @Nullable String url) {
        return new ImageLoadBuilder(context).load(url);
    }

    public static ImageLoadBuilder load(@NonNull Context context, @RawRes @DrawableRes @Nullable Integer res) {
        return new ImageLoadBuilder(context).load(res);
    }

    public static ImageLoadBuilder loadGif(@NonNull Context context, @Nullable String url) {
        return new ImageLoadBuilder(context).as(ImageLoadType.GIF_TYPE).load(url);
    }

    public static ImageLoadBuilder loadGif(@NonNull Context context, @RawRes @DrawableRes @Nullable Integer res) {
        return new ImageLoadBuilder(context).as(ImageLoadType.GIF_TYPE).load(res);
    }

    public static ImageDownLoadBuilder downLoad(@NonNull Context context, @Nullable String url, @Nullable String targetPath) {
        return new ImageDownLoadBuilder(context, url, targetPath);
    }

    public static void clear(Context context, ImageView imageView) {
        ImageLoadFactory.clear(context, imageView);
    }
}
