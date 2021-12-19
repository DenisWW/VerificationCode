package com.nineone.verificationcode.ImageLoader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.PreloadTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;

public class ImageDownLoadBuilder {

    private String path;
    private String url;
    private ProgressListener progressListener;
    private ImageDownListener downListener;
    private RequestBuilder<File> request;
    private RequestManager manager;

    ImageDownLoadBuilder(Context context, String url, String path) {
        this.path = path;
        this.url = url;
        request = (manager = Glide.with(context)).downloadOnly().load(url);
    }

    public ImageDownLoadBuilder progressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
        return this;
    }

    public ImageDownLoadBuilder listener(ImageDownListener downListener) {
        this.downListener = downListener;
        return this;
    }

    @SuppressLint("CheckResult")
    public void down() {
        if (request == null) return;
        if (downListener != null) request.listener(new RequestListener<File>() {
            @Override
            public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                downListener.onDownFailed(url, path, e != null ? e.getMessage() : "");
                return false;
            }

            @Override
            public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                downListener.onDownSuccess(url, path);
                return false;
            }

        });
        if (progressListener != null) {
            ProgressInterceptor.addListener(url, progressListener);
        }
        request.into(new ImagePreloadTarget<File>(manager) {
//            @Override
//            public void onLoadStarted(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) {
//                super.onLoadStarted(placeholder);
//            }
//
//            @Override
//            public void onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable Drawable errorDrawable) {
//                super.onLoadFailed(errorDrawable);
//            }

            @Override
            public void onResourceReady(@NonNull File resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super File> transition) {
                super.onResourceReady(resource, transition);
            }
        });

//        request.preload();
    }
}
