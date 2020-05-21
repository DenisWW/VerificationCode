package com.nineone.verificationcode;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.nineone.verificationcode.activity.BesselActivity;
import com.nineone.verificationcode.view.DragImageView;

public class MainActivity extends Activity {
    private SeekBar seekBar;
    private DragImageView mine_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar = findViewById(R.id.seek_bar);
        mine_iv = findViewById(R.id.mine_iv);
        seekBar.setMax(1000);
        mine_iv.getBgImageView().setImageResource(R.mipmap.demo2);
        mine_iv.getProgressImageView().setImageResource(R.mipmap.demo2);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mine_iv.setOffset(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void junm1(View view) {
        Intent intent = new Intent(this, BesselActivity.class);
        startActivity(intent);
    }
}
