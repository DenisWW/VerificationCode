package com.nineone.verificationcode.ImageLoader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.CheckResult;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;

import java.io.File;

public class ImageLoadBuilder {
    private ImageLoadOptions options;
    private final RequestManager manager;
    private RequestBuilder<?> request;


    ImageLoadBuilder(Context context) {
        manager = Glide.with(context);
    }

    @SuppressLint("CheckResult")
    public void into(ImageView imageView) {
        if (options != null) request.apply(options.build());
        request.into(imageView);
    }

    @SuppressLint("CheckResult")
    public void preload() {
        if (options != null) request.apply(options.build());
        request.preload();
    }

    @SuppressLint("CheckResult")
    public void preload(int width, int height) {
        if (options != null) request.apply(options.build());
        request.preload(width, height);
    }

    public ImageLoadBuilder placeholder(@RawRes @DrawableRes @Nullable Integer res) {
        getOptions().placeholder(res);
        return this;
    }

    public ImageLoadBuilder timeout(int timeoutMs) {
        getOptions().timeout(timeoutMs);
        return this;
    }

    public ImageLoadBuilder scaleType(ImageScaleType scaleType) {
        getOptions().scaleType(scaleType);
        return this;
    }

    public ImageLoadBuilder diskCacheStrategy(ImageCacheType cacheType) {
        getOptions().cacheType(cacheType);
        return this;
    }

    public ImageLoadBuilder round(float dp) {
        getOptions().round(dp);
        return this;
    }

    public ImageLoadBuilder round(float leftTopDp, float rightTopDp, float rightBottomDp, float leftBottomDp) {
        getOptions().round(leftTopDp, rightTopDp, rightBottomDp, leftBottomDp);
        return this;
    }

    public ImageLoadBuilder error(@DrawableRes int resourceId) {
        getOptions().error(resourceId);
        return this;
    }

    public ImageLoadBuilder centerCrop() {
        getOptions().scaleType(ImageScaleType.CENTER_CROP);
        return this;
    }

    ImageLoadBuilder as(ImageLoadType type) {
        switch (type) {
            case GIF_TYPE:
                manager.asGif();
                break;
            case BITMAP_TYPE:
                manager.asBitmap();
                break;
            case FILE_TYPE:
                manager.asFile();
                break;
        }
        return this;
    }

    ImageLoadBuilder load(String url) {
        request = manager.load(url);
        return this;
    }

    ImageLoadBuilder load(@RawRes @DrawableRes @Nullable Integer res) {
        request = manager.load(res);
        return this;
    }

    ImageLoadBuilder load(byte[] res) {
        request = manager.load(res);
        return this;
    }

    @NonNull
    @CheckResult
    ImageLoadBuilder load(@Nullable Bitmap bitmap) {
        request = manager.load(bitmap);
        return this;
    }

    @NonNull
    @CheckResult
    ImageLoadBuilder load(@Nullable Drawable drawable) {
        request = manager.load(drawable);
        return this;
    }

    @NonNull
    @CheckResult
    ImageLoadBuilder load(@Nullable Uri uri) {
        request = manager.load(uri);
        return this;
    }

    @NonNull
    @CheckResult
    ImageLoadBuilder load(@Nullable File file) {
        request = manager.load(file);
        return this;
    }

    private ImageLoadOptions getOptions() {
        if (options == null) options = new ImageLoadOptions();
        return options;
    }


}
