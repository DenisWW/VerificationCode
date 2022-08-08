package com.nineone.verificationcode.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.nineone.verificationcode.R;

public class CircleView extends androidx.appcompat.widget.AppCompatImageView {
    private Paint paint;

    public CircleView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (paint == null) paint = new Paint();
    }

    public CircleView(Context context, AttributeSet attrs) {
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
        kuang.addCircle(getWidth() / 2f, getHeight() / 2f, getHeight() / 2f, Path.Direction.CCW);
        path.op(kuang, Path.Op.DIFFERENCE);
        super.onDraw(canvas);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        canvas.drawPath(path, paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        paint.setXfermode(null);
        canvas.restoreToCount(layerID);
//        ViewGroup viewGroup;
//        viewGroup.setMeasureDimension();
    }

//    android.graphics.PorterDuff.Mode.SRC      : 只绘制源图像
//    android.graphics.PorterDuff.Mode.DST      : 只绘制目标图像
//    android.graphics.PorterDuff.Mode.DST_OVER : 在源图像的顶部绘制目标图像
//    android.graphics.PorterDuff.Mode.DST_IN   : 只在源图像和目标图像相交的地方绘制目标图像
//    android.graphics.PorterDuff.Mode.DST_OUT  : 只在源图像和目标图像不相交的地方绘制目标图像
//    android.graphics.PorterDuff.Mode.DST_ATOP : 在源图像和目标图像相交的地方绘制目标图像，在不相交的地方绘制源图像
//    android.graphics.PorterDuff.Mode.SRC_OVER : 在目标图像的顶部绘制源图像
//    android.graphics.PorterDuff.Mode.SRC_IN   : 只在源图像和目标图像相交的地方绘制源图像  取交集 显示后画
//    android.graphics.PorterDuff.Mode.SRC_OUT  : 只在源图像和目标图像不相交的地方绘制源图像
//    android.graphics.PorterDuff.Mode.SRC_ATOP : 在源图像和目标图像相交的地方绘制源图像，在不相交的地方绘制目标图像
//    android.graphics.PorterDuff.Mode.XOR      : 在源图像和目标图像重叠之外的任何地方绘制他们，而在不重叠的地方不绘制任何内容
//    android.graphics.PorterDuff.Mode.LIGHTEN  : 获得每个位置上两幅图像中最亮的像素并显示
//    android.graphics.PorterDuff.Mode.DARKEN   : 获得每个位置上两幅图像中最暗的像素并显示
//    android.graphics.PorterDuff.Mode.MULTIPLY : 将每个位置的两个像素相乘，除以255，然后使用该值创建一个新的像素进行显示。结果颜色=顶部颜色*底部颜色/255
//    android.graphics.PorterDuff.Mode.SCREEN   : 反转每个颜色，执行相同的操作（将他们相乘并除以255），然后再次反转。结果颜色=255-(((255-顶部颜色)*(255-底部颜色))/255)

}
