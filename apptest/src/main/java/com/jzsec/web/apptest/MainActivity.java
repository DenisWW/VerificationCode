package com.jzsec.web.apptest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jzsec.web.apptest.dialog.SimpleDialog;
import com.jzsec.web.testa.MainActivityA;
import com.jzsec.web.testb.MainActivityB;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.testa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, MainActivityA.class));
                new SimpleDialog(MainActivity.this).show();
            }
        });
        findViewById(R.id.testb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivityB.class));
            }
        });
    }
}