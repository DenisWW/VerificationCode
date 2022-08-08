package com.nineone.verificationcode.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;
import android.util.Log;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.nineone.verificationcode.R;


public class CenterAlignImageSpan extends ImageSpan {
    private Context context;

    public CenterAlignImageSpan(Drawable drawable) {
        super(drawable);
    }

    public CenterAlignImageSpan(Context context, @DrawableRes int drawable) {
        super(context, drawable);
        this.context = context;

    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x,
                     int top, int y, int bottom, @NonNull Paint paint) {
//        Paint.FontMetricsInt metricsInt = paint.getFontMetricsInt();
//        top = 0;
//        bottom = metricsInt.bottom - metricsInt.top;
//        int hw = metricsInt.descent - metricsInt.ascent;
//        super.draw(canvas, text, start, end, x, top, y, bottom, paint);
//        Path path = new Path();
//        canvas.drawText(text, 0, text.length(), 0, hw, paint);
        Drawable b = getDrawable();
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
//        float transY = (y + fm.descent + y + fm.ascent) / 2 - b.getBounds().bottom / 2;
//        float transY = (bottom - (fm.descent - fm.ascent)) / 2 + (bottom - b.getBounds().bottom) / 2;
//        float transY = y + fm.ascent + ((fm.bottom - fm.ascent) - b.getBounds().bottom) / 2F;
//        float transY =  (bottom - ((fm.bottom - fm.ascent) - b.getBounds().height()) / 2F - b.getBounds().height());
        float transY = top + (bottom - top - b.getBounds().height()) / 2F + (fm.bottom - fm.top - fm.descent + fm.ascent) / 2F;
//        float transY = bottom - (bottom - y) / 2F - b.getBounds().height();
        Log.e("draw", "====" + b.getBounds().height()
                + "   transY== " + transY
                + "   y== " + y
                + "   fm==" + (fm.descent - fm.ascent)
                + "   fm.d==" + (fm.bottom - fm.top)
                + "   top==" + top
                + "   bottom==" + bottom
        );
//        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.stroke_eb4a38_1, null);
//        drawable.setBounds(new Rect((int) paint.measureText(text.toString()), 0, fm.bottom - fm.top + (int) paint.measureText(text.toString()), fm.bottom - fm.top));
//        drawable.draw(canvas);
        canvas.save();
        canvas.translate(x, transY);//绘制图片位移一段距离
        b.draw(canvas);
        canvas.restore();


    }
}
