package com.nineone.verificationcode.ImageLoader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.request.RequestOptions;

class ImageLoadOptions implements ILoadOption {
    private BitmapTransformation transformation;
    private BitmapTransformation roundTransformation;
    private final RequestOptions requestOptions;
//    private ImageScaleType scaleType;

    public ImageLoadOptions() {
        requestOptions = new RequestOptions();
    }


    @SuppressLint("CheckResult")
    @Override
    public void placeholder(Integer res) {
        requestOptions.placeholder(res);
    }

    @SuppressLint("CheckResult")
    @Override
    public void scaleType(ImageScaleType scaleType) {
//        this.scaleType = scaleType;
        switch (scaleType) {
            case FIT_CENTER:
                transformation = new FitCenter();
                break;
            case CENTER_CROP:
                transformation = new CenterCrop();
                break;
            case CENTER_INSIDE:
                transformation = new CenterInside();
                break;
            case CIRCLE_CROP:
                transformation = new CircleCrop();
                break;
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void error(int resourceId) {
        requestOptions.error(resourceId);

    }

    @Override
    public void round(float dp) {
        roundTransformation = new GranularRoundedCorners(dp, dp, dp, dp);
    }

    @Override
    public void round(float topLeftDp, float topRightDp, float bottomRightDp, float bottomLeftDp) {
        roundTransformation = new GranularRoundedCorners(topLeftDp, topRightDp, bottomRightDp, bottomLeftDp);
    }

    @Override
    public void cacheType(ImageCacheType cacheType) {
        switch (cacheType) {
            case ALL:
                requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                break;
            case AUTOMATIC:
                requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                break;
            case DATA:
                requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA);
                break;
            case RESOURCE:
                requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                break;
            case NONE:
                requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                break;
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void timeout(int timeoutMs) {
        requestOptions.timeout(timeoutMs);

    }

    @SuppressLint("CheckResult")
    private void setRequest() {
        if (roundTransformation != null && transformation != null) {
            requestOptions.transform(transformation, roundTransformation);
        } else if (transformation != null) {
            requestOptions.transform(transformation);
        } else if (roundTransformation != null) {
            requestOptions.transform(roundTransformation);
        }

    }

    @NonNull
    RequestOptions build() {
        setRequest();
        return requestOptions;
    }
}
