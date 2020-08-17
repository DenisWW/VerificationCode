package com.nineone.verificationcode.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

import io.microshow.rxffmpeg.RxFFmpegInvoke;
import io.microshow.rxffmpeg.RxFFmpegSubscriber;

public class VideoCompressionService extends Service {

    private String cacheVideoPath = "/storage/emulated/0/iyingdi/video_cache/";
    private String cacheVideoMP4 = ".mp4";
    public static String VIDEO_PATH = "VIDEO_PATH";
    //  MP4，分辨率：320*240 480*360 640*480
    private static int[] BIG_MP4_WH = new int[2];
    private String name;

    public VideoCompressionService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("onCreate", "===");
        BIG_MP4_WH[0] = 640;
        BIG_MP4_WH[1] = 480;
        File file = new File(cacheVideoPath);
        if (!file.exists()) {
            boolean b = file.mkdirs();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String path = intent.getStringExtra(VIDEO_PATH);
        if (!TextUtils.isEmpty(path) && path != null) {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(path);

            Log.e("MediaMetadataRetriever", "==="
                    + "    METADATA_KEY_BITRATE=" + mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)
                    + "    METADATA_KEY_VIDEO_ROTATION=" + mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)
                    + "    METADATA_KEY_VIDEO_WIDTH=" + mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
                    + "    METADATA_KEY_VIDEO_HEIGHT=" + mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
            );

            int rotation = 0, width = 0, height = 0;
            try {
                rotation = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));
                width = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                height = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            } catch (Exception ignore) {
            }

            int[] wh = findBestWH(width, height);
            String s = wh[0] + "x" + wh[1];
            if (rotation == 90 || rotation == 270) s = wh[1] + "x" + wh[0];
            name = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
//            String text = "ffmpeg -y -i " + path + " -b:v 400k -bufsize 200k -bt 200k -s 320x640 -r 30 -preset superfast " + cacheVideoPath + name + cacheVideoMP4;
            String text = "ffmpeg -y -i " + path + " -b 500k -s " + s + " -r 30 -preset superfast " + cacheVideoPath + name + cacheVideoMP4;
            Log.e("压缩命令:", "===" + text);
            String[] commands = text.split(" ");
            RxFFmpegInvoke.getInstance()
                    .runCommandRxJava(commands)
                    .subscribe(new RxFFmpegSubscriber() {
                        @Override
                        public void onFinish() {
                            String filePath = cacheVideoPath + name + cacheVideoMP4;
                            File file = new File(filePath);
                            Log.e(VideoCompressionService.this.getClass().getName() + "  onFinish", "======" + filePath);

                        }

                        @Override
                        public void onProgress(int progress, long progressTime) {
                            Log.e(VideoCompressionService.this.getClass().getName() + "onProgress", "===" + progress + "      ==" + progressTime);
                        }

                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onError(String message) {
                            Log.e("onError", "======" + message);
                        }
                    });

        } else {
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }


    private int[] findBestWH(int width, int height) {
        int[] wh = new int[2];
        float zoom;
        if (width > height) {
            zoom = width * 1f / BIG_MP4_WH[0];
            wh[0] = BIG_MP4_WH[0];
            wh[1] = (int) (height / zoom);
        } else {
            zoom = height * 1f / BIG_MP4_WH[0];
            wh[0] = (int) (width / zoom);
            wh[1] = BIG_MP4_WH[0];
        }

        return wh;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
