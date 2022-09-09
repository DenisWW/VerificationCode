package com.nineone.verificationcode.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.nineone.verificationcode.bean.UserBean;
import com.nineone.verificationcode.service.AIDLService;
import com.nineone.verificationcode.BookController;
import com.nineone.verificationcode.R;
import com.nineone.verificationcode.adapter.ViewPagerAdapter2;
import com.nineone.verificationcode.fragment.TestFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FiveActivity extends FragmentActivity {
    private List<UserBean> strings = new ArrayList<>();
    private List<UserBean> stringss = new ArrayList<>();
    private RelativeLayout top_layout;
    private RelativeLayout top_rl;
    private ViewPager2 viewpager2;
    private ImageView iv;
    private AnimationDrawable animationDrawable;

    private TextView tv;
    /**
     * [ a, b, c, d, e,
     * f, g, h, i, j,
     * k, l, m, n, o,
     * p, q, r, s, t ]
     * R’ = a*R + b*G + c*B + d*A + e;
     * G’ = f*R + g*G + h*B + i*A + j;
     * B’ = k*R + l*G + m*B + n*A + o;
     * A’ = p*R + q*G + r*B + s*A + t;
     */
    float[] colorMatrix = {1, 0.5F, 0.5F, 0, 0, //red
            0, 1, 0, 0, 0, //green
            0, 0, 1, 0, 0, //blue
            0, 0, 0, 1, 0 //alpha
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five);
        tv = findViewById(R.id.tv_1);

//        ArgbEvaluatorCompat.getInstance().evaluate();
        ImageView imageView = findViewById(R.id.iv);
//        Color.colorSpace()
        Log.e("color===", "====" + Color.parseColor("#000000")
                + "    ==" + Color.parseColor("#888888")
                + "    color==" + getResources().getResourceName(R.color.color_4F22FF)
                + "    drawable==" + getResources().getResourceName(R.drawable.one)
                + "    ==" + getPackageResourcePath()
                + "    ==" + getPackageCodePath()
                + "    ==" + imageView.getDrawable()
                + "    ==" + Color.parseColor("#FFFFFF")
        );
        imageView.setImageResource(R.drawable.bottom_bg);
        XmlResourceParser xmlResourceParser;

        imageView.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
//        getResources().getIdentifier()
        new Thread() {
            @Override
            public void run() {
                super.run();
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.one);
                ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
                ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos1);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 95, baos2);

//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data1 = baos1.toByteArray();
                byte[] data2 = baos2.toByteArray();
                Bitmap bitmap2 = BitmapFactory.decodeByteArray(data2, 0, data2.length);
                Log.e("bitmap==", "===" + bitmap.getByteCount() + "  bitmap1=  " + bitmap2.getByteCount() + "  data1=  " + data1.length + "  data2=  " + data2.length + "  rate=  " + (data2.length * 1F / data1.length) + "  rate=  " + "   ===" + isSystemRoot());
                tv.post(() -> tv.setText(isSystemRoot() + ""));

            }
        }.start();

        Glide.with(this).load(R.drawable.one).into(imageView);
        Log.e("FiveActivity", "===" + FiveActivity.class.getName());
//        SurfaceView view_surface = findViewById(R.id.view_surface);
//        MediaPlayer mediaPlayer = new MediaPlayer();
//
//        try {
//            mediaPlayer.setDataSource(this, Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        view_surface.setZOrderMediaOverlay(false);
//        view_surface.setZOrderOnTop(true);
//        view_surface.getHolder().addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder holder) {
//                Log.e(" surfaceCreated", "===" + holder.getSurface());
////                MediaPlayer mediaPlayer = MediaPlayer.create(FiveActivity.this,   Uri.parse("https://wsmedia.iyingdi.cn/video/2020/08/10/504fca6f-c079-4027-aacf-d5defeae859a.mp4"));//创建MediaPlayer对象
//                mediaPlayer.setDisplay(holder);
//                mediaPlayer.prepareAsync();//预准备
//                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                    @Override
//                    public void onPrepared(MediaPlayer mp) {
//                        mp.start();
//                        Log.e("onPrepared", "===" + mp);
//                    }
//                });
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
//        VideoView videoView = findViewById(R.id.video_view);
//        videoView.setVideoPath("https://wsmedia.iyingdi.cn/video/2020/08/10/504fca6f-c079-4027-aacf-d5defeae859a.mp4");
//
//        videoView.setZOrderOnTop(false);
//        videoView.setZOrderMediaOverlay(false);
//        videoView.start();
//
//
//        VideoView video_view1 = findViewById(R.id.video_view1);
//        video_view1.setVideoPath("https://wsmedia.iyingdi.cn/video/2020/08/10/504fca6f-c079-4027-aacf-d5defeae859a.mp4");
//        video_view1.setZOrderOnTop(true);
//        video_view1.setZOrderMediaOverlay(false);
//        video_view1.start();

        strings.add(new UserBean("123", 1));
        strings.add(new UserBean("12355", 2));
        strings.add(new UserBean("12366", 3));
        strings.add(new UserBean("123777", 4));
        stringss.addAll(strings);
        stringss.add(new UserBean("1238888", 4));
        top_layout = findViewById(R.id.top_layout);
        iv = findViewById(R.id.iv);
        top_rl = findViewById(R.id.top_rl);
        for (int i = 0; i < 9; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_bbs_layout, null);
            top_layout.addView(view);
        }
        topRLParams = (RelativeLayout.LayoutParams) top_rl.getLayoutParams();


        viewpager2 = findViewById(R.id.viewpager2);
        ViewPagerAdapter2 adapter2 = new ViewPagerAdapter2(this);
        viewpager2.setAdapter(adapter2);
        List<Fragment> fragments = new ArrayList<>();
        Bundle bundle;
        for (int i = 0; i < 5; i++) {
            bundle = new Bundle();
            bundle.putInt("position", i);
            TestFragment fragment = new TestFragment();
            fragment.setArguments(bundle);
            fragments.add(fragment);

        }
        adapter2.setFragments(fragments);
//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> runningTasks = manager.getRunningTasks(1);
//        ActivityManager.RunningTaskInfo cinfo = runningTasks.get(0);

//        Book1 book1= AIDLService.book1;
        Log.e("=========" + this.getClass().getName(), "     onCreate==" + iv.getBackground() + "===" + AIDLService.AUDIO_SERVICE);
        animationDrawable = (AnimationDrawable) iv.getBackground();
        Intent intent = new Intent(this, AIDLService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        findViewById(R.id.button_view).setOnClickListener(v -> {
            Intent intent1 = new Intent(this, ThreeActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
        });
//        AssetManager assetManager = getAssets();
//        for (String s : assetManager.getLocales()) {
//            Log.e("Locales", "===" + s);
//        }
    }

    private BookController controller;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            controller = BookController.Stub.asInterface(service);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(getClass().getName(), "     onDestroy==");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(getClass().getName(), "     onStart==");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(getClass().getName(), "    onResume==");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.e(getClass().getName(), "    onPause==");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(getClass().getName(), "    onStop==");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(getClass().getName(), "    onRestart==");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(getClass().getName(), "    onNewIntent==");
    }

    private RelativeLayout.LayoutParams topRLParams;

    public void clickB(View view) {
        Intent intent = new Intent(this, FiveActivity.class);
        startActivity(intent);
//        try {
//            JSONObject jsonObject = new JSONObject(json);
//            String id = jsonObject.optString("id");
//            String name = jsonObject.optString("name");
//            String gender = jsonObject.optString("gender");
//            String eMail = jsonObject.optString("email");
//            //获取用户头像
//            JSONObject object_pic = jsonObject.optJSONObject("picture");
//            JSONObject object_data = null;
//            String photo = "";
//            if (object_pic != null) {
//                object_data = object_pic.optJSONObject("data");
//                if (object_data != null) photo = object_data.optString("url");
//
//            }
//            String locale = jsonObject.optString("locale");
//            Log.e("id===", id+"     name=="+name+"   gender== "+gender+"   eMail== "+eMail+"    photo=="+photo);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


//        ValueAnimator animator = ObjectAnimator.ofInt(200, 800);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                topRLParams.height = (int) animation.getAnimatedValue();
//                top_rl.setLayoutParams(topRLParams);
//                Log.e("onAnimationUpdate", "===" + animation.getAnimatedValue());
//            }
//        });
//        animator.setDuration(10000);
//        animator.start();
    }

    private static final String[] a = new String[]{"/su", "/su/bin/su", "/sbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/data/local/su", "/system/xbin/su", "/system/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/system/bin/cufsdosck", "/system/xbin/cufsdosck", "/system/bin/cufsmgr", "/system/xbin/cufsmgr", "/system/bin/cufaevdd", "/system/xbin/cufaevdd", "/system/bin/conbb", "/system/xbin/conbb"};

    public boolean isSystemRoot() {
        boolean var0 = false;
        String[] var1;
        int var2 = (var1 = a).length;

        for (int var3 = 0; var3 < var2; ++var3) {
            String var4 = var1[var3];
            if ((new File(var4)).exists()) {
                var0 = true;
                break;
            }
        }

        return Build.TAGS != null && Build.TAGS.contains("test-keys") || var0;
    }

}