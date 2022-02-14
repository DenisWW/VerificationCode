package com.nineone.verificationcode.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;

import com.nineone.verificationcode.uikit.BaseViewHolder;

public abstract class BaseViewHoldHelper<H extends BaseViewHolder<?>> {

    @LayoutRes
    private Integer resLayoutId;

    public BaseViewHoldHelper(Integer resLayoutId) {
        this.resLayoutId = resLayoutId;
    }

    @LayoutRes
    protected Integer getResLayoutId() {
        return resLayoutId;
    }


    protected View getItemView(ViewGroup viewGroup) {
        return (getResLayoutId() != null && getResLayoutId() != 0) ? inflaterLayout(viewGroup, getResLayoutId()) :
                        new View(viewGroup.getContext());
    }

    public abstract H createViewHolder(ViewGroup viewGroup);

    public View inflaterLayout(ViewGroup parent, @LayoutRes Integer resLayoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(resLayoutId, parent, false);
    }

}
