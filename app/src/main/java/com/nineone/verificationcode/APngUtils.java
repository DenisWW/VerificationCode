package com.nineone.verificationcode;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.widget.ImageView;

import androidx.annotation.RawRes;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class APngUtils {

    public static Map<Integer, StandardAPngDecoder> decoderMap;
    private static APngUtils aPngUtils;
    private Executor executor;

    private APngUtils() {
        decoderMap = new HashMap<>();
        executor = Executors.newFixedThreadPool(5);
    }

    public static APngUtils getInstance() {
        if (aPngUtils == null) aPngUtils = new APngUtils();
        return aPngUtils;
    }

    public StandardAPngDecoder startLikeImageView(final ImageView view, final @RawRes int resId) {
        StandardAPngDecoder decoder;
        if (!decoderMap.containsKey(resId) || (decoder = decoderMap.get(resId)) == null) {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            executor.execute(new DecoderRunnable(view, resId, countDownLatch));
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            decoder = decoderMap.get(resId);
        }
        return decoder;
    }

    public synchronized StandardAPngDecoder getDecoder(final @RawRes int resId) {
        return decoderMap.get(resId);
    }

    public synchronized void addDecoder(final @RawRes int resId, StandardAPngDecoder decoder) {
        decoderMap.put(resId, decoder);
    }

    public static class DecoderRunnable implements Runnable {
        private WeakReference<ImageView> viewRef;
        private int resId;
        private CountDownLatch countDownLatch;

        DecoderRunnable(ImageView viewRef, int resId, CountDownLatch countDownLatch) {
            this.viewRef = new WeakReference<>(viewRef);
            this.resId = resId;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            StandardAPngDecoder decoder = new StandardAPngDecoder();
            Bitmap.Config config = Bitmap.Config.ARGB_8888;
            decoder.setDefaultBitmapConfig(config);
            InputStream inputStream = viewRef.get().getResources().openRawResource(resId);
            try {
                decoder.read(inputStream, inputStream.available());
            } catch (IOException e) {
                e.printStackTrace();
            }
            decoderMap.put(resId, decoder);
            countDownLatch.countDown();
        }
    }


}
