package com.nineone.verificationcode.service;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;


public class DownLoadRunnable implements Runnable {

    private String videoUrl;
    private int videoId;
    private int contentLength;
    private long current;
    private boolean isRunning;
    private volatile boolean isPause;
    private OnDownLoadListener listener;
    private WeakReference<Context> context;
    private WeakReference<Handler> handler;
    static final int UP_UI = 1;
    static final int DOWN_HAVE = 3;
    static final int DOWN_SUCCESS = 2;
    static final int DOWN_ERROR = 4;

    DownLoadRunnable(String videoUrl, int videoId) {
        this.videoUrl = videoUrl;
        this.videoId = videoId;
        isRunning = false;
        isPause = false;
    }

    public void setContextAndHandler(Context context, Handler handler) {
        this.context = new WeakReference<>(context);
        this.handler = new WeakReference<>(handler);
    }

    public void setListener(OnDownLoadListener listener) {
        this.listener = listener;
    }

    void setPause(boolean pause) {
        isPause = pause;
    }

    boolean isPause() {
        return isPause;
    }

    int getContentLength() {
        return contentLength < 0 ? 0 : contentLength;
    }

    long getCurrent() {
        return current < 0 ? 0 : current;
    }

    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void run() {
        outFileStream(videoUrl, videoId);
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "StatementWithEmptyBody", "InfiniteLoopStatement"})
    private void outFileStream(String downLoadUrl, int videoId) {
        File saveFileParent = new File(Environment.getExternalStorageDirectory(), DownLoadService.path);
        String fileName = videoId + ".mp4";
        RandomAccessFile outputStream = null;
        InputStream inputStream = null;
        isRunning = true;
        try {
            if (listener != null) listener.onDownStart(videoId);
            File saveFile = new File(saveFileParent, fileName);
            if (!saveFileParent.exists()) saveFileParent.mkdirs();
            long fileLen = 0;
            if (!saveFile.exists()) {
                saveFile.createNewFile();
            } else {
                fileLen = saveFile.length();
                current = fileLen;
            }

            URL url = new URL(downLoadUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.addRequestProperty("Charset", "UTF-8");
//            断点续传
//            connection.addRequestProperty("Range", "bytes="+2+"-");
            connection.setConnectTimeout(5000);
            connection.connect();
            contentLength = connection.getContentLength();
            if (fileLen != 0 && contentLength == fileLen) {
                if (handler != null && handler.get() != null) {
                    handler.get().removeMessages(UP_UI);
                    Message message = Message.obtain();
                    message.what = DOWN_HAVE;
                    message.arg1 = videoId;
                    handler.get().sendMessage(message);
                }
                return;
            }
            inputStream = connection.getInputStream();
            outputStream = new RandomAccessFile(saveFile, "rws");
            if (fileLen != 0) {
                inputStream.skip(fileLen);
                outputStream.seek(fileLen);
            }
            byte[] bytes = new byte[1024];
            int len;

            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                current += len;
                if (listener != null) listener.onDownLoading(current, videoId);
//                if (isPause) {
//                    for (; ; ) {
//                    }
//                }
                while (isPause) {
                }
            }
            if (saveFile.length() == contentLength) {
                if (listener != null) listener.onDownComplete(videoId);
                if (handler != null && handler.get() != null) {
                    handler.get().removeMessages(UP_UI);
                    Message message = Message.obtain();
                    message.what = DOWN_SUCCESS;
                    message.arg1 = videoId;
                    handler.get().sendMessage(message);
                }
            } else {
                saveFile.delete();
                outFileStream(downLoadUrl, videoId);
                if (listener != null) listener.onDownAgain(videoId);
                toastText("下载失败，重新下载中...");
            }

        } catch (Exception e) {
            if (listener != null) listener.onDownError(videoId);
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        isRunning = false;

    }

    private void toastText(String s) {

        if (context != null) {
            Looper.prepare();
//            ToastUtils.showToast(context.get(), s);
            Looper.loop();
        }
    }

    public interface OnDownLoadListener {
        void onDownStart(int videoId);

        void onDownError(int videoId);

        void onDownLoading(long current, int videoId);

        void onDownComplete(int videoId);

        void onDownAgain(int videoId);

    }

}
