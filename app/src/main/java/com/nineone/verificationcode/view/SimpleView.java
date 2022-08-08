package com.nineone.verificationcode.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class SimpleView extends View {
    public SimpleView(Context context) {
        super(context);
    }

    public SimpleView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("SimpleView", " onTouchEvent==="+event.getAction());
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.e("SimpleView", " dispatchTouchEvent==="+event.getAction());
        return super.dispatchTouchEvent(event);
    }
}
