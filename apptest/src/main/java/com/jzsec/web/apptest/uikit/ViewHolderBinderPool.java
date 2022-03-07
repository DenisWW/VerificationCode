package com.jzsec.web.apptest.uikit;


import androidx.collection.SimpleArrayMap;

public class ViewHolderBinderPool {

    private final SimpleArrayMap<Class<?>, ViewHolderCreateHelper<? extends BaseViewHolder<?>, ?>> helperMap;

    public ViewHolderBinderPool() {
        helperMap = new SimpleArrayMap<>();
    }

    public <B> ViewHolderBinderPool addViewHolderCreateHelper(Class<B> c, ViewHolderCreateHelper<? extends BaseViewHolder<B>, B> holdHelper) {
        helperMap.put(c, holdHelper);
        return this;
    }

    int getViewType(Class<?> clazz) {
        int type = helperMap.indexOfKey(clazz);
        if (type == -1) {
            throw new RuntimeException("数据与item条目不匹配,请检查 " + clazz.getName() + "  数据类型是否设置对应的ViewHolder");
        }
        return helperMap.indexOfKey(clazz);
    }

    ViewHolderCreateHelper<? extends BaseViewHolder<?>, ?> getBaseViewHolder(int index) {
        return helperMap.get(helperMap.keyAt(index));
    }
}
