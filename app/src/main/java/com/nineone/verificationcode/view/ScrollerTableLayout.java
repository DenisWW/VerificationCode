package com.nineone.verificationcode.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nineone.verificationcode.R;

public class ScrollerTableLayout extends FrameLayout {

    private boolean isLeftScroll;
    private boolean isRightScroll;

    private HorizontalScrollView rightHsv;
    private LeftRecyclerView leftRecyclerView;
    private RightRecycleView rightRecycleView;
    private LinearLayout lLLeftTitle;
    private LinearLayout lLRightTitle;


    public ScrollerTableLayout(@NonNull Context context) {
        this(context, null);
    }

    public ScrollerTableLayout(@NonNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.scroller_table_layout, this, true);
        initView();

    }

    private void initView() {
        leftRecyclerView = findViewById(R.id.rv_left);
        rightRecycleView = findViewById(R.id.rv_right);
        rightHsv = findViewById(R.id.hsv_right);
        rightRecycleView.setView(leftRecyclerView);
        lLLeftTitle = findViewById(R.id.ll_left_title);
        lLRightTitle = findViewById(R.id.ll_right_title);
        LinearLayoutManager leftLayoutManager;
        leftRecyclerView.setLayoutManager(leftLayoutManager = new LinearLayoutManager(getContext()));
        LinearLayoutManager rightLayoutManager;
        rightRecycleView.setLayoutManager(rightLayoutManager = new LinearLayoutManager(getContext()));

        leftRecyclerView.addOnScrollListener(leftOnScrollListener);
        rightRecycleView.addOnScrollListener(rightOnScrollListener);

    }

    public void setLeftAndRightAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> leftAdapter, RecyclerView.Adapter<RecyclerView.ViewHolder> rightAdapter) {
        leftRecyclerView.setAdapter(leftAdapter);
        rightRecycleView.setAdapter(rightAdapter);

        rightRecycleView.setHasFixedSize(true);
        rightRecycleView.setNestedScrollingEnabled(false);

        leftRecyclerView.setHasFixedSize(true);
        leftRecyclerView.setNestedScrollingEnabled(false);

    }

    public void setLeftTitle(View view) {
        lLLeftTitle.addView(view);
        if (view.getWidth() > 0) {
            setLeftMargin(view.getWidth());
        } else {
            view.post(() -> setLeftMargin(view.getWidth()));
        }
    }

    public void setRightTitle(View view) {
        lLRightTitle.addView(view);
    }

    public void setLeftTitleWidth(int width) {
        setLeftMargin(width);
    }

    public LeftRecyclerView getLeftRecyclerView() {
        return leftRecyclerView;
    }

    public RightRecycleView getRightRecycleView() {
        return rightRecycleView;
    }

    private void setLeftMargin(int width) {
        RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) rightHsv.getLayoutParams();
        rl.leftMargin = width;
        rightHsv.setLayoutParams(rl);
    }


    private final RecyclerView.OnScrollListener leftOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
//                rightRecyclerView.removeOnScrollListener(rightOnScrollListener);
                isLeftScroll = true;
                if (!isRightScroll) rightRecycleView.scrollBy(dx, dy);
                isLeftScroll = false;
//                rightRecyclerView.addOnScrollListener(rightOnScrollListener);
            }
        }
    };


    private final RecyclerView.OnScrollListener rightOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
//                leftRecyclerView.removeOnScrollListener(leftOnScrollListener);
                isRightScroll = true;
                if (!isLeftScroll) leftRecyclerView.scrollBy(dx, dy);
                isRightScroll = false;
//                leftRecyclerView.addOnScrollListener(leftOnScrollListener);
            }
        }

    };
}
