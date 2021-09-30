package com.nineone.verificationcode.view;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.IntegerRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.viewpager.widget.ViewPager;

import com.nineone.verificationcode.R;

import java.util.List;

public class MineViewGroup extends RelativeLayout implements ViewPager.OnPageChangeListener {

    LinearLayout linearLayout;
    private View view;
    private RelativeLayout.LayoutParams bottomRl;
    private float density;
    private float textSize = 14f;
    private float selectTextSize = 16f;
    private int titleLayoutId = 0x2020;
    private ArgbEvaluator argbEvaluator;

    public MineViewGroup(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        density = getContext().getResources().getDisplayMetrics().density;
        if (linearLayout == null) {
            linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setId(titleLayoutId);
            addView(linearLayout);
            view = new View(context);
            bottomRl = new RelativeLayout.LayoutParams((int) (density * 20), (int) (density * 4));
            bottomRl.addRule(RelativeLayout.ALIGN_BOTTOM, linearLayout.getId());
            view.setBackgroundResource(R.drawable.bottom_bg);
            addView(view, bottomRl);
            argbEvaluator = new ArgbEvaluator();
        }


    }

    public MineViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private ViewPager viewPager;


    public void attachViewPager(ViewPager viewPager, List<String> names) {
        if (viewPager.getAdapter() == null) {
            return;
        }
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(this);

        TextView textView;
        for (int i = 0; i < names.size(); i++) {
            textView = new TextView(getContext());
            linearLayout.addView(textView);
            if (i < names.size()) {
                textView.setText(names.get(i));
            }
            int finalI = i;
            textView.setOnClickListener(v -> {
                if (finalI < viewPager.getAdapter().getCount()) {
                    viewPager.setCurrentItem(finalI);
                }
            });
        }
        addView(linearLayout);
    }

    public void attachViewPager(ViewPager viewPager) {
        if (viewPager.getAdapter() == null) {
            return;
        }
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(this);
        TextView textView;
        for (int i = 0; i < viewPager.getAdapter().getCount(); i++) {
            textView = new TextView(getContext());
            linearLayout.addView(textView);
            textView.setText(viewPager.getAdapter().getPageTitle(i));
            setTitleView(textView, i);
            int finalI = i;
            textView.setOnClickListener(v -> {
                if (finalI < viewPager.getAdapter().getCount()) {
                    viewPager.setCurrentItem(finalI);
                }
            });
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    private boolean isFirst;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset >= 0) {
            TextView tvNow = (TextView) linearLayout.getChildAt(position);

            float f = 1.f - positionOffset;
            tvNow.setTextSize(f * (selectTextSize - textSize) + textSize);

            tvNow.setTextColor((Integer) argbEvaluator.evaluate(f, ContextCompat.getColor(getContext(), R.color.color_83b2fd), ContextCompat.getColor(getContext(), R.color.color_bb7af2)));
            tvNow.setTextSize(f * (selectTextSize - textSize) + textSize);

            if (position + 1 < linearLayout.getChildCount()) {
                TextView tvNext = (TextView) linearLayout.getChildAt(position + 1);
                tvNext.setTextSize(positionOffset * (selectTextSize - textSize) + textSize);
                tvNext.setTextColor((Integer) argbEvaluator.evaluate(positionOffset, ContextCompat.getColor(getContext(), R.color.color_83b2fd), ContextCompat.getColor(getContext(), R.color.color_bb7af2)));


                if (bottomViewBuilder != null && bottomViewBuilder.width == 0) {
                    bottomRl.leftMargin = (int) (tvNow.getLeft() + bottomViewBuilder.leftMargin + (tvNext.getLeft() - tvNow.getLeft()) * positionOffset);
                    bottomRl.width = (int) (((tvNext.getWidth() - bottomViewBuilder.leftMargin - bottomViewBuilder.rightMargin) - ((tvNow.getWidth() - bottomViewBuilder.leftMargin - bottomViewBuilder.rightMargin))) * positionOffset
                                                + ((tvNow.getWidth() - bottomViewBuilder.leftMargin - bottomViewBuilder.rightMargin)));
                    if (!isFirst) {
                        isFirst = true;
                        view.setLayoutParams(bottomRl);
                    }
                } else {
                    bottomRl.leftMargin = (int) (tvNow.getLeft() + (tvNext.getWidth() - bottomRl.width) / 2 + (tvNext.getLeft() - tvNow.getLeft()) * positionOffset);
                    if (!isFirst) {
                        isFirst = true;
                        view.setLayoutParams(bottomRl);
                        setBottomView();
                    }
                }

            }

//            Log.e("onPageScrolled", "=          position=" + position + "    positionOffset== " + positionOffset + "    color==" + linearLayout.getChildAt(position).getHeight() + "   " + ((linearLayout.getChildAt(position).getWidth() - bottomRl.width) / 2));

        }

    }

    public void setBottomViewHeight(int dp) {
        bottomRl.height = (int) (density * dp + .5f);
        view.setLayoutParams(bottomRl);
    }

    public void setBottomViewHeightAndWidth(int width, int height) {
        bottomRl.height = (int) (density * height + .5f);
        bottomRl.width = (int) (density * width + .5f);
        view.setLayoutParams(bottomRl);
    }

    public void setBottomViewWidth(int dp) {
        bottomRl.width = (int) (density * dp + .5f);
        view.setLayoutParams(bottomRl);
    }

    public void setBottomViewBackground(int resId) {
        view.setBackgroundResource(resId);
    }


    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private TitleViewBuilder builder;
    private BottomViewBuilder bottomViewBuilder;

    public void buildTextView(TitleViewBuilder viewBuilder) {
        this.builder = viewBuilder;
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            TextView textView = (TextView) linearLayout.getChildAt(i);
            setTitleView(textView, i);
        }
    }

    public void buildBottomView(BottomViewBuilder viewBuilder) {
        this.bottomViewBuilder = viewBuilder;
        setBottomView();


    }

    private void setBottomView() {
        bottomRl.width = bottomViewBuilder.width > 0 ? bottomViewBuilder.width : bottomRl.width;
        bottomRl.height = bottomViewBuilder.height > 0 ? bottomViewBuilder.height : bottomRl.height;
        bottomRl.leftMargin = bottomViewBuilder.leftMargin;
        bottomRl.rightMargin = bottomViewBuilder.rightMargin;
        bottomRl.bottomMargin = bottomViewBuilder.bottomMargin;
        bottomRl.topMargin = bottomViewBuilder.topMargin;
        if (bottomViewBuilder.backGround > 0) {
            view.setBackgroundResource(bottomViewBuilder.backGround);
        }
    }

    private int minWidth, minHeight;

    private void setTitleView(TextView textView, int i) {
        textView.setGravity(Gravity.CENTER);
        if (minWidth == 0 || minHeight == 0) {
            textView.setTextSize(16f);
            minWidth = (int) textView.getPaint().measureText(textView.getText().toString());
            minHeight = textView.getLineHeight() + 10;
        }
        textView.setMinWidth(minWidth);
        textView.setMinHeight(minHeight);
        if (builder == null) {
            textView.setTextSize(14f);
            textView.setTextColor(Color.parseColor("#55000000"));
            if (i == viewPager.getCurrentItem()) {
                textView.setTextSize(16f);
                textView.setTextColor(Color.parseColor("#000000"));
            }

            return;
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
        layoutParams.height = builder.height > 0 ? builder.height : LinearLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.width = builder.width > 0 ? builder.width : LinearLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.leftMargin = builder.leftMargin;
        layoutParams.rightMargin = builder.rightMargin;
        layoutParams.topMargin = builder.topMargin;
        layoutParams.bottomMargin = builder.bottomMargin;
        textView.setTextSize(builder.textSize > 0 ? builder.textSize : 14f);
        textView.setTextColor(builder.textColor > 0 ? builder.textColor : Color.parseColor("#55000000"));
        if (i == viewPager.getCurrentItem()) {
            textView.setTextSize(builder.selectTextSize > 0 ? builder.selectTextSize : 16f);
            textView.setTextColor(builder.selectTextColor > 0 ? builder.selectTextColor : Color.parseColor("#000000"));
        }
        textView.setPadding(builder.paddingLeft, builder.paddingTop, builder.paddingRight, builder.paddingBottom);
        textView.setLayoutParams(layoutParams);
    }


    public static class BottomViewBuilder {
        public int width;
        public int height;
        public int leftMargin;
        public int rightMargin;
        public int bottomMargin;
        public int topMargin;
        public int backGround;

        public BottomViewBuilder setWidth(int width) {
            this.width = width;
            return this;
        }

        public BottomViewBuilder setHeight(int height) {
            this.height = height;
            return this;
        }


        public BottomViewBuilder setLeftMargin(int leftMargin) {
            this.leftMargin = leftMargin;
            return this;
        }

        public BottomViewBuilder setRightMargin(int rightMargin) {
            this.rightMargin = rightMargin;
            return this;
        }

        public BottomViewBuilder setBottomMargin(int bottomMargin) {
            this.bottomMargin = bottomMargin;
            return this;
        }

        public BottomViewBuilder setTopMargin(int topMargin) {
            this.topMargin = topMargin;
            return this;
        }

        public BottomViewBuilder setBackGround(int backGround) {
            this.backGround = backGround;
            return this;
        }

        public BottomViewBuilder build() {
            return this;
        }
    }

    public static class TitleViewBuilder {
        public int width;
        public int height;
        @IntegerRes
        public int textColor;
        @IntegerRes
        public int selectTextColor;


        public int paddingLeft;
        public int paddingRight;
        public int paddingTop;
        public int paddingBottom;

        public float selectTextSize;
        public float textSize;


        public int leftMargin;
        public int rightMargin;
        public int bottomMargin;
        public int topMargin;

        public TitleViewBuilder() {
        }

        public TitleViewBuilder setWidth(int width) {
            this.width = width;
            return this;
        }

        public TitleViewBuilder setHeight(int height) {
            this.height = height;
            return this;
        }

        public TitleViewBuilder setPaddingLeft(int paddingLeft) {
            this.paddingLeft = paddingLeft;
            return this;
        }

        public TitleViewBuilder setPaddingRight(int paddingRight) {
            this.paddingRight = paddingRight;
            return this;
        }

        public TitleViewBuilder setPaddingTop(int paddingTop) {
            this.paddingTop = paddingTop;
            return this;
        }

        public TitleViewBuilder setPaddingBottom(int paddingBottom) {
            this.paddingBottom = paddingBottom;
            return this;
        }

        public TitleViewBuilder setSelectTextSize(float selectTextSize) {
            this.selectTextSize = selectTextSize;
            return this;
        }

        public TitleViewBuilder setTextSize(float textSize) {
            this.textSize = textSize;
            return this;
        }

        public TitleViewBuilder setLeftMargin(int leftMargin) {
            this.leftMargin = leftMargin;
            return this;
        }

        public TitleViewBuilder setRightMargin(int rightMargin) {
            this.rightMargin = rightMargin;
            return this;
        }

        public TitleViewBuilder setBottomMargin(int bottomMargin) {
            this.bottomMargin = bottomMargin;
            return this;
        }

        public TitleViewBuilder setTopMargin(int topMargin) {
            this.topMargin = topMargin;
            return this;
        }

        public TitleViewBuilder setTextColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public TitleViewBuilder setSelectTextColor(int selectTextColor) {
            this.selectTextColor = selectTextColor;
            return this;
        }

        public TitleViewBuilder build() {
            return this;
        }
    }
}
