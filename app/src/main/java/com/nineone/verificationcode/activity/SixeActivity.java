package com.nineone.verificationcode.activity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.stream.HttpGlideUrlLoader;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.nineone.verificationcode.ImageLoader.ImageCacheType;
import com.nineone.verificationcode.ImageLoader.ImageLoader;
import com.nineone.verificationcode.ImageLoader.ImageScaleType;
import com.nineone.verificationcode.R;

import java.io.File;
import java.util.List;

public class SixeActivity extends AppCompatActivity {

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sixe);
        findViewById(R.id.tv).setOnClickListener(v -> {
            ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                List<ActivityManager.AppTask> list = am.getAppTasks();
                for (int i = 0; i < list.size(); i++) {
                    ActivityManager.AppTask appTask = list.get(i);
                    Log.e("am", "==" + appTask.getTaskInfo().toString());
                }

            }

            Intent intent = new Intent(SixeActivity.this, ThreeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
//        WebView webView;
//        MultiTransformation
//        Glide.with(this).downloadOnly().load(new RequestListener<File>() {
//            @Override
//            public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
//                return false;
//            }
//        });
//        Glide.with(this).load("")
//                .placeholder(R.mipmap.demo2)
//                .error(R.mipmap.demo2)
//                .circleCrop()
//                .optionalCenterCrop()
//                .centerCrop()
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        return false;
//                    }
//                })
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                .into(new CustomTarget<Drawable>() {
//                    @Override
//                    public void onStart() {
//                        super.onStart();
//                    }
//
//                    @Override
//                    public void onStop() {
//                        super.onStop();
//                    }
//
//                    @Override
//                    public void onLoadStarted(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) {
//                        super.onLoadStarted(placeholder);
//                    }
//
//                    @Override
//                    public void onResourceReady(@NonNull Drawable resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super Drawable> transition) {
//
//                    }
//
//                    @Override
//                    public void onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable Drawable errorDrawable) {
//                        super.onLoadFailed(errorDrawable);
//                    }
//
//                    @Override
//                    public void onLoadCleared(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) {
//
//                    }
//
//                });
        Context context = this;
//        Glide.with(context)
//                .load("myUrl")
//                .placeholder(R.mipmap.placeholde)
//                .fitCenter()
//                .into(imageView);
//        DoKitSPUtil
//        ImageLoader.load(context, R.string.app_name)
//                .placeholder(R.mipmap.demo2)
//                .round(15.f)
//                .timeout(100)
//                .error(R.mipmap.demo2)
//                .scaleType(ImageScaleType.CENTER_INSIDE)
//                .diskCacheStrategy(ImageCacheType.ALL)
//        ;
//                .into(new ImageView(this));

//        ImageLoader.clear(context, new ImageView(this));
//        ImageLoader.downLoad(context, "", "").down();
//        HttpGlideUrlLoader httpGlideUrlLoader;
    }
}