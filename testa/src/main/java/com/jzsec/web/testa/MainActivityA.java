package com.jzsec.web.testa;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

//import com.bumptech.glide.Glide;

public class MainActivityA extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_a);
        ImageView testa_iv=findViewById(R.id.testa_iv);
//        Glide.with(this).load(R.mipmap.test).into(testa_iv);
    }
}