package com.jzsec.web.apptest.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class MineCoordinatorLayout extends CoordinatorLayout {
    public MineCoordinatorLayout(@NonNull Context context) {
        super(context);
    }

    public MineCoordinatorLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MineCoordinatorLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes, int type) {
        super.onNestedScrollAccepted(child, target, nestedScrollAxes, type);
//        Log.e(getClass().getSimpleName(), "   onNestedScrollAccepted1===");
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int axes, int type) {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View view = getChildAt(i);
//            Log.e(getClass().getSimpleName(), "====" + view);
        }

        return super.onStartNestedScroll(child, target, axes, type);
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        super.onNestedScrollAccepted(child, target, nestedScrollAxes);
//        Log.e(getClass().getSimpleName(), "   onNestedScrollAccepted2===");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
//        Log.e(getClass().getSimpleName(), "   onLayout===" + l);
    }

    @Override
    public void onLayoutChild(@NonNull View child, int layoutDirection) {
        super.onLayoutChild(child, layoutDirection);
//        Log.e(getClass().getSimpleName(), "   onLayoutChild===" + child);
    }


}
