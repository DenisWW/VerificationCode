package com.nineone.verificationcode.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class DragImageView extends FrameLayout implements MineImageView.OnLayoutListener {

    private MineImageView bg;
    private MineImageView progress;
    private int transX;

    public DragImageView(@NonNull Context context) {
        this(context, null);
    }

    public DragImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredHeight() != 0) {
            bg.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, getMeasuredHeight()));
            progress.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, getMeasuredHeight()));
            progress.setTranslationX(transX = -getMeasuredWidth() / 2);
        }

    }

    private void initView() {
        if (bg == null) {
            bg = new MineImageView(getContext());
            bg.setIsBg(true);
            bg.setOnLayoutListener(this);
            bg.setScaleType(ImageView.ScaleType.FIT_XY);
            addView(bg);
        }
        if (progress == null) {
            progress = new MineImageView(getContext());
            progress.setIsBg(false);
            addView(progress);
            progress.setScaleType(ImageView.ScaleType.FIT_XY);
            progress.setVisibility(VISIBLE);

        }
    }


    @Override
    public void onLayoutListener(Bitmap bitmap, Matrix matrix, int w, int h) {
//        progress.setImageDrawable(bitmap);
    }

    public MineImageView getBgImageView() {
        return bg;
    }

    public MineImageView getProgressImageView() {
        return progress;
    }

    public void setOnDistance() {

    }

    private OnDistance onDistance;
    private int d;

    public void setOffset(int offset) {
        progress.setTranslationX(d = transX + offset);
        if (onDistance != null) {
            onDistance.onDistance(d);
        }
    }

    public interface OnDistance {
        void onDistance(int d);
    }
}
