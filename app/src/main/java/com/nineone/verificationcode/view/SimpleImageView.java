package com.nineone.verificationcode.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nineone.verificationcode.R;

public  class SimpleImageView extends androidx.appcompat.widget.AppCompatImageView {
    public SimpleImageView(@NonNull Context context) {
        super(context);
    }

    public SimpleImageView(@NonNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            Log.e("===", "===" + attrs.getAttributeCount());
            for (int i = 0; i < attrs.getAttributeCount(); i++) {
                Log.e("attrs", "Resource ===" + attrs.getAttributeNameResource(i) + "name===" + attrs.getAttributeName(i) + "getAttributeResourceValue===" + attrs.getAttributeResourceValue(i, 0));
                if ("src".equalsIgnoreCase(attrs.getAttributeName(i))) {
                    Log.e("src", "===" + attrs.getAttributeResourceValue(i, 0));
                    addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
                        @Override
                        public void onViewAttachedToWindow(View v) {
                            Log.e("AttachStateChange", "onViewAttachedToWindow===" + v);
                        }

                        @Override
                        public void onViewDetachedFromWindow(View v) {
                            Log.e("AttachStateChange", "onViewDetachedFromWindow===" + v);
                            removeOnAttachStateChangeListener(this);
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    @Override
    public void setImageDrawable(@Nullable @org.jetbrains.annotations.Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        Log.e("setImageDrawable", "===" + drawable);
//        R.styleable.ImageFilterView_altSrc
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);


    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        Log.e("setImageResource", "===");
    }


//    abstract void onSkinModeChange();
}
