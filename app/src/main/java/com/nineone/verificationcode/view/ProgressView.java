package com.nineone.verificationcode.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.nineone.verificationcode.R;


public class ProgressView extends View {
    private Paint paint;
    private RectF rectF;
    private RectF rectFOval;
    private RectF rectFOvalRing;
    private int ovalWidth = 10;
    private int STROKE = 1;
    private float sweepAngle = 50;

    public ProgressView(Context context) {
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

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private boolean isFirst;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (rectF == null) rectF = new RectF(0, 0, getWidth(), getHeight());
        if (rectFOval == null) {
            rectFOval = new RectF();
            rectFOval.set(rectF.left + ovalWidth * 2 - STROKE, rectF.top + ovalWidth * 2 - STROKE, rectF.right - ovalWidth * 2 + STROKE
                    , rectF.bottom - ovalWidth * 2 + STROKE);
        }
        if (rectFOvalRing == null) {
            rectFOvalRing = new RectF();
            rectFOvalRing.set(rectF.left + ovalWidth - STROKE, rectF.top + ovalWidth - STROKE, rectF.right - ovalWidth + STROKE, rectF.bottom - ovalWidth + STROKE);
        }

        paint.reset();
        paint.setAntiAlias(true); //消除锯齿
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.white));
        canvas.drawOval(rectFOvalRing, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(ovalWidth);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.color_bb7af2));
        canvas.drawOval(rectFOvalRing, paint);

        paint.reset();

        @SuppressLint("DrawAllocation") LinearGradient linearGradient = new LinearGradient(0f, 0f, 0f, getHeight(), ContextCompat.getColor(getContext(), R.color.color_ff70ae),
                ContextCompat.getColor(getContext(), R.color.color_bb7af2), Shader.TileMode.REPEAT);
        paint.setShader(linearGradient);
        paint.setStrokeWidth(ovalWidth);
        paint.setAntiAlias(true); //消除锯齿
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(rectFOvalRing, -90, sweepAngle, false, paint);
    }
}
