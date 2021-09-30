package com.nineone.verificationcode.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.nineone.verificationcode.R;


public class LinearProgressView extends View {
    private Paint paint;
    private RectF rectF;
    private RectF rectFOval;
    private RectF rectFOvalRing;
    private int ovalWidth = 10;
    private int STROKE = 1;
    private float sweepAngle = 50;

    public LinearProgressView(Context context) {
        super(context);
        init();
    }

    public void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle * 360f;
        invalidate();
    }

    private void init() {
        if (paint == null) paint = new Paint();
        this.paint.setAntiAlias(true); //消除锯齿
        this.paint.setStyle(Paint.Style.STROKE);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.color_83b2fd));


    }

    public LinearProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private boolean isFirst;
    private int anInt = 10;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        if (rectF == null) rectF = new RectF(0, 0, getWidth(), getHeight());
//        if (rectFOval == null) {
//            rectFOval = new RectF();
//            rectFOval.set(rectF.left + ovalWidth * 2 - STROKE, rectF.top + ovalWidth * 2 - STROKE, rectF.right - ovalWidth * 2 + STROKE
//                    , rectF.bottom - ovalWidth * 2 + STROKE);
//        }
//        if (rectFOvalRing == null) {
//            rectFOvalRing = new RectF();
//            rectFOvalRing.set(rectF.left + ovalWidth - STROKE, rectF.top + ovalWidth - STROKE, rectF.right - ovalWidth + STROKE, rectF.bottom - ovalWidth + STROKE);
//        }
//
//        paint.reset();
//        paint.setAntiAlias(true); //消除锯齿
//        paint.setStyle(Paint.Style.FILL);
//        paint.setColor(ContextCompat.getColor(getContext(), R.color.white));
//        canvas.drawOval(rectFOvalRing, paint);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(ovalWidth);
//        paint.setColor(ContextCompat.getColor(getContext(), R.color.color_bb7af2));
//        canvas.drawOval(rectFOvalRing, paint);

//        paint.reset();

        @SuppressLint("DrawAllocation") Path path = new Path();

        RectF topR1 = new RectF(-20, getHeight() / 3, getWidth() / 3 + anInt, getHeight() * 2 / 3);
        RectF centerR1 = new RectF(getWidth() / 3 , getHeight() * 2 / 3, getWidth() * 2 / 3 + anInt, getHeight());
        RectF bottomR1 = new RectF(getWidth() * 2 / 3 - anInt, 0, getWidth() + anInt, getHeight() / 3);

        RectF topR2 = new RectF(-20, getHeight() * 2 / 3 - 30, getWidth() / 3, getHeight() * 2 / 3 + 30);
        RectF centerR2 = new RectF(getWidth() / 3+anInt, getHeight() * 2 / 3 - 30, getWidth() * 2 / 3, getHeight() * 2 / 3 + 30);
        RectF bottomR2 = new RectF(getWidth() * 2 / 3 - anInt, 0, getWidth() + anInt, getHeight() / 3);

        Path tp1 = new Path();
        Path cp1 = new Path();
        Path bp1 = new Path();

        tp1.addRoundRect(topR1, 10, 10, Path.Direction.CCW);
        cp1.addRoundRect(centerR1, 10, 10, Path.Direction.CCW);
        bp1.addRoundRect(bottomR1, 10, 10, Path.Direction.CCW);


        Path tp2 = new Path();
        Path cp2 = new Path();
        Path bp2 = new Path();

        Path tp3 = new Path();
        Path cp3 = new Path();
        Path bp3 = new Path();

        tp2.addRoundRect(topR2, 10, 10, Path.Direction.CCW);
        tp1.op(tp2,Path.Op.DIFFERENCE);
        path.addPath(tp1);
        cp2.addRoundRect(centerR2, 10, 10, Path.Direction.CCW);
        cp1.op(cp2,Path.Op.DIFFERENCE);
        path.addPath(cp1);
//        bp3.addRoundRect(bottomR2, 10, 10, Path.Direction.CCW);

        @SuppressLint("DrawAllocation") LinearGradient linearGradient = new LinearGradient(0f, 0f, 0f, getHeight(), ContextCompat.getColor(getContext(), R.color.color_F29E4A),
                ContextCompat.getColor(getContext(), R.color.color_4F22FF), Shader.TileMode.REPEAT);
        paint.setShader(linearGradient);
        paint.setStrokeWidth(ovalWidth);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true); //消除锯齿
        canvas.drawPath(path, paint);
//        canvas.drawArc(rectFOvalRing, -90, 180, false, paint);
    }
}
