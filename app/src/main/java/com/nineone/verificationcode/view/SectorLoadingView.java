package com.nineone.verificationcode.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class SectorLoadingView extends View {
    private Paint paint;
    private float sweepAngle;
    private ValueAnimator animator;
    private int ovalWidth =2;
    private float strokeWidth;
    private RectF rectFOval;

    public void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
        invalidate();
    }

    public SectorLoadingView(Context context) {
        super(context);
        init();
    }

    public SectorLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    boolean isFirstDraw = false;
    private RectF rectF;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (rectF == null) rectF = new RectF(0, 0, getWidth(), getHeight());
        paint.setStyle(Paint.Style.FILL);
        canvas.drawArc(rectF, -90, sweepAngle, true, paint);
//        if (!isFirstDraw) {
//            isFirstDraw=true;
//            paint.setStyle(Paint.Style.STROKE);
//            paint.setStrokeWidth(4);
//            canvas.drawOval(rectF, paint);
//            paint.setStyle(Paint.Style.FILL);
//        }

        paint.setStyle(Paint.Style.STROKE);
        if (strokeWidth == 0) strokeWidth = paint.getStrokeWidth();
        paint.setStrokeWidth(ovalWidth);
        if (rectFOval == null) {
            rectFOval = new RectF();
            rectFOval.set(rectF.left + ovalWidth -1, rectF.top + ovalWidth - 1,
                    rectF.right - ovalWidth + 1, rectF.bottom - ovalWidth +1);
        }
        canvas.drawOval(rectFOval, paint);

        if (!animator.isStarted()) {
            animator.start();
        }

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void init() {
        if (paint == null) {
            paint = new Paint();
            paint.setColor(Color.RED);
            paint.setAntiAlias(true);//取消锯齿
        }
        if (animator == null) {
            animator = ObjectAnimator.ofFloat(0, 360);
            animator.setDuration(3000);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    sweepAngle = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
//            animator.start();

        }
    }


}
