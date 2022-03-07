package com.nineone.verificationcode.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.nineone.verificationcode.R;
import com.nineone.verificationcode.adapter.LeftViewHolder;
import com.nineone.verificationcode.adapter.WViewHolder;
import com.nineone.verificationcode.adapter.WViewHolder1;
import com.nineone.verificationcode.bean.Book;
import com.nineone.verificationcode.bean.UserBean;
import com.nineone.verificationcode.uikit.MultiTypeAdapter;
import com.nineone.verificationcode.uikit.ViewHolderBinderPool;
import com.nineone.verificationcode.utils.CenterAlignImageSpan;
import com.nineone.verificationcode.view.LeftRecyclerView;
import com.nineone.verificationcode.view.RightRecycleView;
import com.nineone.verificationcode.view.ScrollerTableLayout;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AdapterActivity extends AppCompatActivity {


    private LeftRecyclerView leftRecyclerView;
    private RightRecycleView rightRecyclerView;

    private ScrollerTableLayout scrollerTableLayout;
    private TextView textView;

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
                .addViewHolderCreateHelper(String.class, WViewHolder::new)
                .addViewHolderCreateHelper(Integer.class, WViewHolder1::new), rightList));

        leftRecyclerView.setAdapter(leftAdapter
                = new MultiTypeAdapter(new ViewHolderBinderPool()
                .addViewHolderCreateHelper(String.class, LeftViewHolder::new), leftList));

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
                try {
                    Object object = UserBean.class.newInstance();
                    Field f = UserBean.class.getDeclaredField("data");
                    Type genericType = f.getGenericType();
                    ParameterizedType parameterizedType = (ParameterizedType) genericType;
                    Class<?> c = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                    Object o = c.newInstance();
                    Field field = c.getDeclaredField("serialPersistentFields");
                    field.setAccessible(true);
                    field.set(o, 12);
                    Log.e("Field", genericType + "====" + parameterizedType.getActualTypeArguments()[0] + "   == " + o.getClass()
                            + "   " + o
                    );
                    for (Field f2 : c.getDeclaredFields()) {
                        Log.e("f2===", "====" + f2);
                    }
                } catch (IllegalAccessException | InstantiationException | NoSuchFieldException e) {
                    e.printStackTrace();
                }

//                clearData(leftAdapter, list);
            }
        });

        textView = findViewById(R.id.top_bt);
        SpannableStringBuilder spannable = new SpannableStringBuilder(" 沪a萨达啥今天亏打发了是啊哈哈哈哈");
        // 设置边框
//        spannable.setSpan(new DrawableMarginSpan( getResources().getDrawable(R.drawable.stroke_eb4a38_1)), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CenterAlignImageSpan(this, R.mipmap.label_ke), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannable);

        String json = "{\"name\":null}";
        Book book = new Gson().fromJson(json, Book.class);
        Log.e("book", "====" + book.name);
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