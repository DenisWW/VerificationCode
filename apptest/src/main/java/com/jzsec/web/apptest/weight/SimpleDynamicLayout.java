package com.jzsec.web.apptest.weight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class SimpleDynamicLayout extends ViewGroup {
    private List<List<View>> views;
    private List<Integer> height;

    public SimpleDynamicLayout(Context context) {
        this(context, null);
    }

    public SimpleDynamicLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        views = new ArrayList<>();
        height = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        views.clear();
        height.clear();
//        int specWidthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int specHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int specHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        measureChildren(specWidthSize, specHeightSize);
        @SuppressLint("DrawAllocation") List<View> lineViews = new ArrayList<>();
        int count = getChildCount(), h = 0, lineWidth = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) child.getLayoutParams();
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            int childW = child.getMeasuredWidth() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
            int childH = child.getMeasuredHeight() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
            if (lineWidth + childW > specWidthSize) {
                lineWidth = childW;
                height.add(h);
                h = childH;
                views.add(lineViews);
                lineViews = new ArrayList<>();
            } else {
                h = Math.max(h, childH);
                lineWidth += childW;
            }
            lineViews.add(child);
        }
        height.add(h);
        views.add(lineViews);
        int lineHeight = 0;
        for (Integer integer : height) lineHeight += integer;
        setMeasuredDimension(specWidthSize, lineHeight);
//        setMeasuredDimension(specWidthMode == MeasureSpec.EXACTLY ? specWidthSize : widthMeasureSpec, specHeightMode == MeasureSpec.EXACTLY ? specHeightSize : lineHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int lineHeight, left, top = 0;
        List<View> list;
        MarginLayoutParams params;
        for (int i = 0; i < views.size(); i++) {
            list = views.get(i);
            left = 0;
            lineHeight = height.get(i);
            for (int j = 0; j < list.size(); j++) {
                View child = list.get(j);
                if (child.getVisibility() == GONE) continue;
                params = (MarginLayoutParams) child.getLayoutParams();
                int lf = left + params.leftMargin;
                int lt = top + params.topMargin;
                int right = lf + child.getMeasuredWidth();
                int bottom = lt + child.getMeasuredHeight();
                child.layout(lf, lt, right, bottom);
                left += child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            }
            top += lineHeight;
        }
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(this.getLayoutParams());
    }

}
