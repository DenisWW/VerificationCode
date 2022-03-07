package com.jzsec.web.apptest.uikit;

import android.view.ViewGroup;

public interface ViewHolderCreateHelper<T extends BaseViewHolder<B>, B> {
    T createViewHolder(ViewGroup parent);
}
