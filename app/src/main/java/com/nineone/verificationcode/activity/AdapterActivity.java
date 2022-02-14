package com.nineone.verificationcode.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineone.verificationcode.R;
import com.nineone.verificationcode.adapter.LeftViewHolder;
import com.nineone.verificationcode.adapter.WViewHolder;
import com.nineone.verificationcode.adapter.WViewHolder1;
import com.nineone.verificationcode.uikit.MultiTypeAdapter;
import com.nineone.verificationcode.uikit.ViewHolderBinderPool;
import com.nineone.verificationcode.uikit.ViewHolderCreateHelper;
import com.nineone.verificationcode.view.LeftRecyclerView;
import com.nineone.verificationcode.view.RightRecycleView;
import com.nineone.verificationcode.view.ScrollerTableLayout;

import java.util.ArrayList;
import java.util.List;

public class AdapterActivity extends AppCompatActivity {


    private LeftRecyclerView leftRecyclerView;
    private RightRecycleView rightRecyclerView;

    private ScrollerTableLayout scrollerTableLayout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adapter);
        scrollerTableLayout = findViewById(R.id.scroll_table);
        rightRecyclerView = scrollerTableLayout.getRightRecycleView();
        leftRecyclerView = scrollerTableLayout.getLeftRecyclerView();

        LinearLayoutManager leftLayoutManager;
        leftRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rightRecyclerView.setLayoutManager(leftLayoutManager = new LinearLayoutManager(this));
        rightRecyclerView.setView(leftRecyclerView);
        MultiTypeAdapter rightAdapter;
        MultiTypeAdapter leftAdapter;
        List<Object> leftList = new ArrayList<>();
        List<Object> rightList = new ArrayList<>();
        rightRecyclerView.setAdapter(rightAdapter = new MultiTypeAdapter(new ViewHolderBinderPool()
                .addViewHolderCreateHelper(String.class, new ViewHolderCreateHelper<WViewHolder, String>() {
                    @Override
                    public WViewHolder createViewHolder(ViewGroup parent) {
                        return new WViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_right_layout, null));
                    }
                })
                .addViewHolderCreateHelper(Integer.class, new ViewHolderCreateHelper<WViewHolder1, Integer>() {
                    @Override
                    public WViewHolder1 createViewHolder(ViewGroup parent) {
                        return new WViewHolder1(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_right_layout, null));
                    }
                })
                , rightList));

        leftRecyclerView.setAdapter(leftAdapter
                = new MultiTypeAdapter(new ViewHolderBinderPool()
                .addViewHolderCreateHelper(String.class, new ViewHolderCreateHelper<LeftViewHolder, String>() {
                    @Override
                    public LeftViewHolder createViewHolder(ViewGroup viewGroup) {
                        return new LeftViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_left_layout, viewGroup, false));
                    }
                })
                , leftList));

        List<Object> right = new ArrayList<>();
        List<Object> left = new ArrayList<>();
        for (int i = 0; i < 120; i++) right.add(i % 2 == 1 ? "测试使用===" + i : i);
        for (int i = 0; i < 120; i++) left.add("左条目===" + i);

        setData(leftAdapter, leftList, left);
        setData(rightAdapter, rightList, right);


        TextView textView = new TextView(this);
        textView.setText("测试标题");
        scrollerTableLayout.setRightTitle(textView);

        TextView textView1 = new TextView(this);
        textView1.setText("左边标题");
        scrollerTableLayout.setLeftTitle(textView1);


        findViewById(R.id.top_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                clearData(leftAdapter, list);
            }
        });
    }

    public void addData(RecyclerView.Adapter<?> adapter, List<Object> list, List<?> res) {
        if (res == null) return;
        if (list == null) list = new ArrayList<>();
        int start = list.size();
        list.addAll(res);
        adapter.notifyItemRangeChanged(start, list.size());
    }

    public void setData(RecyclerView.Adapter<?> adapter, List<Object> list, List<?> res) {
        if (res == null) return;
        if (list == null) list = new ArrayList<>();
        else list.clear();
        list.addAll(res);
        adapter.notifyItemRangeChanged(0, list.size());
    }

    public void clearData(RecyclerView.Adapter<?> adapter, List<Object> list) {
        if (list != null && !list.isEmpty()) {
            int end = list.size();
            list.clear();
            adapter.notifyItemRangeRemoved(0, end);
        }
    }
}