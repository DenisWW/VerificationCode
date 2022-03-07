package com.jzsec.web.apptest.fragment;

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

import com.jzsec.web.apptest.R;
import com.jzsec.web.apptest.uikit.BaseViewHolder;
import com.jzsec.web.apptest.uikit.MultiTypeAdapter;
import com.jzsec.web.apptest.uikit.ViewHolderBinderPool;
import com.jzsec.web.apptest.uikit.ViewHolderCreateHelper;

import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.second_fragment_layout, null);
        RecyclerView recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<String> list = new ArrayList<>();
        recyclerView.setAdapter(new MultiTypeAdapter(new ViewHolderBinderPool().addViewHolderCreateHelper(String.class, new ViewHolderCreateHelper<BaseViewHolder<String>, String>() {
            @Override
            public BaseViewHolder<String> createViewHolder(ViewGroup parent) {
                return new StringViewHolder(new TextView(parent.getContext()));
            }
        }), list));

        for (int i = 0; i < 50; i++) {
            list.add(String.valueOf(i));
        }
        return view;
    }

    public static class StringViewHolder extends BaseViewHolder<String> {

        protected StringViewHolder(View view) {
            super(view);
        }

        @Override
        protected void initView() {

        }

        @Override
        protected void bindViewHolder(int pos, String bean) {
            ((TextView) itemView).setText(bean);
        }
    }
}
