package com.nineone.verificationcode.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.customview.widget.ViewDragHelper;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nineone.verificationcode.R;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

public class ParentViewGroup extends FrameLayout {
    ViewDragHelper helper;
    int mTouchSlop;
    private int maxLeft;
    private RecyclerView recycler;
    private VelocityTracker mVelocityTracker;


    public ParentViewGroup(Context context) {
        super(context);
        init();
    }

    public ParentViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private boolean isFirst;
    private int mMaximumVelocity;

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.e("onLayout", "====" + changed + "====" + l + " rr==" + r + "   " + capturedChild.getWidth() + "    ==" + capturedChild.getLeft());
        if (changed) {
            capturedChild.offsetLeftAndRight(r);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        capturedChild = findViewById(R.id.right_rl);
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(new SimpleAdapter());
        maxLeft = w;
        capturedChild.layout(w, 0, w * 2, h);
        Log.e("onSizeChanged", "===" + h + "    ===" + w);
    }

    private void init() {
        if (helper == null) {
            mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
            mMaximumVelocity = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
            helper = ViewDragHelper.create(this, mTouchSlop, new ViewDragHelper.Callback() {
                @Override
                public boolean tryCaptureView(@NonNull View child, int pointerId) {
                    Log.e("tryCaptureView", "====" + child + "    pointerId ==" + pointerId);
                    if (child instanceof LinearLayout || child instanceof RelativeLayout) {
                        return true;
                    }
                    return false;
                }

                @Override
                public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
                    Log.e("onViewCaptured", "====" + capturedChild + "    activePointerId ==" + activePointerId);
                    super.onViewCaptured(capturedChild, activePointerId);
                }

                @Override
                public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
                    super.onViewPositionChanged(changedView, left, top, dx, dy);
                    Log.e("onViewPositionChanged", "===" + changedView);
                }

                @Override
                public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                    int ret = super.clampViewPositionHorizontal(child, left, dx);
                    if (maxLeft > capturedChild.getLeft() + dx && capturedChild.getLeft() + dx > 0) {
                        capturedChild.offsetLeftAndRight(left);
                    } else if (capturedChild.getLeft() < maxLeft && capturedChild.getLeft() + dx >= maxLeft) {
//                        capturedChild.offsetLeftAndRight(maxLeft - capturedChild.getLeft());
                        capturedChild.setLeft(maxLeft);
                    } else if (capturedChild.getLeft() > 0 && capturedChild.getLeft() + dx <= 0) {
                        capturedChild.setLeft(0);
                    }
                    return ret;
                }

                @Override
                public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                    Log.e("onEdgeDragStarted", "====" + capturedChild);
//                    helper.captureChildView(capturedChild, pointerId);

                }
//                @Override
//                public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
//                    Log.e("Vertical", "====" + top + "    dx ==" + dy);
//                    return super.clampViewPositionVertical(child, top, dy);
//                }
            });
        }

    }

    View capturedChild;
    private float mLastMotionX;
    private float mLastMotionY;


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("onInterceptTouchEvent", "===" + ev.getAction());
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
                if (Math.abs(xDiff) > Math.abs(yDiff)) {
                    return true;
                }
                Log.e("ACTION_MOVE", "===yDiff==" + yDiff + "   xDiff==" + xDiff);
                break;
        }


        return helper.shouldInterceptTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        helper.processTouchEvent(event);

        if (mVelocityTracker == null) mVelocityTracker = VelocityTracker.obtain();
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int velocityY = (int) mVelocityTracker.getXVelocity();
                Log.e("velocityY", "==="+velocityY);
                break;
        }
        Log.e("onTouchEvent===", event.getAction() + "===yDiff==" + event.getY() + "   xDiff==" + event.getX() + "    ===" + (event.getX() - mLastMotionX));
        return true;
    }

    private class SimpleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<String> list;

        public SimpleAdapter() {
            list = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                list.add("1213131");
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(new TextView(getContext()));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            MyViewHolder viewHolder = (MyViewHolder) holder;
            viewHolder.textView.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }

}
