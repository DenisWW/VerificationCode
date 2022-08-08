package com.jzsec.web.apptest.service;

import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.renderscript.RenderScript;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.jzsec.web.apptest.R;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class DownLoadService extends Service {
    private final static int UP_PROGRESS = 0x12;
    int progress = 0;
    private Handler handler;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new UPHandler(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifyNotification();
        handler.sendEmptyMessageDelayed(UP_PROGRESS, 1000);
        return super.onStartCommand(intent, flags, startId);
    }

    private void notifyNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(getPackageName(), "foregroundName", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(false);
            channel.setLightColor(Color.RED);
            channel.enableVibration(false);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 100});
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, createNotification());
    }

    private Notification createNotification() {
        //        notification.flags = Notification.FLAG_FOREGROUND_SERVICE;
        if (builder == null) {
            builder = new NotificationCompat.Builder(this, getPackageName())
                    //指定通知的标题内容
                    .setContentTitle("下载")
                    //设置通知的内容
                    .setContentText("正在下载")
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
                    .setSmallIcon(R.mipmap.test)
                    .setProgress(100, progress, false);
//                .setCustomContentView(remoteViews) RemoteViews remoteViews

        }
        builder.setProgress(100, progress, false);
        return builder.build();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification createNotificationNew() {
        //        notification.flags = Notification.FLAG_FOREGROUND_SERVICE;
        return new Notification.Builder(this, getPackageName())
                //指定通知的标题内容
                .setContentTitle("This is content title")
                //设置通知的内容
                .setContentText("This is content text")
                //指定通知被创建的时间
                .setWhen(System.currentTimeMillis())
                //设置通知的小图标
//                .setSmallIcon(R.mipmap.app_icon)
                .setPriority(Notification.PRIORITY_HIGH)
                //设置通知的大图标
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon))
                .setOnlyAlertOnce(true)
                //添加点击跳转通知跳转
//                .setContentIntent(pendingIntent)
                //实现点击跳转后关闭通知
                .setAutoCancel(true)
                .setOngoing(true)
                .setSmallIcon(R.mipmap.test)
                .setProgress(100, progress, false)
//                .setCustomContentView(remoteViews) RemoteViews remoteViews
                .build();
    }

    public static class UPHandler extends Handler {
        private Reference<DownLoadService> reference;

        public UPHandler(DownLoadService downLoadService) {
            super();
            reference = new WeakReference<>(downLoadService);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UP_PROGRESS:
                    if (reference.get() == null) return;
                    reference.get().progress++;
                    reference.get().upProgress();
                    if (reference.get().progress < 100)
                        sendEmptyMessageDelayed(UP_PROGRESS, 1000);
                    break;
            }
        }
    }

    private void upProgress() {
        notificationManager.notify(0, createNotification());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy", "======");

    }
}