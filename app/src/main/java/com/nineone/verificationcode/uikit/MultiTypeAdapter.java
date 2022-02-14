package com.nineone.verificationcode.uikit;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MultiTypeAdapter extends RecyclerView.Adapter<BaseViewHolder<?>> {

    @NonNull
    private final List<?> items;
    private final ViewHolderBinderPool binder;

    public MultiTypeAdapter(ViewHolderBinderPool binder, @NonNull List<?> items) {
        this.items = items;
        this.binder = binder;
    }

    @NonNull
    @Override
    public final BaseViewHolder<?> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return binder.getBaseViewHolder(viewType).createViewHolder(parent);
    }

    @Override
    public final void onBindViewHolder(@NonNull BaseViewHolder<?> holder, int position) {
        holder.bindViewHolder(items.get(position), position);
    }

    @Override
    public final int getItemViewType(int position) {
        return binder.getViewType(items.get(position).getClass());
    }

    @Override
    public final int getItemCount() {
        return items.size();
    }
}
