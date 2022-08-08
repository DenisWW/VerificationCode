package com.jzsec.web.apptest.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewParentCompat;

public class MineBehavior extends CoordinatorLayout.Behavior<View> {

    public MineBehavior() {
    }

    public MineBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
//        Log.e(this + "", "   layoutDependsOn===" + child
//                + "     MeasuredHeight=" + dependency.getMeasuredHeight()
//                + "     height=" + dependency.getHeight()
//        );
        return dependency instanceof LinearLayout;
    }


    private int totalTop;

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
//        Log.e(this + "", "   onDependentViewChanged===" + child + "      " + dependency);
        child.offsetTopAndBottom(totalTop = dependency.getHeight());
        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public void onNestedScrollAccepted(
            @NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target,
            int axes, int type) {
//        Log.e(this + "", "   onNestedScrollAccepted===" + child + "   " + target);
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, axes, type);

    }

    @Override
    public boolean onStartNestedScroll(
            @NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target,
            int axes, int type) {
//        Log.e(this + " ", "   onStartNestedScroll===" + coordinatorLayout.getChildAt(0).getClass().getSimpleName()
//                + "    " + target.getClass().getSimpleName() + "   " + directTargetChild.getClass().getSimpleName()+"   "+type);
//        if (axes == ViewCompat.SCROLL_AXIS_VERTICAL) {
//            return true;
//        }
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
//        Log.e(this + "", "   onNestedScroll===" + "   " + "   dyConsumed=" + dyConsumed + "   dyUnconsumed= " + dyUnconsumed);
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target,
                                  int dx, int dy, @NonNull int[] consumed, int type) {
        if ((child.getTop() > 0 && dy > 0) || (child.getTop() < totalTop && dy < 0)) {
            if (child.getTop() - dy >= 0 && child.getTop() - dy <= totalTop) {
                consumed[1] = dy;
//                dy = 0;
            } else {
                if (dy < 0) {
                    consumed[1] = -(totalTop - child.getTop());
//                    dy = dy - consumed[1];
                } else {
                    consumed[1] = child.getTop();
//                    dy = dy + consumed[1];
                }
            }
            ViewCompat.offsetTopAndBottom(child, -consumed[1]);
//            child.offsetTopAndBottom(-consumed[1]);
//            Log.e(this + "", "   onNestedPreScroll2===" + child.getTop() + "   dy=" + dy + "     " + consumed[1]);
        }

        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
    }

    @Override
    public boolean onMeasureChild(@NonNull CoordinatorLayout parent, @NonNull View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }
}
