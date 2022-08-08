package com.nineone.verificationcode.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.MemoryFile;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nineone.verificationcode.R;
import com.nineone.verificationcode.bean.UserBean;
import com.nineone.verificationcode.utils.ToastUtils;
import com.nineone.verificationcode.view.AutoTextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class BesselActivity extends AppCompatActivity {
    private ImageView login_bg;
    private ImageView login_top_icon;
    private int screenW;
    private AutoTextView textView;

    private ArrayList<UserBean> userBeans1 = new ArrayList<>();
    private ArrayList<UserBean> userBeans2 = new ArrayList<>();
    int[] mTestInts = new int[2];
    ArrayList<Integer> mIntList = new ArrayList<>(2);
    int mInt = 23;
    Random mRandom = new Random();


    static {
        System.loadLibrary("native-lib");
    }

    public native String stringFromJNI(String s1, String s2);

    private int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bessel);
        initView();
        Color.argb(0,0,0,0);
//        startAnimator();
//        ++i;
        Log.e("onCreate", "=====" + (++i == 1));
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
        textView = findViewById(R.id.auto_text_view);
        TextView dc_go = findViewById(R.id.dc_go);
        dc_go.setOnClickListener(v -> {

            Intent intent = new Intent(v.getContext(), DCStockDetailActivity.class);
            intent.putExtra(DCStockDetailActivity.STOCK_NAME, "比亚迪");
            intent.putExtra(DCStockDetailActivity.STOCK_CODE, "byd");
            startActivity(intent);
        });
        login_top_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showCusToast(BesselActivity.this, "测试一下");
            }
        });
        TextView ths_go = findViewById(R.id.ths_go);
        ths_go.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), OpenGlActivity.class);
            startActivity(intent);
        });
        dc_go.setText(stringFromJNI("why", "why2"));
        textView.setText("就是看看100");
//        textView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                textView.setText("100");
//            }
//        }, 5000);
//
//        textView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                textView.setText("就是看看100");
//            }
//        }, 10000);

        String text = "就是看看10000000000";
        StaticLayout staticLayout = new StaticLayout(text, 0, text.length(), textView.getPaint(),
                (int) (getResources().getDisplayMetrics().density * 50F),
                Layout.Alignment.ALIGN_NORMAL, 0F, 1F, true, TextUtils.TruncateAt.END,
                (int) (getResources().getDisplayMetrics().density * 50F)
        );
        DynamicLayout dynamicLayout = new DynamicLayout(text, text, textView.getPaint(),
                (int) (getResources().getDisplayMetrics().density * 50F),
                Layout.Alignment.ALIGN_NORMAL, 0F, 1F, false, TextUtils.TruncateAt.END,
                (int) (getResources().getDisplayMetrics().density * 50F));

        int staticLine = staticLayout.getLineCount();
        int dynamicLine = dynamicLayout.getLineCount();
        CharSequence s = TextUtils.ellipsize(text, textView.getPaint(), getResources().getDisplayMetrics().density * 50F, TextUtils.TruncateAt.END);
//        try {
//            MemoryFile memoryFile=new MemoryFile("",1024);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        for (int i = 0; i < 5; i++) {
            UserBean userBean = new UserBean();
            userBean.name = "name" + i;
            userBean.age = i;
            userBeans1.add(userBean);
        }

        test();
    }


    public void test() {
        loadInts(mTestInts);
        loadIntArray(mIntList);
        loadInt(mInt);

        System.out.println("int[] first:" + mTestInts[0] + "\tsecond:" + mTestInts[1]);
        System.out.println("list first:" + mIntList.get(0) + "\tsecond:" + mIntList.get(1));
        System.out.println("mInt:" + mInt);
    }

    public void loadInt(int tempInt) {
        tempInt += 52;
    }

    public void loadIntArray(ArrayList<Integer> list) {
        list.add(mRandom.nextInt(10));
        list.add(mRandom.nextInt(10));
    }

    public void loadInts(int[] ints) {
        if (ints instanceof Object) {
            System.out.println("int[] extents Object");
        }
        ints[0] = mRandom.nextInt(10);
        ints[1] = mRandom.nextInt(10);
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
