package com.nineone.verificationcode.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.nineone.verificationcode.R;
import com.nineone.verificationcode.uikit.BaseViewHolder;

public class LeftViewHolder extends BaseViewHolder<String> {


    private TextView textView;
    private String bean;

    public LeftViewHolder(@NonNull ViewGroup itemView) {
        super(R.layout.item_left_layout, itemView,false);

    }

    @Override
    protected void initView() {
        textView = (TextView) itemView.findViewById(R.id.tv_title);
//        textView.setOnClickListener(v -> {
//            if (!TextUtils.isEmpty(String.valueOf(bean)))
//                Toast.makeText(v.getContext(), "点击了" + bean, Toast.LENGTH_SHORT).show();
//        });
        textView.setWidth(200);
    }

    @Override
    protected void parseBeanError(String err) {
        super.parseBeanError(err);
    }

    @Override
    protected void bindViewHolder(int pos, String bean) {
        this.bean = bean;
        textView.setText(bean + "      ");
        textView.setMaxLines(1);
    }

}
