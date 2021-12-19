package com.nineone.verificationcode.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.nineone.verificationcode.R;
import com.nineone.verificationcode.bean.GetTest;
import com.nineone.verificationcode.utils.Utils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class SecondActivity extends Activity {
    private Button button;
    private Button video_button;
    private TextView expand_bt;
    private ImageView six_iv;
    private RelativeLayout parent_rl;
    private RelativeLayout bottom_rl;
    private RelativeLayout child_rl;
    private int VIDEO_CAPTURE = 100;
    private Context context;
    private List<View> views = new ArrayList<>();
    private View[] imageViews;
    private boolean isExpand = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        context = this;
        init();
        Glide.with(this).load("").into(six_iv);
        String url = "http://wwww.baidu.com";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(new Converter.Factory() {
            @Override
            public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {

                return new Converter<ResponseBody, String>() {
                    @org.jetbrains.annotations.Nullable
                    @Override
                    public String convert(ResponseBody value) throws IOException {
                        String responseBody = value.string();
                        Log.e("responseBody", "===" + responseBody);
                        return responseBody;
                    }
                };
            }
        }).build();

        GetTest test = retrofit.create(GetTest.class);
        new Thread() {
            @Override
            public void run() {
                super.run();
                retrofit2.Call<String> call = test.getBaidu();
                try {
                    call.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();

//        retrofit.create(GetTest.class);
//        Executors.newFixedThreadPool()
//        Looper.prepare();
//        Looper.loop();

//       new Handler().sendEmptyMessageAtTime();
//        Lock lock=new ReentrantLock();
//        lock.lockInterruptibly();
        GetTest getTest = Utils.create(GetTest.class);
        getTest.getTest("12131");
//        Thread thread=Thread.currentThread();
//
//        io.reactivex.rxjava3.core.Observable observable = null;
//        observable.subscribe(new Consumer() {
//            @Override
//            public void accept(Object o) throws Throwable {
//
//            }
//        });

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                Log.e("response", "===" + response.code());
                return response;
            }
        });
        builder.retryOnConnectionFailure(true);
        OkHttpClient myClient = builder.build();

        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        myClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e("thread1", "=====" + System.currentTimeMillis());
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
        Thread thread2 = new Thread() {
            @Override
            public void run() {
                super.run();
                Log.e("thread2", "=====" + System.currentTimeMillis());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread3 = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    thread1.start();
                    thread2.start();
                    thread1.wait();
                    thread2.join();
                } catch (InterruptedException e) {
                    thread1.interrupt();
                    thread2.interrupt();
                    e.printStackTrace();
                }
                Log.e("thread3", "--====" + System.currentTimeMillis());


            }
        };
        thread3.start();

//        Log.e(getBaseContext().toString(), "    =====" +o);
//        getInteger(2);
    }

    private void init() {
        parent_rl = findViewById(R.id.parent_rl);
        bottom_rl = findViewById(R.id.bottom_rl);
        child_rl = findViewById(R.id.child_rl);
        six_iv = findViewById(R.id.six_iv);
        six_iv.invalidate();
        button = findViewById(R.id.button_jump);
        button.setOnClickListener(v -> {
//            Intent intent = new Intent(SecondActivity.this, ThreeActivity.class);
            Intent intent = new Intent(SecondActivity.this, FourActivity.class);
            startActivity(intent);
        });

        video_button = findViewById(R.id.video_button);

        video_button.setOnClickListener(v -> {
            Log.e("setOnClickListener", "position");
//            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//            //好使
//            intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 10485760L);
//            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
//            startActivityForResult(intent, VIDEO_CAPTURE);
        });
        video_button.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.e("setOnTouchListener", "ACTION_DOWN");
                return false;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.e("setOnTouchListener", "ACTION_UP");
                return true;
            }
            return false;
        });

//        views.add(findViewById(R.id.six_iv));
        views.add(findViewById(R.id.two_iv));
        views.add(findViewById(R.id.three_iv));
        views.add(findViewById(R.id.four_iv));
//        views.add(findViewById(R.id.bottom_rl));
        views.add(findViewById(R.id.five_iv));
        imageViews = new View[views.size()];

        expand_bt = findViewById(R.id.expand_bt);
        expand_bt.setOnClickListener(v -> {
            if (isExpand) {
                isExpand = false;
                AnimatorSet animatorSet = new AnimatorSet();
                ValueAnimator objectAnimator = ObjectAnimator.ofInt((int) (context.getResources().getDisplayMetrics().density * 640),
                        (int) (context.getResources().getDisplayMetrics().density * 240));
                objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    RelativeLayout.LayoutParams rl;
                    RelativeLayout.LayoutParams parentRl;
                    LinearLayout.LayoutParams childRl;

                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (rl == null) {
                            rl = (RelativeLayout.LayoutParams) parent_rl.getLayoutParams();
                        }

                        if (childRl == null) {
                            childRl = (LinearLayout.LayoutParams) child_rl.getLayoutParams();
                        }
                        rl.height = Integer.parseInt(animation.getAnimatedValue().toString());
                        childRl.height = Integer.parseInt(animation.getAnimatedValue().toString());

                        parent_rl.setLayoutParams(rl);
                        child_rl.setLayoutParams(childRl);
                        bottom_rl.setTranslationY(Integer.parseInt(animation.getAnimatedValue().toString()) - (context.getResources().getDisplayMetrics().density * 240));

                    }
                });
                objectAnimator.setDuration(500);
                animatorSet.play(objectAnimator);
                setAnimatorSetOut(animatorSet, views.toArray(imageViews));
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });
                animatorSet.start();
            } else {
                isExpand = true;
                AnimatorSet animatorSet = new AnimatorSet();
                ValueAnimator objectAnimator = ObjectAnimator.ofInt((int) (context.getResources().getDisplayMetrics().density * 240),
                        (int) (context.getResources().getDisplayMetrics().density * 640));
                objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    RelativeLayout.LayoutParams rl;
                    RelativeLayout.LayoutParams parentRl;
                    LinearLayout.LayoutParams childRl;

                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (rl == null) {
                            rl = (RelativeLayout.LayoutParams) parent_rl.getLayoutParams();
                        }

                        if (childRl == null) {
                            childRl = (LinearLayout.LayoutParams) child_rl.getLayoutParams();
                        }
                        rl.height = Integer.parseInt(animation.getAnimatedValue().toString());
                        childRl.height = Integer.parseInt(animation.getAnimatedValue().toString());
                        parent_rl.setLayoutParams(rl);
                        child_rl.setLayoutParams(childRl);
                        bottom_rl.setTranslationY(Integer.parseInt(animation.getAnimatedValue().toString()) - (context.getResources().getDisplayMetrics().density * 240));
                    }
                });
                objectAnimator.setDuration(800);
                objectAnimator.setInterpolator(new OvershootInterpolator());
                animatorSet.play(objectAnimator);
                setAnimatorSet(animatorSet, views.toArray(imageViews));
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        six_iv.setVisibility(View.INVISIBLE);
                        six_iv.post(new Runnable() {
                            @Override
                            public void run() {
                                NestedScrollView scrollView = new NestedScrollView(context);
                                scrollView.addOnLayoutChangeListener((v1, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {

                                });
                            }
                        });
//                        scrollView.fullScroll(View.FOCUS_DOWN);
//                        scrollView.scrollTo(0, (int) (context.getResources().getDisplayMetrics().density * 640));


                    }
                });
                animatorSet.start();
            }
        });
    }

    private void setAnimatorSet(AnimatorSet animatorSet, View... views) {
        ObjectAnimator animator;
        for (int i = 0; i < views.length; i++) {
            float f = context.getResources().getDisplayMetrics().density * 100 * (views.length - i - 1);
            Log.e("fffff", "==" + f);
            if (i == views.length - 1) {
                animator = ObjectAnimator.ofFloat(views[i], "alpha", 0, 1f);
                animator.setDuration(1000);
            } else {
                animator = ObjectAnimator.ofFloat(views[i], "translationY", 0, f);
                animator.setDuration(800);
                animator.setInterpolator(new OvershootInterpolator());
            }
            int finalI = i;
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) views[finalI].getLayoutParams();
                    rl.leftMargin = (int) (context.getResources().getDisplayMetrics().density * 10);
                    rl.rightMargin = (int) (context.getResources().getDisplayMetrics().density * 10);
                    views[finalI].setLayoutParams(rl);
                }

            });
            animatorSet.play(animator).after(50 * i);
        }
    }

    private void setAnimatorSetOut(AnimatorSet animatorSet, View... views) {
        ObjectAnimator animator;
        for (int i = 0; i < views.length; i++) {
            float f = context.getResources().getDisplayMetrics().density * 100 * (views.length - i - 1);
            if (i == views.length - 1) {
                animator = ObjectAnimator.ofFloat(views[i], "alpha", 1, 0f);
                animator.setDuration(500);
            } else {
                animator = ObjectAnimator.ofFloat(views[i], "translationY", f, 0);
                animator.setDuration(500);
            }
            int finalI = i;
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);

                }

            });
            animatorSet.play(animator).after(50 * i);
            RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) views[finalI].getLayoutParams();
            rl.leftMargin = (int) (context.getResources().getDisplayMetrics().density * 20);
            rl.rightMargin = (int) (context.getResources().getDisplayMetrics().density * 20);
            rl.topMargin = (int) (context.getResources().getDisplayMetrics().density * 20);
            views[finalI].setLayoutParams(rl);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == VIDEO_CAPTURE) {
            Uri uri = data.getData();
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            for (String s : cursor.getColumnNames()) {
                Log.e("cursor", "====" + s);
            }
            cursor.moveToNext();
            Log.e("cursor", "====" + cursor.getString(cursor.getColumnIndex("_data")));
        }

    }


    public int getInteger(int i) {
        if (i == 5) {
            return i;
        }
        i++;
        Log.e("getInteger1", "===" + i);
        getInteger(i);
        Log.e("getInteger2", "===" + i);
        return i;

    }
}