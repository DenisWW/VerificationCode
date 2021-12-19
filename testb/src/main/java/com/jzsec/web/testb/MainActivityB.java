package com.jzsec.web.testb;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

//import com.bumptech.glide.Glide;

public class MainActivityB extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_b);
        ImageView testb_iv = findViewById(R.id.testb_iv);
//        Glide.with(this).load(R.mipmap.test).into(testb_iv);

    }
}