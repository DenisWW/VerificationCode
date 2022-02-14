package com.nineone.verificationcode.uikit;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder<B> extends RecyclerView.ViewHolder {
    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
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
