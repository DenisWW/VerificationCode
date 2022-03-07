package com.nineone.verificationcode.uikit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder<B> extends RecyclerView.ViewHolder {

    protected BaseViewHolder(View view) {
        super(view);
        initView();
    }

    protected BaseViewHolder(@LayoutRes int layoutResId, ViewGroup viewGroup) {
        super(LayoutInflater.from(viewGroup.getContext()).inflate(layoutResId, null));
        initView();
    }

    protected BaseViewHolder(@LayoutRes int layoutResId, ViewGroup viewGroup, boolean attachToRoot) {
        super(LayoutInflater.from(viewGroup.getContext()).inflate(layoutResId, viewGroup, attachToRoot));
        initView();
    }

    protected abstract void initView();

    protected void parseBeanError(String err) {
        //do something
    }

    protected abstract void bindViewHolder(int pos, B bean);

    @SuppressWarnings("unchecked")
    void bindViewHolder(Object bean, int position) {
        try {
            if (bean != null) bindViewHolder(position, (B) bean);
        } catch (Exception e) {
            parseBeanError(e.getMessage());
            e.printStackTrace();
        }
    }

}
