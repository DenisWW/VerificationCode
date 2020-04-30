package com.nineone.verificationcode.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;


import com.nineone.verificationcode.R;

import androidx.core.content.ContextCompat;

public class MineImageView extends androidx.appcompat.widget.AppCompatImageView {
    private Paint paint;
    private float width;
    private float height;

    public MineImageView(Context context) {
        this(context, null);
        paint = new Paint();
    }

    public MineImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    private int offset;
    private Bitmap seekBitmap;

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        int layerID = canvas.saveLayer(0.0f, 0.0f, width, height, paint, Canvas.ALL_SAVE_FLAG);
        super.onDraw(canvas);
        if (!isBg) {
            if (getDrawable() != null) {
                seekPath = getBlockShape(false);
            }
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            canvas.drawPath(seekPath, paint);
            paint.setXfermode(null);
            canvas.restoreToCount(layerID);
        }else {
            if (getDrawable() != null) {
                paint.setColor(ContextCompat.getColor(getContext(), R.color.trans));
                shawPath = getBlockShape(true);
                canvas.drawPath(shawPath, paint);
                canvas.restoreToCount(layerID);
                onBitmapListener();

            }
        }
    }

    private void onBitmapListener() {
        if (onLayoutListener != null)
            onLayoutListener.onLayoutListener(((BitmapDrawable) getDrawable()).getBitmap(), getImageMatrix(), getWidth(), getHeight());
    }


    private Path shawPath;
    private Path seekPath;
    private int w, cw, r;
    private int startX, startY;

    public void setOffset(int offset) {
        this.offset = offset;
        invalidate();
    }

    public Path getBlockShape(boolean isBg) {

        int w = getHeight() / 3;
        startX = getWidth() / 3 * 2;
        startY = getHeight() / 3;
        cw = w / 3;
        r = cw / 10;
        Path path = new Path();
        path.moveTo(startX, startY);
        path.rLineTo(cw, 0);
        path.rCubicTo(-r, -cw, cw + r, -cw, cw, 0);
        path.rLineTo(cw, 0);

        path.rLineTo(0, cw);
        path.rCubicTo(cw, -r, cw, cw + r, 0, cw);
        path.rLineTo(0, cw);

        path.rLineTo(-w, 0);

        path.rLineTo(0, -cw);
        path.rCubicTo(cw, r, cw, -cw - r, 0, -cw);
//        path.rLineTo(0, -cw);
        path.close();
        if (!isBg) {
            Path path1 = new Path();
            RectF f = new RectF(0, 0, getWidth(), getHeight());
            path1.addRect(f, Path.Direction.CCW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                path1.op(path, Path.Op.DIFFERENCE);
            }
            path1.close();
            return path1;
        }
        return path;
    }

    private boolean isBg;

    public void setIsBg(boolean b) {
        isBg = b;

    }

    private OnLayoutListener onLayoutListener;

    public void setOnLayoutListener(OnLayoutListener onLayoutListener) {
        this.onLayoutListener = onLayoutListener;
    }

    public interface OnLayoutListener {
        void onLayoutListener(Bitmap bitmap, Matrix matrix, int w, int h);
    }


/**
 *

 private Canvas canvas;

 private Bitmap getMaskBitmap(Bitmap mBitmap, Path mask) {


 Paint paint = new Paint();
 paint.setAntiAlias(true);
 Bitmap tempBitmap;
 if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
 tempBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888, true);
 } else {
 tempBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
 }
 canvas.restore();
 canvas.setMatrix(getImageMatrix());
 canvas.setBitmap(tempBitmap);
 //        canvas.drawPath(mask, paint);

 //有锯齿 且无法解决,所以换成XFermode的方法做
 //        mCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
 //设置遮罩模式(图像混合模式)
 //        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
 canvas.drawBitmap(mBitmap, getImageMatrix(), paint);
 canvas.clipPath(mask);

 paint.setXfermode(null);

 return tempBitmap;
 }

 */
}
