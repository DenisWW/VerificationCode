package com.nineone.verificationcode;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewParentCompat;
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
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.annotation.ActAnnotation;
import com.nineone.verificationcode.activity.BesselActivity;
import com.nineone.verificationcode.utils.Utils;
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

@ActAnnotation(name = "TestMainActivity")
public class MainActivity extends Activity {
    private SeekBar seekBar;
    private DragImageView mine_iv;
    private APngImageView gif_iv;
    private String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private ParentViewGroup parentViewGroup;
    private EditText edit_view;

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
        Utils.addActivity();
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

}
