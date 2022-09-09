package com.nineone.verificationcode.view;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SkinPatcher {

    private SkinPatcher() {
    }


    private static final class SkinPatcherHolder {
        static final SkinPatcher skinPatcher = new SkinPatcher();
    }

    public static SkinPatcher get() {
        return SkinPatcherHolder.skinPatcher;
    }

    public void autoSkin(AttributeSet attrs, ImageView imageView) {
        if (attrs == null) return;
        Log.e("autoSkin", "===" + attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0));
        int srcResourceId = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);
        if (srcResourceId != 0) {
            String resourceKey = imageView.getResources().getResourceName(attrs.getAttributeResourceValue(srcResourceId, 0));

        }

//        for (int i = 0; i < attrs.getAttributeCount(); i++) {
//            Log.e("attrs", "Resource ===" + attrs.getAttributeNameResource(i)
//                    + "name===" + attrs.getAttributeName(i)
//                    + "Value===" + attrs.getAttributeResourceValue(i, 0)
//            );
//            if ("src".equalsIgnoreCase(attrs.getAttributeName(i))) {
//                //TODO 根据资源name，获取对应皮肤的value
//                Log.e("src", "===" + getResources().getResourceName(attrs.getAttributeResourceValue(i, 0)));
//            }
//        }
    }

    public void autoSkin(AttributeSet attrs, TextView textView) {
        if (attrs == null) return;
        Log.e("autoSkin", "===" + attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "textColor", 0));
        int srcResourceId = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "textColor", 0);
        if (srcResourceId != 0) {
            String resourceKey = textView.getResources().getResourceName(attrs.getAttributeResourceValue(srcResourceId, 0));

        }
//        for (int i = 0; i < attrs.getAttributeCount(); i++) {
//            Log.e("attrs", "Resource ===" + attrs.getAttributeNameResource(i)
//                    + "name===" + attrs.getAttributeName(i)
//                    + "Value===" + attrs.getAttributeResourceValue(i, 0)
//            );
//            if ("src".equalsIgnoreCase(attrs.getAttributeName(i))) {
//                //TODO 根据资源name，获取对应皮肤的value
//                Log.e("src", "===" + getResources().getResourceName(attrs.getAttributeResourceValue(i, 0)));
//            }
//        }
    }

    public void autoBindLifecycle(View view) {
        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                Log.e("AttachStateChange", "onViewAttachedToWindow===" + v);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                Log.e("AttachStateChange", "onViewDetachedFromWindow===" + v);
                view.removeOnAttachStateChangeListener(this);
            }
        });
    }
}
