package com.nineone.verificationcode.base;

import android.app.Application;

import androidx.annotation.NonNull;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyApplication extends Application {
    @Override
    public void onCreate() {

        Lock lock = new ReentrantLock();
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {

            }
        });
//       new  DoKit.Builder(this).build();

    }
}
