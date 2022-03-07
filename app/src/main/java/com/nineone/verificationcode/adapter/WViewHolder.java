package com.nineone.verificationcode.adapter;

import static com.didichuxing.doraemonkit.kit.lbs.common.Constants.getContext;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.nineone.verificationcode.R;
import com.nineone.verificationcode.uikit.BaseViewHolder;

public class WViewHolder extends BaseViewHolder<String> {


    private TextView[] textViews;
    private LinearLayout layout;
    private String bean;

    public WViewHolder(@NonNull ViewGroup itemView) {
        super(R.layout.item_right_layout, itemView, false);
    }

    @Override
    protected void initView() {
        layout = (LinearLayout) itemView;
        textViews = new TextView[5];
        TextView textView;
        for (int i = 0; i < textViews.length; i++) {
            textView = new TextView(layout.getContext());
            textView.setWidth(200);
            textView.setMaxLines(1);
            textView.setGravity(Gravity.CENTER);
            textViews[i] = textView;
            layout.addView(textView);
        }


//        textView.setOnClickListener(v -> {
//            if (!TextUtils.isEmpty(String.valueOf(bean)))
//                Toast.makeText(v.getContext(), bean, Toast.LENGTH_SHORT).show();
//        });
//        TypedValue typedValue = new TypedValue();
//        textView. getContext().getTheme()
//                .resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
//        int[] attribute = new int[]{android.R.attr.selectableItemBackground};
//        TypedArray typedArray =textView. getContext().getTheme().obtainStyledAttributes(typedValue.resourceId, attribute);
//        textView.setBackground(typedArray.getDrawable(0));

    }

    @Override
    protected void parseBeanError(String err) {
        super.parseBeanError(err);
    }

    @Override
    protected void bindViewHolder(int pos, String bean) {
        this.bean = bean;
        for (TextView textView : textViews) {
            Log.e("textView===", "===" + textView.isShown());
            textView.setText(bean);
        }

    }

}
