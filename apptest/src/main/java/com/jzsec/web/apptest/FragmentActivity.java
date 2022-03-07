package com.jzsec.web.apptest;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jzsec.web.apptest.adapter.FragmentAdapter;
import com.jzsec.web.apptest.dialog.SimpleDialog;
import com.jzsec.web.apptest.fragment.FirstFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FragmentActivity extends androidx.fragment.app.FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        ViewPager view_pager = findViewById(R.id.view_pager);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        view_pager.setAdapter(adapter);
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            FirstFragment fragment = new FirstFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", i);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        adapter.setFragments(fragments);
        findViewById(R.id.title_tv).setOnClickListener(v -> {
            new SimpleDialog(v.getContext()).show();
//
//            TextView textview = new TextView(v.getContext());
//            textview.setWidth(200);
//            textview.setHeight(200);
//            textview.setText("12345677990-");
//
//            PopupWindow popupWindow = new PopupWindow(textview, LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//            popupWindow.setOutsideTouchable(true);
//            popupWindow.showAsDropDown(v, Gravity.CENTER, 100, 200);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(getClass().getSimpleName(),"====onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();       Log.e(getClass().getSimpleName(),"====onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(getClass().getSimpleName(),"====onResume");
    }
}