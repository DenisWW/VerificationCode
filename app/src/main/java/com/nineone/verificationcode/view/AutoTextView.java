package com.nineone.verificationcode.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.BoringLayout;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.nineone.verificationcode.R;

public class AutoTextView extends AppCompatTextView {
    String tag = getClass().getSimpleName();

    private float initTextSize;
    private boolean isInit = true;
    private float minTextSize;
    private final String threePoint = "...";

    public AutoTextView(Context context) {
        super(context);
    }


    public AutoTextView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoTextView);
        minTextSize = typedArray.getDimensionPixelSize(R.styleable.AutoTextView_autoMinTextSize, -1);
        typedArray.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getRealRectWith() > 0 && isInit && !TextUtils.isEmpty(getText())) {
            isInit = false;
            resetTextSize(getText());
//            float f = getRealRectWith() / getPaint().measureText(getText().toString());
//            if (isInit && f < 1.f) {
//                isInit = false;
//                resetTextSizeByPx(getTextSize() * f);
//            }
        }
        Log.e(tag, " onMeasure=====" + getRealRectWith() + "   " + getMeasuredHeight() + "   " + getPaint().getTextSize());

    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (initTextSize == 0) initTextSize = getTextSize();
        else resetTextSizeByPx(initTextSize);
        isInit = true;
        super.setText(text, type);

    }


    public float getRealRectWith() {
        return getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }


    public void resetTextSize(CharSequence sequence) {
        if (getMaxLines() == Integer.MAX_VALUE) return;
        int maxLines = getMaxLines();
        float f = getRealRectWith() * maxLines / getPaint().measureText(sequence.toString());
        if (f < 1.f) {
            float zoomSize = getTextSize() * f;
            if (zoomSize < minTextSize) {
                resetTextSizeByPx(minTextSize);
//            int end;
//            BoringLayout boringLayout = BoringLayout.make(sequence, getPaint(), (int) getRealRectWith(), getLayoutAlignment(), 0.0f, 1.0f,
//                    new BoringLayout.Metrics(), true);
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                StaticLayout layout = StaticLayout.Builder.obtain(sequence, 0, sequence.length(), getPaint(), (int) getRealRectWith())
//                        .setEllipsize(TextUtils.TruncateAt.END).setEllipsizedWidth((int) getRealRectWith()).build();
//                end = layout.getEllipsisCount(1);
//                getLayout().getEllipsisCount(0);
//            } else {
//                end = findBestPosition(sequence, 0, sequence.length());
//            }
//            setText(sequence.subSequence(0, end) + threePoint);
                setText(TextUtils.ellipsize(sequence, getPaint(), getRealRectWith() * maxLines, TextUtils.TruncateAt.START));
            } else {
                resetTextSizeByPx(zoomSize);
            }
        }

    }

    public int findBestPosition(CharSequence charSequence, int start, int end) {
        if (start > end) return end;
        int center = (start + end) / 2;
        CharSequence sequence = charSequence.subSequence(0, center);
        if (getPaint().measureText(sequence + threePoint) > getRealRectWith()) {
            center = findBestPosition(charSequence, 0, center - 1);
        } else if (getPaint().measureText(sequence + threePoint) < getRealRectWith()) {
            center = findBestPosition(charSequence, center + 1, end);
        }
        return center;
    }

    /**
     * initTextSize 字体大小  :单位像素
     */
    private void resetTextSizeByPx(float initTextSize) {
        setTextSize(TypedValue.COMPLEX_UNIT_PX, initTextSize);
    }

    private Layout.Alignment getLayoutAlignment() {
        Layout.Alignment alignment;
        switch (getTextAlignment()) {
            case TEXT_ALIGNMENT_GRAVITY:
                switch (getGravity() & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) {
                    case Gravity.START:
                        alignment = Layout.Alignment.ALIGN_NORMAL;
                        break;
                    case Gravity.END:
                        alignment = Layout.Alignment.ALIGN_OPPOSITE;
                        break;
                    case Gravity.LEFT:
                        alignment = Layout.Alignment.ALIGN_NORMAL;
                        break;
                    case Gravity.RIGHT:
                        alignment = Layout.Alignment.ALIGN_OPPOSITE;
                        break;
                    case Gravity.CENTER_HORIZONTAL:
                        alignment = Layout.Alignment.ALIGN_CENTER;
                        break;
                    default:
                        alignment = Layout.Alignment.ALIGN_NORMAL;
                        break;
                }
                break;
            case TEXT_ALIGNMENT_TEXT_START:
                alignment = Layout.Alignment.ALIGN_NORMAL;
                break;
            case TEXT_ALIGNMENT_TEXT_END:
                alignment = Layout.Alignment.ALIGN_OPPOSITE;
                break;
            case TEXT_ALIGNMENT_CENTER:
                alignment = Layout.Alignment.ALIGN_CENTER;
                break;
            case TEXT_ALIGNMENT_VIEW_START:
                alignment = (getLayoutDirection() == LAYOUT_DIRECTION_RTL) ? Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_NORMAL;
                break;
            case TEXT_ALIGNMENT_VIEW_END:
                alignment = (getLayoutDirection() == LAYOUT_DIRECTION_RTL)
                        ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
                break;
            case TEXT_ALIGNMENT_INHERIT:
            default:
                alignment = Layout.Alignment.ALIGN_NORMAL;
                break;
        }
        return alignment;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
//        Log.e(tag, " onLayout=====" + getRealRectWith() + "   " + getMeasuredHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        Log.e(tag, " onSizeChanged=====" + getRealRectWith() + "   " + getMeasuredHeight());
    }


}
