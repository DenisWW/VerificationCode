package com.nineone.verificationcode.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nineone.verificationcode.R;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

public class ParentViewGroup extends FrameLayout implements NestedScrollingParent {
    int mTouchSlop;
    private int maxLeft;
    private RecyclerView recycler;
    private VelocityTracker mVelocityTracker;
    private boolean isOpen;

    private int targetViewId;

    public ParentViewGroup(Context context) {
        super(context);
        init(context);
    }

    public ParentViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ParentViewGroup);
        targetViewId = typedArray.getInteger(R.styleable.ParentViewGroup_targetViewId, 0);
        Log.e("targetViewId", "======" + targetViewId);
        typedArray.recycle();
        init(context);
    }

    private boolean isFirst;
    private int mMaximumVelocity;
    private int mMinimumVelocity;

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
//        if (changed) {
        maxLeft = r;
//            capturedChild.layout(r, 0, r * 2, b);
//            capturedChild.setTranslationX(r);
//        }

        if (view == null) {
            view = new View(getContext());
            view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.black));
            Log.e("getChildCount()", "===" + getChildCount());
            view.setAlpha(0f);
            addView(view, 1);
            view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        getChildMeasureSpec()
    }

    private View view;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        capturedChild = findViewById(R.id.right_rl);
        leftChild = findViewById(R.id.child_view);
        maxLeft = w;
        capturedChild.setTranslationX(maxLeft);
//        if (view != null) {
//            ViewGroup viewParent = (ViewGroup) view.getParent();
//            if (viewParent != null) {
//                viewParent.removeView(view);
//            }
//            addView(view, 1);
//        }
//        Log.e("onSizeChanged", "-=====" + view.getWidth() + "     ==" + view.getHeight());
    }


    private void init(Context context) {
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mMaximumVelocity = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
//        scroller = new OverScroller(getContext());
        scroller = new Scroller(context);

    }

    View capturedChild;
    View leftChild;
    private float mLastMotionX;
    private float LastMotionX;
    private float mLastMotionY;
    //    private OverScroller scroller;
    private Scroller scroller;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final float x = ev.getX();
        final float y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = x;
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int xDiff = (int) (x - mLastMotionX);
                int yDiff = (int) (y - mLastMotionY);
                if ((isOpen && xDiff < 0f) || (!isOpen && xDiff > 0)) {
                    return false;
                }
                if (Math.abs(xDiff) > mTouchSlop && Math.abs(xDiff) > Math.abs(yDiff)) {
                    LastMotionX = x;
                    return true;
                }
                break;
        }


        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) mVelocityTracker = VelocityTracker.obtain();
        mVelocityTracker.addMovement(event);
        float xDiff = 0f;
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int velocityY = (int) mVelocityTracker.getYVelocity();
                int velocityX = (int) mVelocityTracker.getXVelocity();
//                scroller.fling(layoutParams.leftMargin, 0, velocityX, velocityY, 0, maxLeft, 0, 0);
                Log.e("velocityX", "===" + velocityX);
                if (capturedChild.getX() + velocityX > maxLeft / 2f) {
                    isOpen = false;
                    scroller.startScroll((int) capturedChild.getX(), 0, (int) (maxLeft - capturedChild.getX()), 0);
                } else {
                    isOpen = true;
                    scroller.startScroll((int) capturedChild.getX(), 0, (int) -capturedChild.getX(), 0);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                xDiff = event.getX() - LastMotionX;
                if (capturedChild.getX() + xDiff <= maxLeft && capturedChild.getX() + xDiff >= 0) {
                    capturedChild.setTranslationX((int) (capturedChild.getX() + xDiff));
                } else if (capturedChild.getX() != maxLeft && capturedChild.getX() + xDiff > maxLeft) {
                    capturedChild.setTranslationX(maxLeft);
                    isOpen = false;
                } else if (capturedChild.getX() != 0 && capturedChild.getX() + xDiff < 0) {
                    capturedChild.setTranslationX(0);
                    isOpen = true;
                }
                onChangeTranslationX();
                LastMotionX = event.getX();
                break;
            case MotionEvent.ACTION_DOWN:
                break;
        }
//        Log.e("onTouchEvent===", event.getAction()
//                + "   xDiff==" + xDiff
//                + "    allXDiff===" + (event.getX() - mLastMotionX)
//                + "    getWidth==" + capturedChild.getWidth()
//                + "    getTranslationX==" + capturedChild.getTranslationX()
//                + "    getX==" + capturedChild.getX()
//        );
        return true;
    }

    private void onChangeTranslationX() {
//        Rect rect = new Rect(0, 0, leftChild.getRight(), leftChild.getBottom());
//        new Canvas().drawRect(rect, new Paint(ContextCompat.getColor(getContext(),R.color.colorPrimary)));
        if (view != null) {
            float f = ((maxLeft - capturedChild.getTranslationX()) / maxLeft) * 1.f;
            view.setAlpha(f);
            if (f > 0) {
                if (!view.isShown()) view.setVisibility(VISIBLE);
            } else {
                if (view.isShown()) view.setVisibility(GONE);
            }
        }

    }

    private int i = 0;

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            postInvalidate();
            capturedChild.setTranslationX(scroller.getCurrX());
            onChangeTranslationX();
        }
        super.computeScroll();

    }

    public void openRightLayout() {
        if (capturedChild == null) return;
        if (!isOpen) {
            isOpen = true;
            scroller.startScroll(maxLeft, 0, -maxLeft, 0);
            invalidate();
        }
    }

    public void closeRightLayout() {
        if (capturedChild == null) return;
        if (isOpen) {
            isOpen = false;
            scroller.startScroll(0, 0, maxLeft, 0);
            invalidate();
        }
    }

    public boolean isOpenRightView() {

        return isOpen;
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        super.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }
}
