package com.nineone.verificationcode.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nineone.verificationcode.R;

public class XWebView extends WebView {
    public XWebView(@NonNull Context context) {
        this(context,null);
        Log.e("XWebView===", "===========1");
    }

    public XWebView(@NonNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.e("XWebView===", "===========2");
    }

}
