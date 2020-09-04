package com.nineone.verificationcode;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nineone.verificationcode.activity.ThreeActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SecondActivity extends AppCompatActivity {
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
    }

    private void init() {
        parent_rl = findViewById(R.id.parent_rl);
        bottom_rl = findViewById(R.id.bottom_rl);
        child_rl = findViewById(R.id.child_rl);
        six_iv = findViewById(R.id.six_iv);
        button = findViewById(R.id.button_jump);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(SecondActivity.this, ThreeActivity.class);
            startActivity(intent);
        });

        video_button = findViewById(R.id.video_button);

        video_button.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            //好使
            intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 10485760L);
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
            startActivityForResult(intent, VIDEO_CAPTURE);
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
}