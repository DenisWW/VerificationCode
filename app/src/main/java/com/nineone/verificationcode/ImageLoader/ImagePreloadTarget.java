package com.nineone.verificationcode.ImageLoader;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.PreloadTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.util.Synthetic;

public class ImagePreloadTarget <Z> extends CustomTarget<Z> {
    private static final int MESSAGE_CLEAR = 1;
    private static final Handler HANDLER =
            new Handler(
                    Looper.getMainLooper(),
                    new Handler.Callback() {
                        @Override
                        public boolean handleMessage(@NonNull Message message) {
                            if (message.what == MESSAGE_CLEAR) {
                                ((ImagePreloadTarget<?>) message.obj).clear();
                                return true;
                            }
                            return false;
                        }
                    });

    private final RequestManager requestManager;


    public ImagePreloadTarget(RequestManager requestManager) {
        super();
        this.requestManager = requestManager;
    }

    @Override
    public void onResourceReady(@NonNull Z resource, @Nullable Transition<? super Z> transition) {
        HANDLER.obtainMessage(MESSAGE_CLEAR, this).sendToTarget();
    }

    @Override
    public void onLoadCleared(@Nullable Drawable placeholder) {
        // Do nothing, we don't retain a reference to our resource.
    }

    @SuppressWarnings("WeakerAccess")
    @Synthetic
    void clear() {
        requestManager.clear(this);
    }
}
