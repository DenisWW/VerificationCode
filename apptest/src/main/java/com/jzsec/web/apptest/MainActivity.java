package com.jzsec.web.apptest;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import com.jzsec.web.apptest.dialog.SimpleDialog;
import com.jzsec.web.apptest.weight.SimpleDynamicLayout;

import java.io.File;
import java.io.IOException;

public class MainActivity extends Activity implements ViewModelStoreOwner, LifecycleOwner {
    private final LifecycleRegistry lifecycle = new LifecycleRegistry(this);
    private ViewModelStore viewModelStore;

    public MainActivity() {
        lifecycle.addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                Log.e("onStateChanged", "====");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        UserViewModel userViewModel
                = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication()))
                .get(UserViewModel.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(Color.TRANSPARENT);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        findViewById(R.id.testa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleDialog(MainActivity.this).show();
            }
        });
        findViewById(R.id.testb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userViewModel.getUserList();
                Log.e("click", "=====");
            }
        });
        SimpleDynamicLayout dynamicLayout = findViewById(R.id.group_divider);
        TextView textView;
        for (int i = 0; i < 10; i++) {
            textView = new TextView(this);
            textView.setText("Test   " + (i % 2 == 1 ? 10000000 : 10));
            ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setPadding(40, 20, 40, 20);
            textView.setLayoutParams(layoutParams);
            dynamicLayout.addView(textView);
        }

        userViewModel.getLiveData()
                .observe(this, strings -> {
                    for (String s : strings) {
                        Log.e("s", "==" + s);
                    }
                });

//        Condition condition=new Lock().newCondition();
//        condition.await();
//        condition.signal();
//        ReentrantLock reentrantLock=new ReentrantLock();
//        reentrantLock.newCondition();


        new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                super.run();
//                PdfRenderer pdfRenderer= null;
//                try {
//                    pdfRenderer = new PdfRenderer(new File(""));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                pdfRenderer.openPage(0);
                try {
                    for (int i = 0; i < 100; i++) {
                        if (i == 50) {
                            throw new IOException("就是想抛出异常");
                        }
                        Log.e("Thread", "====" + i);
                        if (this.isInterrupted()) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    this.interrupt();
                    e.printStackTrace();
                }

            }
        }.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START);
    }

    @Override
    protected void onStop() {
        super.onStop();
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
    }

    @Override
    protected void onPause() {
        super.onPause();
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    }

    @CallSuper
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        lifecycle.markState(Lifecycle.State.CREATED);
        super.onSaveInstanceState(outState);
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        Log.e("getViewModelStore", "====");
//        if (mViewModelStore == null) {
//            NonConfigurationInstances nc =
//                    (NonConfigurationInstances) getLastNonConfigurationInstance();
//            if (nc != null) {
//                // Restore the ViewModelStore from NonConfigurationInstances
//                mViewModelStore = nc.viewModelStore;
//            }
//            if (mViewModelStore == null) {
//                mViewModelStore = new ViewModelStore();
//            }
//        }

        return viewModelStore == null ? new ViewModelStore() : viewModelStore;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        Log.e("onPointerCaptureChanged", "====" + hasCapture);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        Log.e("getLifecycle", "====");
        return lifecycle;
    }

}