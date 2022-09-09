package com.nineone.verificationcode.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;

public class IMasterImageView extends androidx.appcompat.widget.AppCompatImageView {
    public IMasterImageView(Context context) {
        this(context, null);
    }

    public IMasterImageView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        SkinPatcher.get().autoSkin(attrs, this);
        SkinPatcher.get().autoBindLifecycle( this);
    }

    private void setLiveStatus() {

    }

    @Override
    public void setImageResource(int resId) {
//        super.setImageResource(resId);
        getResources().getResourceName(resId);

    }
}
