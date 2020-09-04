package com.nineone.verificationcode.activity;

import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by xmuSistone on 2017/9/20.
 */
public class VegaLayoutManager extends RecyclerView.LayoutManager {

    private int scroll = 0;
    private SparseArray<Rect> locationRects = new SparseArray<>();
    private SparseArray<Rect> locationRectExpand = new SparseArray<>();
    private SparseBooleanArray attachedItems = new SparseBooleanArray();
    @SuppressLint("NewApi")
    private ArrayMap<Integer, Integer> viewTypeHeightMap = new ArrayMap<>();

    private boolean needSnap = false;
    private int lastDy = 0;
    private int maxScroll = -1;
    private RecyclerView.Adapter adapter;
    private RecyclerView.Recycler recycler;
    private boolean isExpand = false;

    public VegaLayoutManager() {
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return true;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onAdapterChanged(RecyclerView.Adapter oldAdapter, RecyclerView.Adapter newAdapter) {
        super.onAdapterChanged(oldAdapter, newAdapter);
        this.adapter = newAdapter;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.recycler = recycler; // 二话不说，先把recycler保存了
        if (state.isPreLayout()) {
            return;
        }
        buildLocationRects();
        // 先回收放到缓存，后面会再次统一layout
        detachAndScrapAttachedViews(recycler);
        layoutItemsOnCreate(recycler);
    }

    private void buildLocationRects() {
        locationRects.clear();
        locationRectExpand.clear();
        attachedItems.clear();
        int tempPosition = getPaddingTop();
        int itemCount = getItemCount();
        for (int i = 0; i < itemCount; i++) {
            // 1. 先计算出itemWidth和itemHeight
            int viewType = adapter.getItemViewType(i);
            int itemHeight;
            if (viewTypeHeightMap.containsKey(viewType)) {
                itemHeight = viewTypeHeightMap.get(viewType);
            } else {
                View itemView = recycler.getViewForPosition(i);
                addView(itemView);
                measureChildWithMargins(itemView, View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//                itemHeight = getDecoratedMeasuredHeight(itemView);
                itemHeight = getHeight() / 5 + 100;
                viewTypeHeightMap.put(viewType, itemHeight);
            }
            // 2. 组装Rect并保存
            Rect rect = new Rect();
            rect.left = getPaddingLeft();
            rect.top = tempPosition;
            rect.right = getWidth() - getPaddingRight();
            rect.bottom = rect.top + itemHeight;
            locationRects.put(i, rect);
            attachedItems.put(i, false);
            tempPosition = tempPosition + itemHeight - 100;
//            Rect rect1 = new Rect();
//
//            rect1.left = getPaddingLeft();
//            rect1.right = getWidth() - getPaddingRight();
//            rect1.top

        }
        if (itemCount == 0) {
            maxScroll = 0;
        } else {
            computeMaxScroll();
        }
    }

    /**
     * 对外提供接口，找到第一个可视view的index
     */
    public int findFirstVisibleItemPosition() {
        int count = locationRects.size();
        Rect displayRect = new Rect(0, scroll, getWidth(), getHeight() + scroll);
        for (int i = 0; i < count; i++) {
            if (Rect.intersects(displayRect, locationRects.get(i)) &&
                    attachedItems.get(i)) {
                return i;
            }
        }
        return 0;
    }

    /**
     * 计算可滑动的最大值
     */
    private void computeMaxScroll() {
        maxScroll = locationRects.get(locationRects.size() - 1).bottom - getHeight();
        if (maxScroll < 0) {
            maxScroll = 0;
            return;
        }
        int itemCount = getItemCount();
        int screenFilledHeight = 0;
        for (int i = itemCount - 1; i >= 0; i--) {
            Rect rect = locationRects.get(i);
            screenFilledHeight = screenFilledHeight + (rect.bottom - rect.top);
            if (screenFilledHeight > getHeight()) {
                int extraSnapHeight = getHeight() - (screenFilledHeight - (rect.bottom - rect.top));
                maxScroll = maxScroll + extraSnapHeight;
                break;
            }
        }
        maxScroll = locationRects.get(locationRects.size() - 1).bottom - getHeight() - 100;
    }

    int i;

    /**
     * 初始化的时候，layout子View
     */
    private void layoutItemsOnCreate(RecyclerView.Recycler recycler) {

        if (position != -1) {
            scroll = locationRects.get(position).top;
            position = -1;
        }

        int itemCount = getItemCount();
        Rect displayRect = new Rect(0, scroll + 100, getWidth(), getHeight() + scroll + 100);

        for (int i = 0; i < itemCount; i++) {
            Rect thisRect = locationRects.get(i);
            if (Rect.intersects(displayRect, thisRect)) {
                View childView = recycler.getViewForPosition(i);
                addView(childView);
                measureChildWithMargins(childView, View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                layoutItem(childView, locationRects.get(i));
                attachedItems.put(i, true);
                childView.setPivotY(0);
                childView.setPivotX(childView.getMeasuredWidth() / 2);
                if (thisRect.top - scroll > getHeight()) {
                    break;
                }
            }
        }
    }

    @Override
    public void attachView(@NonNull View child) {
        super.attachView(child);

    }

    public void startAnimator(boolean isExpand, int startTop) {
        this.isExpand = isExpand;
        AnimatorSet animatorSet = new AnimatorSet();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view != null) {
                setAnimatorSet(animatorSet, view, locationRects.get(findFirstVisibleItemPosition() + i), getChildCount() - i - 1, startTop);
            }
        }
        animatorSet.start();
    }


    private void setAnimatorSet(AnimatorSet animatorSet, View views, Rect target, int i, int startTop) {
        if (views == getChildAt(0)) {
            ValueAnimator animator = ObjectAnimator.ofFloat(views, "alpha", 0f, 1f);
            animator.addUpdateListener(animation -> {
            });
            animator.setDuration(800);
//            animatorSet.play(animator).after(50 * i);
            animatorSet.play(animator);
        } else {
            ValueAnimator animator = ObjectAnimator.ofInt(locationRects.get(findFirstVisibleItemPosition()).top + startTop, target.top);
            animator.setInterpolator(new OvershootInterpolator(1.1f));
            int height = target.height();
            animator.addUpdateListener(animation -> {
                int value = (Integer) animation.getAnimatedValue();
                Rect f = new Rect(0, value, getWidth(), value + height);
//            layoutDecorated(views, f.left, f.top, f.right, f.bottom);
                layoutItem(views, f);
                if (listener != null && views == getChildAt(getChildCount() - 1)) {
//                    listener.onListener(value + height - scroll - 100);
                    listener.onListener((int) (getHeight() * animation.getAnimatedFraction()));
                }
            });
            animator.setDuration(800);
//            animatorSet.play(animator).after(i*50);
            animatorSet.play(animator);
        }
    }

    public void stopAnimator(boolean isExpand, int startTop, AnimatorListenerAdapter animatorListenerAdapter) {
        this.isExpand = isExpand;
        AnimatorSet animatorSet = new AnimatorSet();
        int j = 0;
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View view = getChildAt(i);
            int pos = findFirstVisibleItemPosition() + i;
            Rect rect = locationRects.get(pos);
            int center = rect.top + rect.height() / 2;
            Log.e("==", "findFirstVisibleItemPosition===" + locationRects.get(findFirstVisibleItemPosition() + i)
                    + "   ==" + pos
                    + "   ==" + scroll
                    + "   ==" + center
            );
            if (view != null && center < scroll + getHeight()) {
                setAnimatorSetOut(animatorSet, view, rect, j, startTop, j, pos);
                j++;
            }
        }
        animatorSet.addListener(animatorListenerAdapter);
        animatorSet.start();
    }

    private void setAnimatorSetOut(AnimatorSet animatorSet, View views, Rect target, int i, int startTop, int j, int pos) {
        if (views == getChildAt(0)) {
            ValueAnimator animator = ObjectAnimator.ofFloat(views, "alpha", 1f, 0f);
            animator.addUpdateListener(animation -> {
            });
            animator.setDuration(500);
//            animatorSet.play(animator).after(50 * i);
            animatorSet.play(animator);
        } else {
            ValueAnimator animator = ObjectAnimator.ofInt(target.top, j == 1 ? scroll : scroll + startTop);
            int height = target.height();
            animator.addUpdateListener(animation -> {
                int value = (Integer) animation.getAnimatedValue();
                Rect f = new Rect(0, value, getWidth(), value + height);
                if (j >= 1) {
                    views.setScaleX(((1f - animation.getAnimatedFraction()) * 0.2f) + 0.8f);
                }
                layoutItemOut(views, f);
//                layoutDecorated(views, f.left, f.top, f.right, f.bottom);
                if (listener != null && j == 0) {
//                    listener.onListener(value + height - scroll - 100);
                    listener.onOutListener((int) (getHeight() * (1f - animation.getAnimatedFraction())), pos);
                }
            });
            animator.setDuration(500);
            animatorSet.play(animator).after(30 * i);
            animatorSet.play(animator);
        }
    }


    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller linearSmoothScroller =
                new LinearSmoothScroller(recyclerView.getContext());
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }


    public OnHeightListener listener;

    public void cancel() {
        startSnapHelper.attachToRecyclerView(null);
    }

    public interface OnHeightListener {
        void onListener(int height);

        void onOutListener(int height, int pos);
    }

    /**
     * 初始化的时候，layout子View
     */
    private void layoutItemsOnScroll() {
        int childCount = getChildCount();
        int itemCount = getItemCount();
        Rect displayRect = new Rect(0, scroll, getWidth(), getHeight() + scroll);
        int firstVisiblePosition = -1;
        int lastVisiblePosition = -1;
        for (int i = childCount - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (child == null) {
                continue;
            }
            int position = getPosition(child);
            if (!Rect.intersects(displayRect, locationRects.get(position))) {
                // 回收滑出屏幕的View
                removeAndRecycleView(child, recycler);
                attachedItems.put(position, false);
            } else {
                // Item还在显示区域内，更新滑动后Item的位置
                if (lastVisiblePosition < 0) {
                    lastVisiblePosition = position;
                }

                if (firstVisiblePosition < 0) {
                    firstVisiblePosition = position;
                } else {
                    firstVisiblePosition = Math.min(firstVisiblePosition, position);
                }

                layoutItem(child, locationRects.get(position)); //更新Item位置
            }
        }

        // 2. 复用View处理
        if (firstVisiblePosition > 0) {
            // 往前搜索复用
            for (int i = firstVisiblePosition - 1; i >= 0; i--) {
                if (Rect.intersects(displayRect, locationRects.get(i)) &&
                        !attachedItems.get(i)) {
                    reuseItemOnScroll(i, true);
                } else {
                    break;
                }
            }
        }
        // 往后搜索复用
        for (int i = lastVisiblePosition + 1; i < itemCount; i++) {
            if (Rect.intersects(displayRect, locationRects.get(i)) &&
                    !attachedItems.get(i)) {
                reuseItemOnScroll(i, false);
            } else {
                break;
            }
        }
    }

    /**
     * 复用position对应的View
     */
    private void reuseItemOnScroll(int position, boolean addViewFromTop) {
        View scrap = recycler.getViewForPosition(position);
        measureChildWithMargins(scrap, 0, 0);
        scrap.setPivotY(0);
        scrap.setPivotX(scrap.getMeasuredWidth() / 2);

        if (addViewFromTop) {
            addView(scrap, 0);
        } else {
            addView(scrap);
        }
        // 将这个Item布局出来
        layoutItem(scrap, locationRects.get(position));
        attachedItems.put(position, true);
    }


    private void layoutItem(View child, Rect rect) {
        int topDistance = scroll - rect.top;
        int layoutTop, layoutBottom;
        int itemHeight = rect.bottom - rect.top;
        if (topDistance < itemHeight && topDistance > 0) {
            float rate1 = (float) topDistance / itemHeight;
            float rate2 = 1 - rate1 * rate1 / 3;
            float rate3 = 1 - rate1 * rate1;
            child.setScaleX(rate2);
            child.setScaleY(rate2);
//            child.setAlpha(rate3);
            layoutTop = 0;
            layoutBottom = itemHeight;
        } else {
            child.setScaleX(1);
            child.setScaleY(1);
            child.setAlpha(1);
            layoutTop = rect.top - scroll;
            layoutBottom = rect.bottom - scroll;
        }
        layoutDecorated(child, rect.left, layoutTop, rect.right, layoutBottom);
    }

    private void layoutItemOut(View child, Rect rect) {
        int topDistance = scroll - rect.top;
        int layoutTop, layoutBottom;
        int itemHeight = rect.bottom - rect.top;
        if (topDistance < itemHeight && topDistance > 0) {
            float rate1 = (float) topDistance / itemHeight;
            float rate2 = 1 - rate1 * rate1 / 3;
            float rate3 = 1 - rate1 * rate1;
            child.setScaleX(rate2);
            child.setScaleY(rate2);
//            child.setAlpha(rate3);
            layoutTop = 0;
            layoutBottom = itemHeight;
        } else {
            layoutTop = rect.top - scroll;
            layoutBottom = rect.bottom - scroll;
        }
        layoutDecorated(child, rect.left, layoutTop, rect.right, layoutBottom);
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    private int position;

    @Override
    public void scrollToPosition(int position) {
        super.scrollToPosition(position);
        this.position = position;
//        if (this.position != -1 && locationRects.get(position) != null && locationRects.size() > 0) {
//            scroll = locationRects.get(position).top;
//            this.position = -1;
//        }
        requestLayout();
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0 || dy == 0) {
            return 0;
        }
        int travel = dy;
        if (dy + scroll < 0) {
            travel = -scroll;
        } else if (dy + scroll > maxScroll) {
            travel = maxScroll - scroll;
        }
        scroll += travel; //累计偏移量
        lastDy = dy;
        if (!state.isPreLayout() && getChildCount() > 0) {
            layoutItemsOnScroll();
        }
        return travel;
    }

    private ThreeActivity.StartSnapHelper startSnapHelper = new ThreeActivity.StartSnapHelper();

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        startSnapHelper.attachToRecyclerView(view);
//        new StartSnapHelper().attachToRecyclerView(view);


    }

    @Override
    public void onDetachedFromWindow(RecyclerView view, RecyclerView.Recycler recycler) {
        super.onDetachedFromWindow(view, recycler);

    }

    @Override
    public void onScrollStateChanged(int state) {
        if (state == RecyclerView.SCROLL_STATE_DRAGGING) {
            needSnap = true;
        }
        super.onScrollStateChanged(state);
    }

    public int getSnapHeight() {
        if (!needSnap) {
            return 0;
        }
        needSnap = false;

        Rect displayRect = new Rect(0, scroll, getWidth(), getHeight() + scroll);
        int itemCount = getItemCount();
        for (int i = 0; i < itemCount; i++) {
            Rect itemRect = locationRects.get(i);
            if (displayRect.intersect(itemRect)) {
                if (lastDy > 0) {
                    // scroll变大，属于列表往下走，往下找下一个为snapView
                    if (i < itemCount - 1) {
                        Rect nextRect = locationRects.get(i + 1);
                        return nextRect.top - displayRect.top;
                    }
                }
                return itemRect.top - displayRect.top;
            }
        }
        return 0;
    }

    public View findSnapView() {
        if (getChildCount() > 0) {
            return getChildAt(0);
        }
        return null;
    }
}
