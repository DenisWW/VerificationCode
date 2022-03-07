package com.jzsec.web.apptest.weight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jzsec.web.apptest.R;

public class SimpleHistogramView extends View {
    private Paint paint;
    private TextPaint textPaint;
    private float roundXY = 10f, leftMargin = 24f, titleMarginTop = 15f, titleMarginBottom = 5f, dataMarginBottom = 25f, dataMarginTop = 5f;
    private int rectangleWidth;
    private String[] titles = new String[]{"跌停", "≤-7%", "-7～-5%", "-5～-3%", "-3～0%",
            "平", "0～3%", "3～5%", "5～7%", "≥7%", "涨停"
    };
    private int[] data = new int[]{1, 2, 3, 4, 5,
            6, 7, 8, 9, 10, 11};
    private int titleHeight;
    private int dataHeight;

    private int max = 11;

    public SimpleHistogramView(Context context) {
        this(context, null);
    }

    public SimpleHistogramView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initPaint() {
        if (paint == null) paint = new Paint();
        if (textPaint == null) textPaint = new TextPaint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#12B370"));
        paint.setAntiAlias(true);
        paint.setDither(true);

        textPaint.setColor(Color.parseColor("#666666"));
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rectangleWidth = (int) ((getWidth() - (10 * leftMargin)) / 11);
        Rect rect = new Rect();
        setDataHeight(rect);
        setTitleHeight(rect);
        Path path = new Path();
        RectF rectF = new RectF();
        for (int position = 0; position < titles.length; position++) {
            drawRectangle(canvas, path, rectF, rect, position);
        }

    }

    private void setTitleHeight(Rect rect) {
        textPaint.setTextSize(rectangleWidth / 3f);
        textPaint.getTextBounds("$", 0, "$".length(), rect);
        titleHeight = (int) (rect.height() + titleMarginBottom + titleMarginTop);

    }

    private void setDataHeight(Rect rect) {
        textPaint.setTextSize(rectangleWidth / 2f);
        textPaint.getTextBounds("$", 0, "$".length(), rect);
        dataHeight = (int) (rect.height() + dataMarginBottom + dataMarginTop);
    }

    private void drawRectangle(Canvas canvas, Path path, RectF rectF, Rect rect, int position) {
        float h = 0f;
        int left = (int) (rectangleWidth * position + leftMargin * position);
        int right = left + rectangleWidth;

        int bottom = getHeight() - titleHeight;
        int topTotal = (int) (dataHeight + dataMarginBottom + dataMarginTop);
        int top = (int) ((bottom - topTotal) * (1f - data[position] * 1f / max)) + topTotal;
        rectF.set(left, top, right, bottom);
        setPaintColor(position);
        path.reset();
        path.addRoundRect(rectF, new float[]{roundXY, roundXY, roundXY, roundXY, 0, 0, 0, 0}, Path.Direction.CW);
        canvas.drawPath(path, paint);


        setTitleTextPaintColor(position);
        String title = titles[position];
        textPaint.getTextBounds(title, 0, title.length(), rect);
        int titleLeft = left + (rectangleWidth - rect.width()) / 2;
        canvas.drawText(title, titleLeft, getHeight() - titleMarginBottom, textPaint);


        setDataTextPaintColor(position);
        String data = String.valueOf(position * 100);
        textPaint.getTextBounds(data, 0, data.length(), rect);
        int dataLeft = left + (rectangleWidth - rect.width()) / 2;
        canvas.drawText(data, dataLeft, top - dataMarginBottom, textPaint);


    }

    private void setTitleTextPaintColor(int position) {
        if (position == titles.length - 1) {
            textPaint.setColor(Color.parseColor("#EB4A38"));
        } else if (position == 0) {
            textPaint.setColor(Color.parseColor("#12B370"));
        } else {
            textPaint.setColor(Color.parseColor("#666666"));
        }
        textPaint.setTextSize(rectangleWidth / 3f);
    }

    private void setDataTextPaintColor(int position) {
        textPaint.setColor(Color.parseColor("#666666"));
        textPaint.setTextSize(rectangleWidth / 2f);
    }

    private void setPaintColor(int position) {
        if (position > titles.length / 2) {
            paint.setColor(Color.parseColor("#EB4A38"));
        } else if (position < titles.length / 2) {
            paint.setColor(Color.parseColor("#12B370"));
        } else {
            paint.setColor(Color.parseColor("#cccccc"));
        }

    }
}
