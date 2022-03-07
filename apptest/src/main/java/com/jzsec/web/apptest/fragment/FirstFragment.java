package com.jzsec.web.apptest.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.jzsec.web.apptest.R;
import com.jzsec.web.apptest.adapter.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_fragment_layout, null);
        int j = getArguments().getInt("position");
        j += 1;
        view.setBackgroundColor(Color.rgb(j * 40, j * 40, j * 40));
        ViewPager view_pager = view.findViewById(R.id.view_pager);
        FragmentAdapter adapter = new FragmentAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        view_pager.setAdapter(adapter);
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            SecondFragment fragment = new SecondFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", i);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        adapter.setFragments(fragments);
        return view;
    }
}
