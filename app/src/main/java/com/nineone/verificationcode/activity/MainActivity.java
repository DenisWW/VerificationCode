package com.nineone.verificationcode.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.annotation.ActAnnotation;
import com.nineone.verificationcode.apng.APngImageView;
import com.nineone.verificationcode.R;
import com.nineone.verificationcode.utils.Utils;
import com.nineone.verificationcode.view.DragImageView;
import com.nineone.verificationcode.view.ParentViewGroup;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static android.media.MediaCodecList.REGULAR_CODECS;

@ActAnnotation(name = "TestMainActivity")
public class MainActivity extends Activity {
    private SeekBar seekBar;
    private DragImageView mine_iv;
    private APngImageView gif_iv;
    private String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private ParentViewGroup parentViewGroup;
    private EditText edit_view;
    private MediaFormat mOutputFormat;
    private final int TIMEOUT_USEC = 2500;
    MediaMuxer mMuxer;
    private HashMap<String, String> hashMap = new HashMap<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            for (int i : grantResults) {
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String id = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        initImageLoader(this);
        seekBar = findViewById(R.id.seek_bar);
        mine_iv = findViewById(R.id.mine_iv);
        gif_iv = findViewById(R.id.gif_iv);
        edit_view = findViewById(R.id.edit_view);
        final ImageView circle = findViewById(R.id.circle);
        seekBar.setMax(1000);
        mine_iv.getBgImageView().setImageResource(R.mipmap.loading);
//       SurfaceView view_surface = findViewById(R.id.view_surface);
//        view_surface.getHolder().addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder holder) {
//                Log.e(" surfaceCreated", "===" + holder.getSurface());
//                MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this,
//                        Uri.parse("https://wsmedia.iyingdi.cn/video/2020/08/10/504fca6f-c079-4027-aacf-d5defeae859a.mp4"),
//                        holder);//创建MediaPlayer对象
//                mediaPlayer.start();
//
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//                Log.e(" surfaceChanged", "===" + holder.getSurface());
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder holder) {
//                Log.e(" surfaceDestroyed", "===" + holder.getSurface());
//            }
//        });
        Handler handler = new Handler();
//        handler.sendEmptyMessage();
        mine_iv.getProgressImageView().setImageResource(R.mipmap.loading);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mine_iv.setOffset(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        findViewById(R.id.testView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        parentViewGroup = findViewById(R.id.parent);

        setAdapter();
        Utils.addActivity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 200);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 100);
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        }
        Lock lock = new ReentrantLock();

//        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        Log.e("Location", "===" + location);
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                VideoSlimEncoder videoSlimEncoder = new VideoSlimEncoder();
//                videoSlimEncoder.convertVideo(Environment.getExternalStorageDirectory() + "/DCIM/Camera/VID_20200730_180248.mp4",
//                        Environment.getExternalStorageDirectory() + "/12345.mp4", 1000, 1920, 9600, new VideoSlimEncoder.SlimProgressListener() {
//                            @Override
//                            public void onProgress(float percent) {
//                            }
//                        }
//
//                );
//            }
//        }.start();
// -crf 30 -ac 2 -ab 47k
//        String text = "ffmpeg -y -i /storage/emulated/0/dy.mp4 -b 900k -s 852x480 -r 30 -preset superfast /storage/emulated/0/123.mp4";
//        String text = "ffmpeg -y -i /storage/emulated/0/soul/1597047206579.mp4 -b 500k -crf 30 -r 30 -preset superfast /storage/emulated/0/123.mp4";
//        String text = "ffmpeg -y -i /storage/emulated/0/VID_20200901_170143.mp4 -b 1200k -s 720x1280 -r 30 -preset superfast /storage/emulated/0/123.mp4";
//        RxFFmpegInvoke.getInstance().runCommandAsync(text.split(" "), new RxFFmpegInvoke.IFFmpegListener() {
//            @Override
//            public void onFinish() {
//
//            }
//
//            @Override
//            public void onProgress(int progress, long progressTime) {
//                Log.e("onProgress", "==" + progress);
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(String message) {
//
//            }
//        });
//        try {
//            mMuxer = new MediaMuxer(Environment.getExternalStorageDirectory() + "/why.mp4", MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        Intent intent = new Intent(this, VideoCompressionService.class);
//        intent.putExtra(VIDEO_PATH, "/storage/emulated/0/DCIM/Camera/VID_20200730 _ 180248.mp4");
//        intent.putExtra(VIDEO_PATH, "/storage/emulated/0/DCIM/ScreenRecorder/Screenrecorder-2020-08-29-00-47-02-56.mp4");
//        intent.putExtra(VIDEO_PATH, "/storage/emulated/0/soul/1597047206579.mp4");
//        startService(intent);


//        new Thread() {
//            @SuppressLint("NewApi")
//            @Override
//            public void run() {
//                super.run();
//                ContentResolver contentResolver = getContentResolver();
//                Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{
//                        MediaStore.Video.Media.DATA,
//                        MediaStore.Video.Media.WIDTH,
//                        MediaStore.Video.Media.HEIGHT,
//                        MediaStore.Video.Media.TITLE,
//                        MediaStore.Video.Media.MIME_TYPE,
//                        MediaStore.Video.Media.SIZE
//                }, null, null, null);
//                while (cursor != null && cursor.moveToNext()) {
//                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
//                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//                    try {
//                        mmr.setDataSource(path);
//                        Log.e("path==", path
//                                        + "    HEIGHT===" + cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.HEIGHT))
//                                        + "    WIDTH===" + cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.WIDTH))
//                                        + "    SIZE===" + cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))
////                            + "    KEY_BIT_RATE===" + (mediaFormat != null ? mediaFormat.toString() : "  ==")
////                                        + "    KEY_BIT_RATE===" + (Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)))
//                        );
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//
//                    File file = new File(path);
//                    if (!file.exists()) {
//                        Uri uri = Uri.fromFile(file);
//                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                        intent.setData(uri);
//                        sendBroadcast(intent);
//                    }
//
//                }
//
//                if (cursor != null && !cursor.isClosed()) cursor.close();
//            }
//        }.start();
//        VideoView videoView = findViewById(R.id.video_view);
//        videoView.setVideoPath("https://wsmedia.iyingdi.cn/video/2020/08/10/504fca6f-c079-4027-aacf-d5defeae859a.mp4");
//        videoView.start();
    }

    public native String stringFromJNI();

    private MediaFormat getMediaFormatFrom(String s) {
        MediaExtractor extractor = new MediaExtractor();
        MediaFormat mf = null;
        try {
//            File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/Camera/VID_20200730_180248.mp4");
//            File file = new File(Environment.getExternalStorageDirectory(), "s");
//            extractor.setDataSource(file.getAbsolutePath());
            extractor.setDataSource(s);
//            extractor.setDataSource(file.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int numTracks = extractor.getTrackCount();
        for (int i = 0; i < numTracks; i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime != null && mime.contains("video")) {
                mf = format;
            }
        }
        return mf;
    }

    private static void selectCodec() {
        MediaCodecList list = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            list = new MediaCodecList(REGULAR_CODECS);
            MediaCodecInfo[] infos = list.getCodecInfos();
            for (MediaCodecInfo m : infos) {
                for (String type : m.getSupportedTypes()) {
                    Log.e("type  ====", "the selected encoder is :" + type);
                }
            }
        }
    }


    private RecyclerView recycler;
    private RecyclerView recycler1;
    private SimpleAdapter adapter;


    private void setAdapter() {
        recycler = findViewById(R.id.recycler);
        recycler1 = findViewById(R.id.recycler1);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler1.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter = new SimpleAdapter());
        recycler1.setAdapter(adapter);
        adapter.setList();
        WebView webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });
    }


    public void junm1(View view) {
        Intent intent = new Intent(this, BesselActivity.class);
        startActivity(intent);
    }

    public void junm2(View view) {
        Intent intent = new Intent(this, MineActivity.class);
        startActivity(intent);
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public void clickStart(View view) {
//        if (parentViewGroup.isOpenRightView()) {
//            parentViewGroup.closeRightLayout();
//        } else {
//            parentViewGroup.openRightLayout();
//        }
//        parentViewGroup.setList();

//        gif_iv.start();

        SpannableString spannableString = new SpannableString("我爱你");
        SpData spData = new SpData();
        spannableString.setSpan(spData, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.colorPrimary)), 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        Log.e("edit_view", "===" + edit_view.getSelectionStart());
//        edit_view.setText(spannableString, TextView.BufferType.SPANNABLE);
//        edit_view.getText().setSpan(spannableString, edit_view.getSelectionStart() > 0 ? edit_view.getSelectionStart() : 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        edit_view.getText().insert(edit_view.getSelectionStart() > 0 ? edit_view.getSelectionStart() : 0, spannableString);
//        edit_view.getText().setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.colorPrimary)), 0, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        edit_view.getText().setSpan(new ClickableSpan() {
//            @Override
//            public void onClick(@NonNull View widget) {
//                Toast.makeText(MainActivity.this, "点击了", Toast.LENGTH_LONG).show();
//            }
//        }, 0, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        edit_view.setMovementMethod(LinkMovementMethod.getInstance());
        SpData[] spanneds = edit_view.getText().getSpans(0, edit_view.getText().length(), SpData.class);
        for (SpData spData1 : spanneds) {
            int start = edit_view.getText().getSpanStart(spData1);
            int end = edit_view.getText().getSpanEnd(spData1);
            Log.e("spData1", "==" + spData1 + "    start=" + start + "    end=" + end);
        }
    }


    private class SimpleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<String> list;

        public SimpleAdapter() {

        }

        public void setList() {
            list = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                list.add("i=====" + i);
            }

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(new TextView(getApplicationContext()));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            MyViewHolder viewHolder = (MyViewHolder) holder;
            viewHolder.textView.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }

    public class SpData {
        private CharSequence showContent;
        private Object customSpan;
        private int start;
        private int end;
        private int userId;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public Object getCustomSpan() {
            return customSpan;
        }

        private void setCustomSpan(Object customSpan) {
            this.customSpan = customSpan;
        }

        public int getStart() {
            return start;
        }

        private void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        private void setEnd(int end) {
            this.end = end;
        }

        public CharSequence getShowContent() {
            return showContent;
        }

        private void setShowContent(CharSequence showContent) {
            this.showContent = showContent;
        }

    }

    private static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test1.h264";
    private static int yuvqueuesize = 10;

    public static ArrayBlockingQueue<byte[]> YUVQueue = new ArrayBlockingQueue<byte[]>(yuvqueuesize);

    /**
     * 无论参数是否为null返回结果一定不为null
     */
    @NotNull
    public String getTest(@Nullable String tests) {
        return tests != null ? tests : "";
    }
}
