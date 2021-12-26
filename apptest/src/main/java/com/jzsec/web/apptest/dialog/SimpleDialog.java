package com.jzsec.web.apptest.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jzsec.web.apptest.R;

public class SimpleDialog extends Dialog {

    public SimpleDialog(@NonNull Context context) {
        this(context, R.style.simpleDialog);
    }

    public SimpleDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_simple_layout);
        init();
    }

    private void init() {
        initDialogConfig();
        initView();
    }

    private void initView() {

        TextView textView = findViewById(R.id.simple_cancel_tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initDialogConfig() {
        Window window = getWindow();

        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity= Gravity.CENTER;
        window.setAttributes(layoutParams);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
    }
}
