package com.nineone.verificationcode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import java.util.concurrent.ThreadFactory;

public class APngImageView extends androidx.appcompat.widget.AppCompatImageView {
    StandardAPngDecoder decoder;
    public Handler mHandler;
    private boolean isStart;
    private DrawThread drawThread;

    public APngImageView(Context context) {
        super(context);
        init();
    }

    private void init() {
        isStart = false;
        this.mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                setImageBitmap((Bitmap) msg.obj);
                if (msg.what != 0) {
                    setVisibility(GONE);
                }
                return true;
            }
        });

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.e("Runnable",  Thread.currentThread().getName()+"     "+ Looper.getMainLooper().getThread().getName());
            }
        });

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public APngImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public void start() {
        if (!isStart) {
            AnimImageLoadTask task = new AnimImageLoadTask(this, R.mipmap.fire_bitmap);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            isStart = true;
        } else {
            Log.e("drawThread1111", "==" + drawThread.isInterrupted() + "    ==");
            if (!drawThread.isInterrupted()) drawThread.interrupt();
            Log.e("drawThread22", "==" + drawThread.isInterrupted() + "    ==");
        }
    }


    private class AnimImageLoadTask extends AsyncTask<Void, Void, Integer> {
        private final WeakReference<APngImageView> viewRef;
        private final int resMaP;
        private Bitmap bitmap;

        AnimImageLoadTask(APngImageView view, int res) {
            this.viewRef = new WeakReference<>(view);
            this.resMaP = res;
            decoder = new StandardAPngDecoder();
            Bitmap.Config config = Bitmap.Config.ARGB_8888;
            decoder.setDefaultBitmapConfig(config);

        }

        @SuppressLint("WrongThread")
        @Override
        public Integer doInBackground(Void... voidArr) {
            InputStream inputStream = null;
            try {
                try {
                    inputStream = viewRef.get().getResources().openRawResource(resMaP);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (inputStream == null) {
                    return null;
                }
                decoder.read(inputStream, inputStream.available());
                decoder.resetFrameIndex();
                decoder.advance();
                bitmap = decoder.getNextFrame();
//                if (bitmap != null) {
//                    // 图片过大时进行缩放
//                    if (viewRef.get() != null) {
//                        int max = viewRef.get().maxTextureSize;
//                        if (max > 0 && (bitmap.getWidth() > max || bitmap.getHeight() > max)) {
//                            bitmap = onlyScaleBitmap(bitmap, max);
//                        }
//                    }
                return 0;
//                } else {
//                    exception = new FileNotFoundException();
//                    return null;
//                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } catch (OutOfMemoryError error) {
                error.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(Integer num) {
            ImageView view = viewRef.get();
            if (view == null) {
                return;
            }
            view.setImageBitmap(bitmap);
            drawThread = new DrawThread();
            setVisibility(VISIBLE);
            drawThread.start();
//                if (preview) {
//                    view.onPreviewLoaded(bitmap);
//                } else {
//                    view.onAnimImageLoaded(bitmap, num, animDecoder);
//                }
//            } else if (exception != null && view.onImageEventListener != null) {
//                if (preview) {
//                    view.onImageEventListener.onPreviewLoadError(exception);
//                } else {
//                    view.onImageEventListener.onImageLoadError(exception);
//                }
//            }
        }
    }

    public class DrawThread extends Thread {

        public DrawThread() {
        }

        public void run() {

            try {
                if (decoder != null) {
                    decoder.resetFrameIndex();
                    while (isStart) {
                        if (decoder != null) {
                            long startTime = SystemClock.elapsedRealtime();
                            int i = decoder.advance();
                            Bitmap bitmap = decoder.getNextFrame();
                            if (bitmap != null) {
                                Message msg = Message.obtain();
                                msg.obj = bitmap;
                                msg.what = i == decoder.getFrameCount() - 1 ? i : 0;
                                mHandler.sendMessage(msg);
                                long f = ((long) decoder.getNextDelay()) - (SystemClock.elapsedRealtime() - startTime);
                                if (f <= 0) {
                                    f = 0;
                                }
                                Log.e("decoder.getNextDelay()", "==" + decoder.getNextDelay()
                                        + "   SystemClock.elapsedRealtime()==" + SystemClock.elapsedRealtime()
                                        + "   startTime==" + startTime
                                        + "   f==" + f
                                );
//                                SystemClock.sleep(f);
                                Thread.sleep(f);
                            }

                            if (i == decoder.getFrameCount() - 1) {
                                isStart = false;
                                break;
                            }
                        }
                    }
//                if (decoder != null) {
//                    decoder.clear();
//                    decoder = null;
//                }
                }
            } catch (InterruptedException e) {
                isStart = false;
                e.printStackTrace();
            }

        }
    }

}
