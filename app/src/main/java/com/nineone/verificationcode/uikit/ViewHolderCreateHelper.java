package com.nineone.verificationcode.uikit;

import android.view.ViewGroup;

public interface ViewHolderCreateHelper<T extends BaseViewHolder<B>, B> {
    T createViewHolder(ViewGroup parent);
}
