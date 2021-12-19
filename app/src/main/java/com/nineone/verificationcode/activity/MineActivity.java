package com.nineone.verificationcode.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;

import com.nineone.verificationcode.R;
import com.nineone.verificationcode.fragment.TestFragment;
import com.nineone.verificationcode.view.MineViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MineActivity extends FragmentActivity {
    private MineViewGroup mine_vg;
    private ViewPager vp;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        init();
    }

    private class MyHandler extends Handler {
        @Override
        public void dispatchMessage(@NonNull Message msg) {
            super.dispatchMessage(msg);

        }
    }

    private void init() {

        mine_vg = findViewById(R.id.mine_vg);
        vp = findViewById(R.id.vp);
        adapter = new Adapter(getSupportFragmentManager());
        vp.setAdapter(adapter);

        List<Fragment> fragments = new ArrayList<>();
        List<String> names = new ArrayList<>();
        Bundle bundle;
        for (int i = 0; i < 5; i++) {
            bundle = new Bundle();
            bundle.putInt("position", i);
            TestFragment fragment = new TestFragment();
            fragment.setArguments(bundle);
            names.add("标题" + i);
            fragments.add(fragment);

        }
        Log.e("fragments", "===" + fragments);
        Log.e("names", "===" + names);

        adapter.setFragments(fragments, names);
        vp.setCurrentItem(0);
//        mine_vg.buildBottomView(new MineViewGroup.BottomViewBuilder().setLeftMargin(10).setRightMargin(5).build());
        mine_vg.attachViewPager(vp);


    }

    public  class Adapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;
        private List<String> names;

        public Adapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        public void setFragments(List<Fragment> fragments, List<String> names) {
            if (fragments.size() != names.size()) {
                return;
            }
            this.fragments = fragments;
            this.names = names;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return names.get(position);
        }

        @Override
        public int getCount() {
            return fragments != null ? fragments.size() : 0;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }
}