package com.nineone.verificationcode.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nineone.verificationcode.R;

import java.util.ArrayList;
import java.util.List;

public class TestFragment2 extends Fragment {
    private int position;
    private RecyclerView recyclerView;
    private List<String> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.simple_fragment_layout1, null);

        TextView test_tv = view.findViewById(R.id.test_tv);
        test_tv.setText(String.valueOf(position));
        recyclerView = view.findViewById(R.id.recyclerView);
        initRecyclerView(recyclerView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        position = getArguments().getInt("position", 0);
        view.findViewById(R.id.back_ground_view).setBackgroundColor(getArguments().getInt("colorKey"));

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
