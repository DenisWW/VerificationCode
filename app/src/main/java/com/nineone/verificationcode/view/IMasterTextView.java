package com.nineone.verificationcode.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class IMasterTextView extends androidx.appcompat.widget.AppCompatTextView {
    public IMasterTextView(@NonNull Context context) {
        this(context, null);
    }

    public IMasterTextView(@NonNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        SkinPatcher.get().autoSkin(attrs, this);
    }

    @Override
    public void setTextColor(int color) {
//        super.setTextColor(color);
    }
}
