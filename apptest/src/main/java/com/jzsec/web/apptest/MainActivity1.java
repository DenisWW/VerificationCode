package com.jzsec.web.apptest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity1 extends AppCompatActivity {
    TextView textView;
    private ImageView carIv;
    private TextView numTv;
    private ImageView centerIv;
    private ImageView rightIv;
    private RelativeLayout parentRlay;
    private float carLeftX;
    private float rightIvY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        textView = findViewById(R.id.tv);
        carIv = findViewById(R.id.carIv);
        numTv = findViewById(R.id.numTv);
        centerIv = findViewById(R.id.centerIv);
        rightIv = findViewById(R.id.rightIv);
        parentRlay = findViewById(R.id.parentRlay);
        textView.setOnClickListener(v -> {

            ObjectAnimator objectAnimatorCar = ObjectAnimator.ofFloat(carIv, "TranslationX", -carLeftX, 0);
            objectAnimatorCar.setDuration(800);
            ObjectAnimator objectAnimatorCarEnd = ObjectAnimator.ofFloat(carIv, "TranslationX", 0, -5, 0);
            objectAnimatorCarEnd.setDuration(300);

            ObjectAnimator objectAnimatorRightIv = ObjectAnimator.ofFloat(rightIv, "TranslationY", 0, rightIvY);
            objectAnimatorRightIv.setDuration(800);
            ObjectAnimator objectAnimatorRightIvEnd = ObjectAnimator.ofFloat(rightIv, "TranslationY", rightIvY, rightIvY - 10, rightIvY);
            objectAnimatorRightIvEnd.setDuration(500);
//            ObjectAnimator objectAnimatorRightIvEndX = ObjectAnimator.ofFloat(rightIv, "TranslationX", 0f, 20f);
//            objectAnimatorRightIvEndX.setDuration(100);


            ObjectAnimator objectAnimatorNumTv = ObjectAnimator.ofFloat(numTv, "alpha", 0, 1);
            objectAnimatorNumTv.setDuration(500);

            ObjectAnimator objectAnimatorCenterIv = ObjectAnimator.ofFloat(centerIv, "alpha", 0, 1);
            objectAnimatorCenterIv.setDuration(500);
            ObjectAnimator objectAnimatorCenterXIv = ObjectAnimator.ofFloat(centerIv, "scaleX", 2, 1);
            objectAnimatorCenterXIv.setDuration(500);
            ObjectAnimator objectAnimatorCenterYIv = ObjectAnimator.ofFloat(centerIv, "scaleY", 2, 1);
            objectAnimatorCenterYIv.setDuration(500);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(objectAnimatorNumTv)
                    .with(objectAnimatorCenterXIv).with(objectAnimatorCenterYIv).with(objectAnimatorCenterIv)
                    .with(objectAnimatorCarEnd)
//                    .with(objectAnimatorRightIvEnd)
//                    .with(objectAnimatorRightIvEndX)

                    .after(objectAnimatorCar)
                    .after(objectAnimatorRightIv);
            rightIv.setTranslationX(0);
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    numTv.setAlpha(0f);
                    centerIv.setAlpha(0f);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
//                    ObjectAnimator objectAnimatorRightIvEnd = ObjectAnimator.ofFloat(rightIv, "TranslationY", rightIvY, rightIvY-10,rightIvY);
//                    objectAnimatorRightIvEnd.setDuration(100);
                    ObjectAnimator objectAnimatorRightIvEndX = ObjectAnimator.ofFloat(rightIv, "TranslationX", 0f, 20f);
                    objectAnimatorRightIvEndX.setDuration(100);
                    objectAnimatorRightIvEndX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            Log.e("onAnimationUpdate", "===" + animation.getAnimatedValue());
                        }
                    });
                    objectAnimatorRightIvEndX.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            Log.e("onAnimationEnd", "===" +rightIv.getTranslationX());
                        }
                    });
                    Log.e("ObjectAnimator", "===" + rightIv.getTranslationX());
                    objectAnimatorRightIvEndX.start();

//                    AnimatorSet animatorSet = new AnimatorSet();
//                    animatorSet.playTogether(objectAnimatorRightIvEnd,objectAnimatorRightIvEndX);
//                    animatorSet.playTogether(objectAnimatorRightIvEndX);
//                    animatorSet.start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animatorSet.start();
        });
        carIv.post(new Runnable() {
            @Override
            public void run() {
                carLeftX = carIv.getX() + carIv.getWidth();
                rightIvY = -(rightIv.getY() + rightIv.getHeight() / 2f - parentRlay.getHeight() / 2f);
            }
        });
        carIv.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                Log.e("onViewAttachedToWindow", "===="+v);

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                Log.e("onViewDetachedFrom", "===="+v);
            }
        });

    }
}
