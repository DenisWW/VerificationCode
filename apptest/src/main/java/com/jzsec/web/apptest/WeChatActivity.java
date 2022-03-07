package com.jzsec.web.apptest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jzsec.web.apptest.dialog.SimpleDialog;

public class WeChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_we_chat);
        findViewById(R.id.back_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView user_tv = findViewById(R.id.user_tv);
        TextView phone_tv = findViewById(R.id.phone_tv);
        findViewById(R.id.user_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDialog simpleDialog = new SimpleDialog(v.getContext());
                simpleDialog. setOnTextListener(new SimpleDialog.OnTextListener() {
                    @Override
                    public void onText(String s) {
                        user_tv.setText(s);
                    }
                });
                simpleDialog.show();
            }
        });
        findViewById(R.id.phone_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDialog simpleDialog = new SimpleDialog(v.getContext());
                simpleDialog. setOnTextListener(new SimpleDialog.OnTextListener() {
                    @Override
                    public void onText(String s) {
                        phone_tv.setText(s);
                    }
                });
                simpleDialog.show();
            }
        });
    }
}