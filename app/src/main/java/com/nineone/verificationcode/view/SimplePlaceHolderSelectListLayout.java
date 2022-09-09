package com.nineone.verificationcode.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import com.nineone.verificationcode.R;

import java.util.List;

/**
 * 居中选择List布局
 */
public class SimplePlaceHolderSelectListLayout extends FrameLayout {

    public SimplePlaceHolderSelectListLayout(@NonNull Context context) {
        this(context, null);
    }

    private int topAndBottomItem = 3;
    private int topAndBottomPadding = 5;
    private float normalTextSize = 16F, maxTextSize = 18F;

    public SimplePlaceHolderSelectListLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private RecyclerView leftRecycler, centerRecycler, rightRecycler;
    private SimpleSelectHelper.SelectTextViewAdapter leftAdapter, centerAdapter, rightAdapter;
    private SimpleSelectHelper.CustomVerticallyLayoutManager leftManager, centerManager, rightManager;
    private int measureHeight;

    private void initView(Context context) {
        TextView textView = new TextView(context);
        textView.setTextSize(18F);
        textView.setMaxLines(1);
        float density = getResources().getDisplayMetrics().density;
        textView.setPadding(0, (int) density * topAndBottomPadding, 0, (int) density * topAndBottomPadding);
        textView.setBackgroundResource(R.drawable.solid_ff9500_5);
        addView(textView);
        LayoutParams layoutParams = (LayoutParams) textView.getLayoutParams();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = LayoutParams.MATCH_PARENT;
        layoutParams.height = LayoutParams.WRAP_CONTENT;
        textView.setLayoutParams(layoutParams);

        int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        textView.measure(w, h);
        measureHeight = textView.getMeasuredHeight();

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        leftRecycler = new RecyclerView(context);
        centerRecycler = new RecyclerView(context);
        rightRecycler = new RecyclerView(context);

        linearLayout.addView(leftRecycler);
        linearLayout.addView(centerRecycler);
        linearLayout.addView(rightRecycler);


        leftRecycler.setOverScrollMode(OVER_SCROLL_NEVER);
        centerRecycler.setOverScrollMode(OVER_SCROLL_NEVER);
        rightRecycler.setOverScrollMode(OVER_SCROLL_NEVER);

        addView(linearLayout);
        layoutParams = (LayoutParams) linearLayout.getLayoutParams();
        layoutParams.width = LayoutParams.MATCH_PARENT;
        layoutParams.height = measureHeight * (topAndBottomItem * 2 + 1);
        linearLayout.setLayoutParams(layoutParams);

        LinearLayout.LayoutParams linearLayoutParams = (LinearLayout.LayoutParams) leftRecycler.getLayoutParams();
        linearLayoutParams.width = 0;
        linearLayoutParams.weight = 1;
        layoutParams.leftMargin = (int) (density * 28);
        linearLayoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
        leftRecycler.setLayoutParams(linearLayoutParams);

        linearLayoutParams = (LinearLayout.LayoutParams) centerRecycler.getLayoutParams();
        linearLayoutParams.width = 0;
        linearLayoutParams.weight = 1;
        linearLayoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
        centerRecycler.setLayoutParams(linearLayoutParams);

        linearLayoutParams = (LinearLayout.LayoutParams) rightRecycler.getLayoutParams();
        linearLayoutParams.width = 0;
        linearLayoutParams.weight = 1;
        layoutParams.rightMargin = (int) (density * 28);
        linearLayoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
        rightRecycler.setLayoutParams(linearLayoutParams);


        leftRecycler.setLayoutManager(leftManager = SimpleSelectHelper.createVerticallyLayoutManager(context, normalTextSize, maxTextSize));
        centerRecycler.setLayoutManager(centerManager = SimpleSelectHelper.createVerticallyLayoutManager(context, normalTextSize, maxTextSize));
        rightRecycler.setLayoutManager(rightManager = SimpleSelectHelper.createVerticallyLayoutManager(context, normalTextSize, maxTextSize));


        SimpleSelectHelper.SnapVerticalCenterHelper leftHelper = SimpleSelectHelper.createVerticallySnapVerticalHelper(normalTextSize, maxTextSize);
        leftHelper.attachToRecyclerView(leftRecycler);
        SimpleSelectHelper.SnapVerticalCenterHelper centerHelper = SimpleSelectHelper.createVerticallySnapVerticalHelper(normalTextSize, maxTextSize);
        centerHelper.attachToRecyclerView(centerRecycler);
        SimpleSelectHelper.SnapVerticalCenterHelper rightHelper = SimpleSelectHelper.createVerticallySnapVerticalHelper(normalTextSize, maxTextSize);
        rightHelper.attachToRecyclerView(rightRecycler);

        leftAdapter = SimpleSelectHelper.createVerticallyAdapter(normalTextSize, maxTextSize, topAndBottomPadding,
                topAndBottomItem, Gravity.START | Gravity.CENTER_VERTICAL);
        centerAdapter = SimpleSelectHelper.createVerticallyAdapter(normalTextSize, maxTextSize, topAndBottomPadding,
                topAndBottomItem, Gravity.CENTER);
        rightAdapter = SimpleSelectHelper.createVerticallyAdapter(normalTextSize, maxTextSize, topAndBottomPadding,
                topAndBottomItem, Gravity.END | Gravity.CENTER_VERTICAL);


        leftRecycler.setAdapter(leftAdapter);
        centerRecycler.setAdapter(centerAdapter);
        rightRecycler.setAdapter(rightAdapter);


    }

    public void setLeftSelectListener(SimpleSelectListener listener) {
        if (listener != null && leftManager != null) leftManager.setListener(position -> {
            listener.onSelectItem(position - topAndBottomItem);
        });
    }

    public void setCenterSelectListener(SimpleSelectListener listener) {
        if (listener != null && centerManager != null) centerManager.setListener(position -> {
            listener.onSelectItem(position - topAndBottomItem);
        });
    }

    public void setRightSelectListener(SimpleSelectListener listener) {
        if (listener != null && rightManager != null) rightManager.setListener(position -> {
            listener.onSelectItem(position - topAndBottomItem);
        });
    }

    public void setLeftDataList(List<String> leftData, int currentPosition) {
        if (leftAdapter != null) leftAdapter.setData(leftData, currentPosition);
        if (!leftRecycler.isShown()) leftRecycler.setVisibility(VISIBLE);
        if (leftManager != null) leftManager.scrollToPositionWithOffset(currentPosition, 0);
    }


    public void setCenterDataList(List<String> centerData, int currentPosition) {
        if (centerAdapter != null) centerAdapter.setData(centerData, currentPosition);
        if (!centerRecycler.isShown()) centerRecycler.setVisibility(VISIBLE);
        if (centerManager != null) centerManager.scrollToPositionWithOffset(currentPosition, 0);
    }

    public void setRightDataList(List<String> rightData, int currentPosition) {
        if (rightAdapter != null) rightAdapter.setData(rightData, currentPosition);
        if (!rightRecycler.isShown()) rightRecycler.setVisibility(VISIBLE);
        if (rightManager != null) {
            rightManager.scrollToPositionWithOffset(currentPosition, 0);
        }
    }

    /**
     * 选中监听
     */
    public interface SimpleSelectListener {
        void onSelectItem(int position);
    }
}
