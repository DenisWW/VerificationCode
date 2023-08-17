package com.nineone.verificationcode.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;

public class ImageView extends androidx.appcompat.widget.AppCompatImageView {
    private Paint paint;

    private int radius = 10;

    public ImageView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (paint == null) paint = new Paint();
    }

    public ImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        int layerID = canvas.saveLayer(0.0f, 0.0f, getWidth(), getHeight(), paint, Canvas.ALL_SAVE_FLAG);
        Path path = new Path();
        path.addRect(new RectF(0, 0, getWidth(), getHeight()), Path.Direction.CCW);
        Path kuang = new Path();
        kuang.addRoundRect(new RectF(0, 0, getWidth(), getHeight()), radius, radius, Path.Direction.CCW);
        path.op(kuang, Path.Op.DIFFERENCE);
        super.onDraw(canvas);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        canvas.drawPath(path, paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        paint.setXfermode(null);
        canvas.restoreToCount(layerID);
    }

}
