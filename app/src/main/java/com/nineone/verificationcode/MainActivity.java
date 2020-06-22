package com.nineone.verificationcode;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.nineone.verificationcode.activity.BesselActivity;
import com.nineone.verificationcode.view.DragImageView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class MainActivity extends Activity {
    private SeekBar seekBar;
    private DragImageView mine_iv;
    private APngImageView gif_iv;
    private String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String id = Settings.System.getString(getContentResolver(),  Settings.Secure.ANDROID_ID);
        Log.e("ANDROID_ID", "====="+id);
        initImageLoader(this);
        ActivityCompat.requestPermissions(this, new String[]{permission}, 200);
        seekBar = findViewById(R.id.seek_bar);
        mine_iv = findViewById(R.id.mine_iv);
        gif_iv = findViewById(R.id.gif_iv);
        seekBar.setMax(1000);
        mine_iv.getBgImageView().setImageResource(R.mipmap.demo2);
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
//        try {
//            new Thread() {
//                @Override
//                public void run() {
//                    super.run();
//                    try {
//                        InputStream fileInputStream = getResources().openRawResource(R.mipmap.fire_bitmap);
//                        InputStream testInputStream = getResources().openRawResourceFd(R.mipmap.login_top_bg).createInputStream();
//                        InputStream testInputStream1 = getResources().openRawResourceFd(R.mipmap.demo2).createInputStream();
//                        readStream(fileInputStream, fileInputStream.available());
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }.start();
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }

    private void readStream(InputStream fileInputStream, int available) {
        try {
            int capacity = (available > 0) ? (available + 4 * 1024) : 16 * 1024;
            ByteArrayOutputStream buffer = new ByteArrayOutputStream(capacity);
            int nRead;
            byte[] data = new byte[16 * 1024];
            while ((nRead = fileInputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
//            APngHeader aPngHeaderParser = new APngHeaderParser().setData(ByteBuffer.wrap(buffer.toByteArray())).parseHeader();
//            ByteBuffer rawData = ByteBuffer.wrap(buffer.toByteArray()).asReadOnlyBuffer();
//            rawData.position(0);
//            for (int i = 0; i < 100; i++) {
//                byte b = rawData.get();
//                Log.e("readStream", i + "===" + b);
//            }
//            int n= (byte)0x81;
//            int m= 0x81;
//            Log.e("(byte)", "  n==" +new String(new byte[]{82}) );
//            for (int i = 0; i < APngConstant.LENGTH_SIGNATURE; i++) {
//                byte b = rawData.get();
//                Log.e("readStream", b + "===" + new String(new byte[]{b}, "utf-8")
//                        + "    ==" + APngConstant.BYTES_SIGNATURE[i]
//                );
//            }

//            CharBuffer rawData = fileInputStream.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, fileInputStream.available()).asCharBuffer();

//            rawData.position(0);
//            rawData.order(ByteOrder.BIG_ENDIAN);
//
//            rawData.position(0);
//            int length, code;
//            for (int i = 0; i < 1000; i++) {
//                length = rawData.getInt();
//            Log.e("rawData", i + "    code ===" + "  position==" + rawData.position() + "    ==" + rawData.get());
//                if (code == APngConstant.IHDR_VALUE) {
//                    Log.e("readIHDR", i + "===" + readIHDR(rawData).toString());
//                } else if (code == APngConstant.acTL_VALUE) {
//                    readAcTL(rawData);
//                }
//                int newPosition = Math.min(rawData.position() + 4, rawData.limit());
//                rawData.position(newPosition);

//                skip(rawData, 4);
//                rawData.position(i * 4);
//                        + "\nIHDR_VALUE=" + APngConstant.IHDR_VALUE
//                        + "\nacTL_VALUE=" + APngConstant.acTL_VALUE
//                        + "\nPLTE_VALUE=" + APngConstant.PLTE_VALUE
//                        + "\nfcTL_VALUE=" + APngConstant.fcTL_VALUE
//                        + "\nIDAT_VALUE=" + APngConstant.IDAT_VALUE
//                        + "\nfdAT_VALUE=" + APngConstant.fdAT_VALUE
//                        + "\nIEND_VALUE=" + APngConstant.IEND_VALUE
//                        + "\ntIME_VALUE=" + APngConstant.tIME_VALUE
//                        + "\niTXt_VALUE=" + APngConstant.iTXt_VALUE
//                        + "\ntEXt_VALUE=" + APngConstant.tEXt_VALUE
//                        + "\nzTXt_VALUE=" + APngConstant.zTXt_VALUE
//                        + "\ngAMA_VALUE=" + APngConstant.gAMA_VALUE
//                        + "\nbKGD_VALUE=" + APngConstant.bKGD_VALUE
//                        + "\ntRNS_VALUE=" + APngConstant.tRNS_VALUE
//                );
//            }


        } catch (IOException ignored) {
        }
    }

    /**
     * Animation Control Chunk<br/>
     * num_frames：0~3字节表示该Apng总的播放帧数。<br/>
     * num_plays：4~7字节表示该Apng循环播放的次数。<br/>
     */


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

        gif_iv.start();
    }
}
