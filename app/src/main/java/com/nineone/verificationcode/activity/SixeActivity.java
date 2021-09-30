package com.nineone.verificationcode.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.nineone.verificationcode.R;

import java.util.List;

public class SixeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sixe);
        findViewById(R.id.tv).setOnClickListener(v -> {
            ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                List<ActivityManager.AppTask> list = am.getAppTasks();
                for (int i = 0; i < list.size(); i++) {
                    ActivityManager.AppTask appTask = list.get(i);
                    Log.e("am", "==" +appTask.getTaskInfo().toString());
                }

            }

            Intent intent = new Intent(SixeActivity.this, ThreeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }
}