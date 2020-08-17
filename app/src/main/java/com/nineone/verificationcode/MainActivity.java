package com.nineone.verificationcode;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.os.CancellationSignal;
import androidx.core.view.ViewParentCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
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
import android.widget.Toast;
import android.widget.VideoView;

import com.example.annotation.ActAnnotation;
import com.nineone.verificationcode.activity.BesselActivity;
import com.nineone.verificationcode.service.VideoCompressionService;
import com.nineone.verificationcode.utils.CodecInputSurface;
import com.nineone.verificationcode.utils.Utils;
import com.nineone.verificationcode.utils.VideoSlimEncoder;
import com.nineone.verificationcode.view.DragImageView;
import com.nineone.verificationcode.view.ParentViewGroup;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;

import io.microshow.rxffmpeg.RxFFmpegInvoke;
import io.microshow.rxffmpeg.RxFFmpegSubscriber;

import static android.media.MediaCodec.BUFFER_FLAG_CODEC_CONFIG;
import static android.media.MediaCodec.CONFIGURE_FLAG_ENCODE;
import static android.media.MediaCodec.INFO_OUTPUT_FORMAT_CHANGED;
import static android.media.MediaCodecList.REGULAR_CODECS;
import static androidx.core.os.CancellationSignal.*;
import static com.nineone.verificationcode.service.VideoCompressionService.VIDEO_PATH;

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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String id = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        initImageLoader(this);
        ActivityCompat.requestPermissions(this, new String[]{permission}, 200);
        seekBar = findViewById(R.id.seek_bar);
        mine_iv = findViewById(R.id.mine_iv);
        gif_iv = findViewById(R.id.gif_iv);
        edit_view = findViewById(R.id.edit_view);
        final ImageView circle = findViewById(R.id.circle);
        seekBar.setMax(1000);
        mine_iv.getBgImageView().setImageResource(R.mipmap.demo2);
        final SurfaceView view_surface = findViewById(R.id.view_surface);
        mine_iv.getProgressImageView().setImageResource(R.mipmap.demo2);
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
                Log.e("setOnClickListener", "==============");
            }
        });
        parentViewGroup = findViewById(R.id.parent);

        setAdapter();
        Utils.addActivity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
        }

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

//        String text = "ffmpeg -y -i /storage/emulated/0/DCIM/Camera/VID_20200730_180248.mp4 -vf boxblur=25:5  -preset superfast /storage/emulated/0/4567.mp4";
//        -vf transpose = 0

//        selectCodec();




//        try {
//            mMuxer = new MediaMuxer(Environment.getExternalStorageDirectory() + "/why.mp4", MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    MediaRecorder mediaRecorder = new MediaRecorder();
//                    final MediaExtractor extractor = new MediaExtractor();
//                    extractor.setDataSource(Environment.getExternalStorageDirectory() + "/DCIM/Camera/VID_20200730_180248.mp4");
//                    extractor.seekTo(0, MediaExtractor.SEEK_TO_PREVIOUS_SYNC);
//                    int videoTrackIndex = -1, j = 0, k = 0;
//                    MediaFormat videoFormat = null;
//                    for (int i = 0; i < extractor.getTrackCount(); i++) {
//                        Log.e("extractor", "===" + extractor.getTrackFormat(i).toString());
//                        if (extractor.getTrackFormat(i).getString(MediaFormat.KEY_MIME).contains("video/")) {
//                            videoFormat = extractor.getTrackFormat(i);
//                            videoTrackIndex = mMuxer.addTrack(videoFormat);
//                            j = i;
//                        } else {
//                            k = mMuxer.addTrack(extractor.getTrackFormat(i));
//                        }
//                    }
//                    extractor.selectTrack(j);
//                    final int finalJ = j;
//                    final MediaCodec.BufferInfo mBufferInfo = new MediaCodec.BufferInfo();
//
//                    MediaFormat decoderFormat = getMediaFormatFrom();
//                    String type = decoderFormat.getString(MediaFormat.KEY_MIME);
////                    String type = "video/avc";
//                    final MediaCodec encoder = MediaCodec.createEncoderByType(type);
//                    encoder.setCallback(new MediaCodec.Callback() {
//                        @Override
//                        public void onInputBufferAvailable(@NonNull MediaCodec codec, int index) {
//                            ByteBuffer byteBuffer = encoder.getInputBuffer(index);
//                            Log.e("InputBuffer1111111", "====" + byteBuffer);
//                        }
//
//
//                        @Override
//                        public void onOutputBufferAvailable(@NonNull MediaCodec codec, int index, @NonNull MediaCodec.BufferInfo info) {
//                            ByteBuffer outputBuffer = codec.getOutputBuffer(index);
//                            Log.e("编码输出数据", "   outputBuffer ===" + outputBuffer + "    flags===" + info.flags
//                                    + "  size=== " + info.size
//                                    + "  index=== " + index
//                            );
//
//                            if ((info.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) == 0 && info.size > 1)
//                                if (mMuxer != null)
//                                    mMuxer.writeSampleData(finalJ, outputBuffer, info);
//                            if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
//                                extractor.unselectTrack(finalJ);
//                                Log.e("finalVideoTrackIndex", "===" + finalJ);
//                                writeAutoTrack(mMuxer, mBufferInfo, extractor);
//                                mMuxer.stop();
//                                mMuxer.release();
//                                mMuxer = null;
//                            }
//                            codec.releaseOutputBuffer(index, false);
//                        }
//
//                        @Override
//                        public void onError(@NonNull MediaCodec codec, @NonNull MediaCodec.CodecException e) {
//                            Log.e("onError", "    ===" + e);
//                        }
//
//                        @Override
//                        public void onOutputFormatChanged(@NonNull MediaCodec codec, @NonNull MediaFormat format) {
//                            Log.e("onOutputFormatChanged22", "    ===" + format);
//                        }
//                    });
//
//                    MediaFormat encoderFormat = MediaFormat.createVideoFormat(decoderFormat.getString(MediaFormat.KEY_MIME), 1080, 1920);     // 创建MediaFormat
//                    encoderFormat.setInteger(MediaFormat.KEY_BIT_RATE, 9600);// 指定比特率
//                    encoderFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 60);  // 指定帧率
////                    encoderFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatL32);  // 指定编码器颜色格式
//                    encoderFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);  // 指定编码器颜色格式
//                    encoderFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 5); // 指定关键帧时间间隔
////                    encoderFormat.setInteger(MediaFormat.KEY_COMPLEXITY, MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_CBR);
////                    encoderFormat.setInteger(MediaFormat.KEY_BITRATE_MODE, MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_CBR);\
//                    Log.e(" view_surface.getHolder().getSurface()", "==="+ view_surface.getHolder().getSurface());
//                    view_surface.getHolder().addCallback(new SurfaceHolder.Callback() {
//                        @Override
//                        public void surfaceCreated(SurfaceHolder holder) {
//                            Log.e(" surfaceCreated", "==="+ holder.getSurface());
//
//                        }
//
//                        @Override
//                        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//                            Log.e(" surfaceChanged", "==="+ holder.getSurface());
//                        }
//
//                        @Override
//                        public void surfaceDestroyed(SurfaceHolder holder) {
//                            Log.e(" surfaceDestroyed", "==="+ holder.getSurface());
//                        }
//                    });
//                    encoder.configure(encoderFormat, view_surface.getHolder().getSurface(), null, CONFIGURE_FLAG_ENCODE);
////                    Surface surface = new Surface(new SurfaceTexture(123456));
//                    Surface surface = view_surface.getHolder().getSurface();
////                    Rect rect = new Rect();
////                    rect.bottom = getResources().getDisplayMetrics().heightPixels;
////                    rect.right = getResources().getDisplayMetrics().widthPixels;
////                    rect.left = 0;
////                    rect.top = 0;
////                    surface.lockCanvas(rect);
//                    encoder.start();
//                    mMuxer.start();
//
//                    final MediaCodec decoder = MediaCodec.createDecoderByType(decoderFormat.getString(MediaFormat.KEY_MIME));
//
//                    decoder.setCallback(new MediaCodec.Callback() {
//
//                        @Override
//                        public void onInputBufferAvailable(@NonNull MediaCodec codec, int index) {
//                            ByteBuffer byteBuffer = decoder.getInputBuffer(index);
//                            try {
//                                int sampleSize = extractor.readSampleData(byteBuffer, 0);
//                                if (sampleSize < 0) {
//                                    decoder.queueInputBuffer(index, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
//                                } else {
//                                    decoder.queueInputBuffer(index, 0, sampleSize, extractor.getSampleTime(), 0);
//                                    extractor.advance();
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        public void onOutputBufferAvailable(@NonNull MediaCodec codec, int index, @NonNull MediaCodec.BufferInfo info) {
//                            ByteBuffer outputBuffer = codec.getOutputBuffer(index);
//                            Log.e("outputBuffer", "===" + outputBuffer + "      ===" + info.flags
//                                    + "    &==" + outputBuffer.limit()
//                                    + "   size=" + info.size);
////                            if (mMuxer != null)
////                                mMuxer.writeSampleData(finalVideoTrackIndex, outputBuffer, info);
//                            if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
//                                encoder.signalEndOfInputStream();
////                                extractor.unselectTrack(finalJ);
////                                Log.e("finalVideoTrackIndex", "===" + finalVideoTrackIndex+"     "+finalJ);
////                                writeAutoTrack(mMuxer, mBufferInfo, extractor);
////                                mMuxer.stop();
////                                mMuxer.release();
////                                mMuxer = null;
//                            }
//                            codec.releaseOutputBuffer(index, info.size != 0);
//
//                        }
//
//                        @Override
//                        public void onError(@NonNull MediaCodec codec, @NonNull MediaCodec.CodecException e) {
//                            Log.e("onError", "-----------");
//                        }
//
//                        @Override
//                        public void onOutputFormatChanged(@NonNull MediaCodec codec, @NonNull MediaFormat format) {
//                            Log.e("onOutputFormatChanged1", "-----------" + format);
//                        }
//                    });
//                    decoder.configure(encoderFormat, surface, null, 0);
////                    decoder.configure(decoderFormat, surface, null, 0);
////                    decoder.configure(encoderFormat, null, null, 0);
//                    decoder.start();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
        Intent intent = new Intent(this, VideoCompressionService.class);
        intent.putExtra(VIDEO_PATH, "/storage/emulated/0/DCIM/Camera/VID_20200730_180248.mp4");
//        intent.putExtra(VIDEO_PATH, "/storage/emulated/0/soul/1597047206579.mp4");
        startService(intent);


        new Thread() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                super.run();
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{
                        MediaStore.Video.Media.DATA,
                        MediaStore.Video.Media.WIDTH,
                        MediaStore.Video.Media.HEIGHT,
                        MediaStore.Video.Media.TITLE,
                        MediaStore.Video.Media.MIME_TYPE,
                        MediaStore.Video.Media.SIZE
                }, null, null, null);
                while (cursor != null && cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                    Log.e("path==", path
                                    +"    HEIGHT==="+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.HEIGHT))
                                    +"    WIDTH==="+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.WIDTH))
                                    +"    SIZE==="+cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))
                            );
                }

                if (cursor != null && !cursor.isClosed()) cursor.close();
            }
        }.start();

    }

    private void writeAutoTrack(MediaMuxer mMuxer, MediaCodec.BufferInfo info, MediaExtractor extractor) {

        for (int i = 0; i < extractor.getTrackCount(); i++) {
            if (extractor.getTrackFormat(i).getString(MediaFormat.KEY_MIME).contains("audio/")) {
                extractor.selectTrack(i);
                MediaFormat trackFormat = extractor.getTrackFormat(i);
                Log.e("trackIndex", "===" + i);

                int maxBufferSize = trackFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE);
                boolean inputDone = false;
                extractor.seekTo(0, MediaExtractor.SEEK_TO_PREVIOUS_SYNC);
                ByteBuffer buffer = ByteBuffer.allocateDirect(maxBufferSize);
                long startTime = -1, end = -1;

                while (!inputDone) {

                    boolean eof = false;
                    int index = extractor.getSampleTrackIndex();
                    if (index == i) {
                        info.size = extractor.readSampleData(buffer, 0);

                        if (info.size < 0) {
                            info.size = 0;
                            eof = true;
                        } else {
                            info.presentationTimeUs = extractor.getSampleTime();

                            if (end < 0 || info.presentationTimeUs < end) {
                                info.offset = 0;
                                info.flags = extractor.getSampleFlags();
                                mMuxer.writeSampleData(i, buffer, info);
                                extractor.advance();
                            } else {
                                eof = true;
                            }
                        }
                    } else if (index == -1) {
                        eof = true;
                    }
                    if (eof) {
                        inputDone = true;
                    }
                }

                extractor.unselectTrack(i);


            }

        }

    }

    public native String stringFromJNI();

    private MediaFormat getMediaFormatFrom() {
        MediaExtractor extractor = new MediaExtractor();
        MediaFormat mf = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/Camera/VID_20200730_180248.mp4");
            extractor.setDataSource(file.getAbsolutePath());
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

}
