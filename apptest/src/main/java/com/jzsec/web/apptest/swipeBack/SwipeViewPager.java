package com.jzsec.web.apptest.swipeBack;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewParent;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by Denis on 2018/1/12.
 */

public class SwipeViewPager extends ViewPager {
    private int mTouchSlop;

    public SwipeViewPager(Context context) {
        super(context);
        init();
    }

    public SwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        final Context context = getContext();
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledPagingTouchSlop();
    }

    //    float mLastX;
//    float mLastY;
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (getCurrentItem() == 0) {
//            float x = ev.getX();
//            float y = ev.getY();
//            switch (ev.getAction()) {
//                case MotionEvent.ACTION_MOVE:
//                    float xDiff = Math.abs(x - mLastY);
//                    float yDiff = Math.abs(y - mLastY);
//                    //在第一页，判断到是向左边滑动，即想滑动第二页
//                    if (xDiff > 0 && x - mLastX < 0 && xDiff * 0.5f > yDiff) {
//                        //告诉父容器不要拦截事件
//                        getParent().getParent().getParent().requestDisallowInterceptTouchEvent(true);
//                    } else if (yDiff > mTouchSlop && xDiff < mTouchSlop) {
//                        //竖直滑动时，告诉父容器拦截事件，用于在ScrollView中可以竖直滑动
//
//                        getParent().getParent().getParent().requestDisallowInterceptTouchEvent(false);
//                    }
//                    break;
//                case MotionEvent.ACTION_UP:
//                    getParent().getParent().getParent().requestDisallowInterceptTouchEvent(false);
//                    break;
//            }
//            mLastX = x;
//            mLastY = y;
//        } else {
//            getParent(). getParent(). getParent().requestDisallowInterceptTouchEvent(true);
//        }
//        return super.dispatchTouchEvent(ev);
//    }
// 滑动距离及坐标 归还父控件焦点
    private float xDistance, yDistance, xLast, yLast, xDown, mLeft;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        ViewParent viewParent;
        if (getParent()!=null&&getParent().getParent() != null&&getParent().getParent().getParent() != null)
            viewParent = getParent().getParent().getParent();
        else if (getParent()!=null&&getParent().getParent() != null) viewParent = getParent().getParent();
        else  viewParent = getParent();
        viewParent.requestDisallowInterceptTouchEvent(true);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                xDown = ev.getX();
                mLeft = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;
//                mLeft < 100 ||
                if (xDistance < yDistance) {
                    viewParent.requestDisallowInterceptTouchEvent(false);
                } else {
                    if (getCurrentItem() == 0) {
                        if (curX < xDown) {
                            viewParent.requestDisallowInterceptTouchEvent(true);
                        } else {
                            viewParent.requestDisallowInterceptTouchEvent(false);
                        }
                    } else if (getCurrentItem() == (getAdapter().getCount() - 1)) {
                        if (curX > xDown) {
                            viewParent.requestDisallowInterceptTouchEvent(true);
                        } else {
                            viewParent.requestDisallowInterceptTouchEvent(false);
                        }
                    } else {
                        viewParent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

}
