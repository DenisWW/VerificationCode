package com.jzsec.web.apptest.dialog;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SimpleDialog extends Dialog {

    public SimpleDialog(@NonNull Context context) {
        super(context);
    }

    public SimpleDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SimpleDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
