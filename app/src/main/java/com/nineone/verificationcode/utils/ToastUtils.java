package com.nineone.verificationcode.utils;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.nineone.verificationcode.R;

import java.lang.reflect.Field;

public class ToastUtils {
    private static Field sField_TN;
    private static Field sField_TN_Handler;

    static {
        //安卓7.1的做处理，其它版本系统的不用处理
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
            try {
                sField_TN = Toast.class.getDeclaredField("mTN");
                sField_TN.setAccessible(true);
                sField_TN_Handler = sField_TN.getType().getDeclaredField("mHandler");
                sField_TN_Handler.setAccessible(true);
            } catch (Exception ignored) {
            }
        }
    }

    private static void hook(Toast toast) {
        try {
            Object tn = sField_TN.get(toast);
            Handler preHandler = (Handler) sField_TN_Handler.get(tn);
            sField_TN_Handler.set(tn, new SafelyHandlerWarpper(preHandler));
        } catch (Exception ignored) {
        }
    }

    /**
     * 自定义Handler catch处理异常
     */
    public static class SafelyHandlerWarpper extends Handler {
        private final Handler impl;

        public SafelyHandlerWarpper(Handler impl) {
            this.impl = impl;
        }

        @Override
        public void dispatchMessage(Message msg) {
            try {
                super.dispatchMessage(msg);
            } catch (Exception ignored) {
            }
        }

        @Override
        public void handleMessage(Message msg) {
            impl.handleMessage(msg);//需要委托给原Handler执行
        }
    }

    private static long oneTimeCus = 0;
    private static long twoTimeCus = 0;
    private static String oldMsg = "";
    private static Toast mCusToast;

    public static void showCusToast(Context context, String msg) {
        showCusToast(context, msg, Gravity.CENTER, 0);
    }

    public static void showCusToast(Context context, String msg, int gravity, int yOffset) {
        if (twoTimeCus - oneTimeCus < Toast.LENGTH_SHORT && oldMsg.equalsIgnoreCase(msg)) return;
        if (mCusToast == null) {
            createCusToast(context, msg, gravity, yOffset);
        } else {
            try {
                LinearLayout layout = (LinearLayout) mCusToast.getView();
                ((TextView) layout.getChildAt(0)).setText(msg);
            } catch (Exception e) {
                createCusToast(context, msg, gravity, yOffset);
                e.printStackTrace();
            }
        }
        twoTimeCus = System.currentTimeMillis();
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) hook(mCusToast);
        if (twoTimeCus - oneTimeCus > Toast.LENGTH_SHORT) {
            oneTimeCus = twoTimeCus;
            oldMsg = msg;
            mCusToast.show();
        }
    }

    private static void createCusToast(Context context, String msg, int gravity, int yOffset) {
        mCusToast = new Toast(context);
        mCusToast.setGravity(gravity, 0, yOffset);
        mCusToast.setDuration(Toast.LENGTH_SHORT);
        oneTimeCus = System.currentTimeMillis();
        LinearLayout layout = new LinearLayout(context);
        TextView textView = new TextView(context);
        layout.setBackgroundResource(R.drawable.solid_ff9500_5);
        textView.setTextColor(ContextCompat.getColor(context, R.color.white));
        layout.setPadding((int) context.getResources().getDisplayMetrics().density * 12,
                (int) context.getResources().getDisplayMetrics().density * 12,
                (int) context.getResources().getDisplayMetrics().density * 12,
                (int) context.getResources().getDisplayMetrics().density * 12);
        layout.addView(textView);
        textView.setText(msg);
        textView.setTextSize(14F);
        mCusToast.setView(layout);
    }

}
