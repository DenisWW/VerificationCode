package com.nineone.verificationcode.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.nineone.verificationcode.R;
import com.nineone.verificationcode.activity.MineActivity;
import com.nineone.verificationcode.view.MineViewGroup;

import java.util.ArrayList;
import java.util.List;

public class TestFragment extends Fragment {
    private int position;
    private List<String> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.simple_fragment_layout, null);
        position = getArguments().getInt("position", 0);
        TextView test_tv = view.findViewById(R.id.test_tv);
        test_tv.setText(String.valueOf(position));
        ViewPager vp = view.findViewById(R.id.vp);
        MineViewGroup mineViewGroup = view.findViewById(R.id.mine_vg);
        MineActivity.Adapter adapter = new MineActivity.Adapter(getChildFragmentManager());
        vp.setAdapter(adapter);

        List<Fragment> fragments = new ArrayList<>();
        List<String> names = new ArrayList<>();
        Bundle bundle;
        for (int i = 0; i < 5; i++) {
            bundle = new Bundle();
            bundle.putInt("position", i);
            TestFragment1 fragment = new TestFragment1();
            fragment.setArguments(bundle);
            names.add("标题" + i);
            fragments.add(fragment);

        }
        Log.e("fragments", "===" + fragments);
        Log.e("names", "===" + names);

        adapter.setFragments(fragments, names);
        vp.setCurrentItem(0);
//        mine_vg.buildBottomView(new MineViewGroup.BottomViewBuilder().setLeftMargin(10).setRightMargin(5).build());
        mineViewGroup.attachViewPager(vp);
        return view;
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add("测试数据   条目   ====" + i);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(new SimpleAdapter());
    }


    private class SimpleAdapter extends RecyclerView.Adapter<SimpleViewHolder> {


        @NonNull
        @Override
        public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView view = new TextView(parent.getContext());
            return new SimpleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
            holder.textView.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView;
            textView.setPadding(100, 100, 100, 100);
        }
    }
}
