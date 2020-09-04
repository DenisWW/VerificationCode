//package com.nineone.verificationcode.service;
//
//import android.app.Service;
//import android.content.Intent;
//import android.media.MediaExtractor;
//import android.media.MediaFormat;
//import android.media.MediaMetadataRetriever;
//import android.media.MediaRecorder;
//import android.media.MediaScannerConnection;
//import android.net.Uri;
//import android.os.Build;
//import android.os.FileUtils;
//import android.os.IBinder;
//import android.provider.MediaStore;
//import android.text.TextUtils;
//import android.util.Log;
//
//import androidx.localbroadcastmanager.content.LocalBroadcastManager;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.file.Files;
//
//import io.microshow.rxffmpeg.RxFFmpegCommandList;
//import io.microshow.rxffmpeg.RxFFmpegInvoke;
//import io.microshow.rxffmpeg.RxFFmpegSubscriber;
//
//public class VideoCompressionService extends Service implements RxFFmpegInvoke.IFFmpegListener {
//
//    private String cacheVideoPath = "/storage/emulated/0/iyingdi/video_cache/";
//    private String cacheVideoMP4 = ".mp4";
//    public static String VIDEO_PATH = "VIDEO_PATH";
//    //  MP4，分辨率：320*240 480*360 640*480
//    private static int[] BIG_MP4_WH = new int[2];
//    private String name;
//
//    public VideoCompressionService() {
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        Log.e("onCreate", "===");
//        BIG_MP4_WH[0] = 1280;
//        BIG_MP4_WH[1] = 720;
//        File file = new File(cacheVideoPath);
//        if (!file.exists()) {
//            boolean b = file.mkdirs();
//        }
//    }
//
//    private String path;
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        path = intent.getStringExtra(VIDEO_PATH);
//        Log.e("onStartCommand", "====" + Thread.currentThread().getName());
//        if (!TextUtils.isEmpty(path) && path != null) {
//            new Thread() {
//                @Override
//                public void run() {
//                    super.run();
//
//                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//                    mmr.setDataSource(path);
//                    int rotation = 0, width = 0, height = 0;
//
//                    try {
//                        rotation = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));
//                        width = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
//                        height = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
//                    } catch (Exception ignore) {
//                    }
//
//                    int[] wh = findBestWH1(width, height);
//                    String s = wh[0] + "x" + wh[1];
//                    if (rotation == 90 || rotation == 270) s = wh[1] + "x" + wh[0];
//                    name = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
//                    String text = "ffmpeg -y -i " + path + " -s " + s + " -b 500k -r 30 -preset superfast " + cacheVideoPath + name + cacheVideoMP4;
//                    Log.e("text", "===" + text);
//                    String[] commands = getBoxblur(path, s, cacheVideoPath + name + cacheVideoMP4);
//                    RxFFmpegInvoke.getInstance().runCommand(commands, VideoCompressionService.this
//                    );
//                }
//            }.start();
//
//        } else {
//            stopSelf();
//        }
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    public static String[] getBoxblur(String path, String s, String out) {
//        RxFFmpegCommandList cmdlist = new RxFFmpegCommandList();
//        cmdlist.append("-i");
//        cmdlist.append(path);
//        cmdlist.append("-s");
//        cmdlist.append(s);
//        cmdlist.append("-b");
//        cmdlist.append("500k");
//        cmdlist.append("-r");
//        cmdlist.append("30");
//        cmdlist.append("-preset");
//        cmdlist.append("superfast");
//        cmdlist.append(out);
//        return cmdlist.build(true);
//    }
//
//    private int[] findBestWH(int width, int height) {
//        int[] wh = new int[2];
//        float zoom;
//        if (width > height) {
//            zoom = width * 1f / BIG_MP4_WH[0];
//            wh[0] = BIG_MP4_WH[0];
//            wh[1] = (int) (height / zoom);
//        } else {
//            zoom = height * 1f / BIG_MP4_WH[0];
//            wh[0] = (int) (width / zoom);
//            wh[1] = BIG_MP4_WH[0];
//        }
//
//        return wh;
//    }
//
//    private int[] findBestWH1(int width, int height) {
//        int[] wh = new int[2];
//        if (width > height) {
//            wh[0] = BIG_MP4_WH[0];
//            wh[1] = BIG_MP4_WH[1];
//        } else {
//            wh[0] = BIG_MP4_WH[1];
//            wh[1] = BIG_MP4_WH[0];
//        }
//
//        return wh;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
//
//    @Override
//    public void onFinish() {
//
//    }
//
//    @Override
//    public void onProgress(int progress, long progressTime) {
//        Log.e("onProgress", "====" + progress);
//    }
//
//    @Override
//    public void onCancel() {
//
//    }
//
//    @Override
//    public void onError(String message) {
//        Log.e("onError", "===" + message);
//
//    }
//}
