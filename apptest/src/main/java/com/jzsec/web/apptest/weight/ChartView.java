package com.jzsec.web.apptest.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class ChartView extends View {
    private Paint paint;
    private float d = 15f;
    private float centerMargin = 20f;
    private float centerWidth = 20f;
    private float centerTotalHalfWidth;
    private int current;

    public ChartView(Context context) {
        this(context, null);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        if (paint == null) paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        current = getWidth() / 2;
        d = getHeight() * 2 / 3f;
        centerTotalHalfWidth = (centerMargin * 2 + centerWidth) / 2;
        drawLeftLine(canvas);
        drawCenterLine(canvas);
        drawRightLine(canvas);
    }

    private void drawLeftLine(Canvas canvas) {
        Path path = new Path();
        float right = current - centerTotalHalfWidth;
        path.addRoundRect(new RectF(0, 0, right - d, getHeight()),
                new float[]{getHeight(), getHeight(), 0, 0, 0, 0, getHeight(), getHeight()}, Path.Direction.CCW);
        path.moveTo(right - d, 0);
        path.lineTo(right, 0);
        path.lineTo(right - d, getHeight());
        path.close();
        paint.setColor(Color.parseColor("#12B370"));
        canvas.drawPath(path, paint);
    }

    private void drawCenterLine(Canvas canvas) {
        Path path = new Path();
        float left = current - centerTotalHalfWidth + centerMargin;
        path.moveTo(left, 0);
        path.lineTo(left + centerWidth, 0);
        path.lineTo(left + centerWidth - d, getHeight());
        path.lineTo(left - d, getHeight());
        path.close();
        paint.setColor(Color.parseColor("#CCCCCC"));
        canvas.drawPath(path, paint);
    }

    private void drawRightLine(Canvas canvas) {
        Path path = new Path();
        float left = current + centerTotalHalfWidth;
        path.addRoundRect(new RectF(left, 0, getWidth(), getHeight()),
                new float[]{0, 0, getHeight(), getHeight(), getHeight(), getHeight(), 0, 0}, Path.Direction.CCW);
        path.moveTo(left, 0);
        path.lineTo(left - d, getHeight());
        path.lineTo(left, getHeight());
        path.close();
        paint.setColor(Color.parseColor("#EB4A38"));
        canvas.drawPath(path, paint);

    }


}
