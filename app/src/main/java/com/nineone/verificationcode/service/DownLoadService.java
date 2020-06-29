package com.nineone.verificationcode.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;
import static com.nineone.verificationcode.service.DownLoadRunnable.DOWN_HAVE;
import static com.nineone.verificationcode.service.DownLoadRunnable.DOWN_SUCCESS;
import static com.nineone.verificationcode.service.DownLoadRunnable.UP_UI;


public class DownLoadService extends Service {
    private static String stopAndStart = "stopAndStart";
    private static Executor executor = Executors.newFixedThreadPool(3);
    public static final String path = ".nineone" + File.separator + "videoDownload" + File.separator;

    private static Handler upUIHandler;
    private Map<Integer, RemoteViews> downLoadViews;
    private Map<Integer, Notification> downLoadNotifications;
    //    private Map<Integer, SimpleDownLoadBean> downLoadBeans;
    private Map<Integer, DownLoadRunnable> downLoadRunnable;

    private String channel_id;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    MyBroadcastReceiver myBroadcastReceiver;

    @SuppressLint("UseSparseArrays")
    private void init() {
        channel_id = getPackageName();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(stopAndStart);
        myBroadcastReceiver = new MyBroadcastReceiver();
        registerReceiver(myBroadcastReceiver, intentFilter);
        upUIHandler = new MyUpUIHandler(this);
        downLoadViews = new Hashtable<>();
        downLoadNotifications = new Hashtable<>();
        downLoadRunnable = new Hashtable<>();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myBroadcastReceiver != null) {
            unregisterReceiver(myBroadcastReceiver);
        }
    }

    NotificationManager notificationManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int videoId = intent.getIntExtra("1111", 0);
//        if (!downLoadBeans.containsKey(videoId) && downLoadBeans.size() < 3) {
//            ToastUtils.showToast(this, "开始下载");
//            String videoTitle = intent.getStringExtra(Config.VIDEO_TITLE);
//            String videoPic = intent.getStringExtra(Config.VIDEO_PIC);
//            String videoUrl = intent.getStringExtra(Config.VIDEO_URL);
//            int videoRecord = intent.getIntExtra(Config.VIDEO_RECORD, 0);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(channel_id, "foregroundName", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(false);
            channel.setLightColor(Color.RED);
            channel.enableVibration(false);
//            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 100});
            notificationManager.createNotificationChannel(channel);
        }
//            RemoteViews remoteViews = createRemoteViews(videoId, videoTitle);
//            Notification notification = createNotification(remoteViews);
//            notification.flags = FLAG_ONLY_ALERT_ONCE | FLAG_FOREGROUND_SERVICE;
//            notificationManager.notify(videoId, notification);
//            addHandler(remoteViews, notification, videoId, videoPic, videoTitle, videoUrl, videoRecord);
//        } else {
//            if (downLoadBeans.size() == 3) {
//                ToastUtils.showToast(this, "同时最多下载三个任务");
//            } else {
//                ToastUtils.showToast(this, "已经在队列中");
//            }
//        }
        return super.onStartCommand(intent, flags, startId);
    }

//    private RemoteViews createRemoteViews(int videoId, String videoTitle) {
//        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.down_loading_notification_layout);
//        remoteViews.setProgressBar(R.id.progress, 100, 0, false);
//        remoteViews.setTextViewText(R.id.progress_s, 0 + " %");
//        remoteViews.setTextViewText(R.id.title, videoTitle);
//
//        Intent broad = new Intent();
//        broad.setAction(stopAndStart);
//        broad.putExtra(Config.VIDEO_ID, videoId);
//        int requestCode = 10;
//        remoteViews.setOnClickPendingIntent(R.id.down_state, PendingIntent.getBroadcast(this, requestCode, broad, FLAG_CANCEL_CURRENT));
//        return remoteViews;
//    }

    private Notification createNotification(RemoteViews remoteViews) {
        //        notification.flags = Notification.FLAG_FOREGROUND_SERVICE;
        return new NotificationCompat.Builder(this, channel_id)
                //指定通知的标题内容
                .setContentTitle("This is content title")
                //设置通知的内容
                .setContentText("This is content text")
                //指定通知被创建的时间
                .setWhen(System.currentTimeMillis())
                //设置通知的小图标
//                .setSmallIcon(R.mipmap.app_icon)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                //设置通知的大图标
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon))
                .setOnlyAlertOnce(true)
                //添加点击跳转通知跳转
//                .setContentIntent(pendingIntent)
                //实现点击跳转后关闭通知
                .setAutoCancel(true)
                .setOngoing(true)
                .setCustomContentView(remoteViews)
                .build();
    }

    private void addHandler(RemoteViews remoteViews, Notification notification,
                            int videoId,
                            String videoPic,
                            String videoTitle,
                            String videoUrl,
                            int videoRecord
    ) {
        DownLoadRunnable runnable = new DownLoadRunnable(videoUrl, videoId);
        downLoadViews.put(videoId, remoteViews);
        downLoadNotifications.put(videoId, notification);
//        downLoadBeans.put(videoId, new SimpleDownLoadBean(videoId, videoTitle, videoPic, videoUrl, videoRecord));
        downLoadRunnable.put(videoId, runnable);
        runnable.setContextAndHandler(this, upUIHandler);
        executor.execute(runnable);
        upUIHandler.removeMessages(UP_UI);
        upUIHandler.sendEmptyMessage(UP_UI);
    }

    private DownLoadRunnable.OnDownLoadListener downLoadListener = new DownLoadRunnable.OnDownLoadListener() {


        @Override
        public void onDownStart(int videoId) {

        }

        @Override
        public void onDownError(int videoId) {

        }

        @Override
        public void onDownLoading(long current, int videoId) {

        }

        @Override
        public void onDownComplete(int videoId) {

        }

        @Override
        public void onDownAgain(int videoId) {

        }
    };

    public static class MyUpUIHandler extends Handler {
        private Reference<DownLoadService> reference;

        MyUpUIHandler(DownLoadService reference) {
            this.reference = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == UP_UI) {
                DownLoadService service = reference.get();
                if (service != null) {
                    boolean isRun = false;
                    for (Integer videoId : service.downLoadViews.keySet()) {
                        RemoteViews remoteViews = service.downLoadViews.get(videoId);
                        if (remoteViews != null) {
                            DownLoadRunnable runnable = service.downLoadRunnable.get(videoId);
                            if (runnable != null && runnable.getContentLength() > 0) {
//                                remoteViews.setProgressBar(R.id.progress, runnable.getContentLength() / 1000, (int) runnable.getCurrent() / 1000, false);
//                                remoteViews.setTextViewText(R.id.progress_s, runnable.getCurrent() * 100 / runnable.getContentLength() + " %");
//                                remoteViews.setTextViewText(R.id.down_state, runnable.isPause() ? "开始" : "暂停");
                                service.notificationManager.notify(videoId, service.downLoadNotifications.get(videoId));
                            }
                        }
                    }
//                    Log.e("isRun", "==" + isRun);
//                    if (isRun)
                    this.sendEmptyMessageDelayed(UP_UI, 1000);
                }
            } else if (msg.what == DOWN_SUCCESS) {
                DownLoadService service = reference.get();
                if (service != null) {
                    int videoId = msg.arg1;
                    if (videoId > 0) {
//                        ToastUtils.showToast(service, "下载成功，请到我的下载查看！");
//                        MyDatabasesManager.getInstance(service).addVideoDataByDown(service.downLoadBeans.get(videoId));
//                        service.notificationManager.cancel(videoId);
//                        service.downLoadBeans.remove(videoId);
//                        service.downLoadRunnable.remove(videoId);
//                        service.downLoadNotifications.remove(videoId);
//                        service.downLoadViews.remove(videoId);

                    }
                }
                sendEmptyMessageDelayed(UP_UI, 1000);

            } else if (msg.what == DOWN_HAVE) {
                DownLoadService service = reference.get();
                if (service != null) {
                    int videoId = msg.arg1;
                    if (videoId > 0) {
//                        MyDatabasesManager.getInstance(service).addVideoDataByDown(service.downLoadBeans.get(videoId));
//                        service.notificationManager.cancel(videoId);
//                        service.downLoadBeans.remove(videoId);
//                        service.downLoadRunnable.remove(videoId);
//                        service.downLoadNotifications.remove(videoId);
//                        service.downLoadViews.remove(videoId);
                    }
                }
                sendEmptyMessageDelayed(UP_UI, 1000);

            }
        }
    }


    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equalsIgnoreCase(stopAndStart)) {
                int videoId = intent.getIntExtra("www", 0);
                if (videoId != 0) {
                    DownLoadRunnable runnable = downLoadRunnable.get(videoId);
                    if (runnable != null) runnable.setPause(!runnable.isPause());
                }
            }
        }
    }

    @Override
    public ComponentName startService(Intent service) {
        return super.startService(service);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    //断点下载 流截取
    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    private void outFileStream(InputStream inputStream, int videoId) {
        File saveFileParent = new File(Environment.getExternalStorageDirectory(), path);
        String fileName = videoId + ".mp4";
        RandomAccessFile outputStream = null;
        try {
            if (saveFileParent.exists()) saveFileParent.mkdirs();
            File saveFile = new File(saveFileParent, fileName);
            if (!saveFile.exists()) {
                saveFile.createNewFile();
                outputStream = new RandomAccessFile(saveFile, "rws");
            } else {
                outputStream = new RandomAccessFile(saveFile, "rws");
                long fileLen = saveFile.length();
                inputStream.skip(fileLen);
                outputStream.seek(fileLen);
            }
            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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

    }


}
