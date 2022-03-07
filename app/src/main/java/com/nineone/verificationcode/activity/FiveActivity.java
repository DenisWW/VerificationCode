package com.nineone.verificationcode.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.nineone.verificationcode.bean.UserBean;
import com.nineone.verificationcode.service.AIDLService;
import com.nineone.verificationcode.BookController;
import com.nineone.verificationcode.R;
import com.nineone.verificationcode.adapter.ViewPagerAdapter2;
import com.nineone.verificationcode.fragment.TestFragment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five);


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

        Log.e("         =========" + this.getClass().getName(), "     onCreate==" + iv.getBackground());
        animationDrawable = (AnimationDrawable) iv.getBackground();
        Intent intent = new Intent(this, AIDLService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        findViewById(R.id.button_view).setOnClickListener(v -> {
            Intent intent1 = new Intent(this, ThreeActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
        });

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

}