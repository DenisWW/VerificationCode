package com.nineone.verificationcode.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.os.Bundle;
import android.widget.ImageView;

import com.nineone.verificationcode.R;

public class BesselActivity extends AppCompatActivity {
    private ImageView login_bg;
    private ImageView login_top_icon;
    private int screenW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bessel);
        initView();
        startAnimator();

    }

    private void startAnimator() {
        screenW = this.getResources().getDisplayMetrics().widthPixels;
        ;

        ObjectAnimator animator = ObjectAnimator.ofObject(this, "UI", new PathEvaluator(), new PathPoint(screenW - (this.getResources().getDisplayMetrics().density * 200),
                        -(this.getResources().getDisplayMetrics().density * 100)),
                new PathPoint(0, 0,
                        -screenW / 2f, 100,
                        -screenW / 2f, screenW));
        animator.setDuration(1500);
        animator.start();


        ObjectAnimator animatorBg1 = ObjectAnimator.ofFloat(login_bg, "alpha", 0.0f, 1.0f);
        ObjectAnimator animatorBg2 = ObjectAnimator.ofFloat(login_bg, "translationX", -screenW, 0);

    }

    private void initView() {
        login_top_icon = findViewById(R.id.login_top_icon);
        login_bg = findViewById(R.id.login_bg);


    }

    public void setUI(PathPoint point) {
        login_top_icon.setTranslationX(point.mX);
        login_top_icon.setTranslationY(point.mY);
        login_top_icon.setScaleX(point.fraction);
        login_top_icon.setScaleY(point.fraction);
        login_top_icon.setRotation(60 * (1.0f - point.fraction));
    }

    public static class PathEvaluator implements TypeEvaluator<PathPoint> {

        //三阶贝塞尔曲线的运动轨迹
        @Override
        public PathPoint evaluate(float fraction, PathPoint startValue, PathPoint endValue) {
            float x, y;
            float oneMiunsT = 1 - fraction;
            x = startValue.mX * oneMiunsT * oneMiunsT * oneMiunsT +
                    3 * endValue.mContorl0X * fraction * oneMiunsT * oneMiunsT
                    + 3 * endValue.mContorl1X * fraction * fraction * oneMiunsT
                    + endValue.mX * fraction * fraction * fraction;

            y = startValue.mY * oneMiunsT * oneMiunsT * oneMiunsT +
                    3 * endValue.mContorl0Y * fraction * oneMiunsT * oneMiunsT
                    + 3 * endValue.mContorl1Y * fraction * fraction * oneMiunsT
                    + endValue.mY * fraction * fraction * fraction;

            return PathPoint.newInstance(x, y, fraction);
        }
    }

    public static class PathPoint {
        float mX;
        float mY;
        float fraction;


        float mContorl0X;
        float mContorl0Y;

        float mContorl1X;
        float mContorl1Y;


        public PathPoint(float x, float y) {
            mX = x;
            mY = y;

        }

        public PathPoint(float mX, float mY, float mContorl0X, float mContorl0Y, float mContorl1X, float mContorl1Y) {
            this.mX = mX;
            this.mY = mY;
            this.mContorl0X = mContorl0X;
            this.mContorl0Y = mContorl0Y;
            this.mContorl1X = mContorl1X;
            this.mContorl1Y = mContorl1Y;
        }


        public static PathPoint newInstance(float x, float y, float fraction) {
            return new PathPoint(x, y).setFraction(fraction);
        }

        private PathPoint setFraction(float fraction) {
            this.fraction = fraction;
            return this;
        }
    }

}
