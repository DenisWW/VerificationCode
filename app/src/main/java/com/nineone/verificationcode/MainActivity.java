package com.nineone.verificationcode;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nineone.verificationcode.activity.BesselActivity;
import com.nineone.verificationcode.view.DragImageView;
import com.nineone.verificationcode.view.ParentViewGroup;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private SeekBar seekBar;
    private DragImageView mine_iv;
    private APngImageView gif_iv;
    private String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private ParentViewGroup parentViewGroup;

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

        findViewById(R.id.testView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("setOnClickListener", "==============");
            }
        });
        parentViewGroup = findViewById(R.id.parent);

        setAdapter();

//        WorkManager workManager = WorkManager.getInstance(this);
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
//        if (parentViewGroup.isOpenRightView()) {
//            parentViewGroup.closeRightLayout();
//        } else {
//            parentViewGroup.openRightLayout();
//        }
//        parentViewGroup.setList();

//        gif_iv.start();
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

}
