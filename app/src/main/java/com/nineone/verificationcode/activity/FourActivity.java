package com.nineone.verificationcode.activity;

import static android.net.LocalSocket.SOCKET_DGRAM;

import android.content.Context;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nineone.verificationcode.R;
import com.nineone.verificationcode.bean.Bean;
import com.nineone.verificationcode.fragment.TestFragment2;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FourActivity extends FragmentActivity {
    private RecyclerView recycler;
    private Context context;
    private int height;
    private List<SimpleBean> list = new ArrayList<>();
    private int[] ids = new int[]{R.color.tag_00a0e9, R.color.tag_eea266, R.color.tag_916ed9, R.color.color_f763fb, R.color.color_ff70ae};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);
        for (int i = 0; i < ids.length; i++) {
            TestFragment2 fragment2 = new TestFragment2();
            Bundle bundle = new Bundle();
            bundle.putInt("colorKey", ids[i]);
            fragment2.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_view, fragment2).addToBackStack(String.valueOf(i)).commitAllowingStateLoss();

        }
        int intw = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int inth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        TextView textView = findViewById(R.id.back_bt);
        textView.measure(intw, inth);
        int intwidth = textView.getMeasuredWidth();
        int intheight = textView.getMeasuredHeight();
        Log.e("intwidth", "===" + intwidth + "     intheight==" + intheight);
//        HashSet<String> hashSet = new HashSet<>();
//        hashSet.add("2");
//        hashSet.add("1");
//        hashSet.add("4");
//        hashSet.add("5");
//        Log.e("有序", "======" + hashSet);
//        for (String s : hashSet) {
//            Log.e("有序", "======" + s);
//        }
//        context = this;
//        height = context.getResources().getDisplayMetrics().heightPixels;
//        init();
        try {
            Method method = FileDescriptor.class.getDeclaredMethod("getInt$");
            Bean bean = new Bean(5);
            Field field = Bean.class.getDeclaredField("type");
            field.setAccessible(true);
            Log.e("method", "===" + field.get(bean));

        } catch (NoSuchMethodException | IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
//        View.MeasureSpec.getMode();
//        View.MeasureSpec.getSize()
        Log.e("长度是", "====" + lengthOfLongestSubstring("  "));
//        start();

//       new Thread(){
//           @Override
//           public void run() {
//               super.run();
//               while (true){
//                   LocalSocket localSocket=new LocalSocket();
//                   try {
//                       localSocket.connect(new LocalSocketAddress("wanghaoyu"));
//                   } catch (IOException e) {
//                       throw new RuntimeException(e);
//                   }
//
//               }
//           }
//       }.start();
    }

    public int lengthOfLongestSubstring(String s) {
        char[] chars = s.toCharArray();
        List<Character> characters = new ArrayList<>();
        char first;
        int maxLength = 0;
        for (int i = 0; i < chars.length; i++) {
            first = chars[i];
            characters.clear();
            characters.add(first);
            for (int j = i + 1; j < chars.length; j++) {
                if (characters.contains( chars[j])) {
                    Log.e("characters111","==="+characters.toString());
                    break;
                } else {
                    characters.add(chars[j]);
                }
            }
            Log.e("characters2222","==="+characters.toString());
            if (characters.size() > maxLength) {
                maxLength = characters.size();
            }
        }
        return maxLength;
    }

    public void start() {
        Text text = new Text();
        new Thread("name1==") {
            @Override
            public void run() {
                super.run();
                while (true) {
                    text.get();
                }

            }
        }.start();
        new Thread("name2==") {
            @Override
            public void run() {
                super.run();
                while (true) {
                    text.save();
                }


            }
        }.start();
    }

    private class Text {
        private boolean flag = false;

        public synchronized void save() {
            try {
                if (flag) {
                    Log.e("存钱", "======1");
                    wait();
                } else {
                    Log.e("存钱", "======2");
                    flag = true;
                    notifyAll();
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public synchronized void get() {
            try {
                if (!flag) {
                    Log.e("取钱", "======1");
                    wait();
                }
                Log.e("取钱", "======2");
                flag = false;
                notifyAll();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }


    }

    private void init() {
        for (int i = 0; i < 20; i++) {
            SimpleBean simpleBean = new SimpleBean();
            simpleBean.name = "  Test:" + i;
            simpleBean.resId = ids[i % ids.length];
            list.add(simpleBean);

        }
        recycler = findViewById(R.id.recycler);
        recycler.setAdapter(new SimpleAdapter());
        recycler.setLayoutManager(new MyManager(context));
        Log.e("SimpleAdapter", "===" + ((10 & 15)));
    }

    private class MyManager extends LinearLayoutManager {

        public MyManager(Context context) {
            super(context);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            super.onLayoutChildren(recycler, state);
            Log.e("onLayoutChildren", "====" + state.isPreLayout() + "   " + getChildCount());
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                Log.e("onLayoutChildren", "===" + view + "   " + view.getTop());
            }

        }

        @Override
        public void addView(View child) {
            super.addView(child);
            Log.e("addView", "====" + "   " + child.getTop());
        }

        @Override
        public void addView(View child, int index) {
            super.addView(child, index);
            Log.e("addView", "====" + "   " + child.getTop() + " index=" + index);
        }

        @Override
        public void onLayoutCompleted(RecyclerView.State state) {
            super.onLayoutCompleted(state);
            Log.e("onLayoutCompleted", "====" + "   " + state.toString());
        }
    }

    private class SimpleAdapter extends RecyclerView.Adapter<SimpleViewHolder> {

        @NonNull
        @Override
        public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ImageView textView = new ImageView(context);
            return new SimpleViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
//            Glide.with(context).load(list.get(position).resId).transform(new CenterCrop(), new RoundedCorners(25)).into(holder.imageView);
            holder.imageView.setImageResource(list.get(position).resId);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


    }

    private class SimpleViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView;
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (height / 5) + 100));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    private class SimpleBean {
        @RawRes
        @DrawableRes
        @ColorRes
        int resId;

        String name;


    }
}